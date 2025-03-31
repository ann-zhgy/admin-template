package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.exception.ExceptionEnum;
import life.klstoys.admin.template.rbac.constant.AdminTemplateConstant;
import life.klstoys.admin.template.rbac.converter.AppInfoConverter;
import life.klstoys.admin.template.rbac.converter.RoleConverter;
import life.klstoys.admin.template.rbac.converter.UserConverter;
import life.klstoys.admin.template.rbac.converter.UserRoleConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import life.klstoys.admin.template.rbac.dal.domain.UserAppDO;
import life.klstoys.admin.template.rbac.dal.domain.UserDO;
import life.klstoys.admin.template.rbac.dal.domain.UserRoleDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.dal.repository.BackendFunctionRepository;
import life.klstoys.admin.template.rbac.dal.repository.RedisRepository;
import life.klstoys.admin.template.rbac.dal.repository.RoleRepository;
import life.klstoys.admin.template.rbac.dal.repository.UserAppRepository;
import life.klstoys.admin.template.rbac.dal.repository.UserRepository;
import life.klstoys.admin.template.rbac.dal.repository.UserRoleRepository;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
import life.klstoys.admin.template.rbac.entity.EndPointMenuInfoEntity;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.MenuService;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginPasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.RegisterRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.SendCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.UpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdateRequest;
import life.klstoys.admin.template.rbac.web.controller.response.UserRoleResponse;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import life.klstoys.admin.template.utils.StreamUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:29
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;
    @Setter(onMethod_ = @Autowired)
    private RoleRepository roleRepository;
    @Setter(onMethod_ = @Autowired)
    private BackendFunctionRepository backendFunctionRepository;
    @Setter(onMethod_ = @Autowired)
    private UserRoleRepository userRoleRepository;
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    @Setter(onMethod_ = @Autowired)
    private UserAppRepository userAppRepository;
    @Setter(onMethod_ = @Autowired)
    private RedisRepository redisRepository;
    @Setter(onMethod_ = @Autowired)
    private MenuService menuService;
    @Setter(onMethod_ = {@Autowired, @Qualifier("commonExecutorService")})
    private ThreadPoolTaskExecutor commonExecutorService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        UserDO userDO = userRepository.selectUserByUsername(request.getUsername());
        if (Objects.nonNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_IS_EXISTS);
        }
        userDO = userRepository.selectUserByEmail(request.getEmail());
        if (Objects.nonNull(userDO)) {
            throw new BizException(RbacExceptionEnum.EMAIL_IS_EXISTS);
        }
        userDO = userRepository.selectUserByPhone(request.getPhone());
        if (Objects.nonNull(userDO)) {
            throw new BizException(RbacExceptionEnum.PHONE_IS_EXISTS);
        }
        userRepository.insert(UserConverter.INSTANCE.convertRequestToDO(request));
    }

    @Override
    public String loginByPassword(LoginPasswordRequest request) {
        UserDO userDO = userRepository.selectUserByMultiFlag(request.getUsername());
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        if (!CommonUtil.matchPassword(request.getPassword(), userDO.getPassword())) {
            throw new BizException(RbacExceptionEnum.USER_INFO_NOT_MATCH);
        }
        String token = CommonUtil.randomUUID();
        cacheLoginUserAndToken(token, userDO.getId(), request.getAppKey());
        return token;
    }

    @Override
    public String loginByCaptcha(LoginCaptchaRequest request) {
        UserDO userDO = userRepository.selectUserByTwoFlag(request.getUsername());
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        if (!redisRepository.checkCaptcha(request.getCaptcha(), request.getUsername())) {
            throw new BizException(RbacExceptionEnum.CAPTCHA_ERROR);
        }
        redisRepository.removeCaptcha(request.getCaptcha());
        String token = CommonUtil.randomUUID();
        cacheLoginUserAndToken(token, userDO.getId(), request.getAppKey());
        return token;
    }

    @Override
    public void sendCaptcha(SendCaptchaRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserInfoEntity baseInfo(boolean queryMenus, String appKey) {
        UserAuthorInfoDO userInfo = RequestContext.getUserInfo();
        List<EndPointMenuInfoEntity> menus = null;
        if (queryMenus) {
            menus = menuService.queryUserMenus(userInfo.getUsername(), appKey);
        }
        return UserConverter.INSTANCE.convertAuthorInfoDOToEntity(userInfo, menus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest request) {
        UserDO userDO = userRepository.selectById(RequestContext.getUserInfo().getId());
        CommonUtil.callSetterIfParamNotBlank(userDO::setUsername, request.getUsername());
        CommonUtil.callSetterIfParamNotBlank(userDO::setEmail, request.getEmail());
        CommonUtil.callSetterIfParamNotBlank(userDO::setPhone, request.getPhone());
        userRepository.updateById(userDO);
    }

    @Override
    public PageResult<UserInfoEntity> list(UserListRequest request) {
        PageInfo<UserDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<UserDO> list = userRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        return PageHelperUtil.convertToPage(pageInfo, UserConverter.INSTANCE::convertDOToEntity);
    }

    @Override
    public UserInfoEntity query(Long id) {
        UserDO userInfo = userRepository.selectById(id);
        return UserConverter.INSTANCE.convertDOToEntity(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        if (Objects.equals(id, RequestContext.getUserInfo().getId())) {
            throw new BizException(RbacExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        UserDO userInfo = userRepository.selectById(id);
        if (Objects.isNull(userInfo)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        userInfo.setStatus(CommonStatusEnum.DISABLE);
        userRepository.updateById(userInfo);
        redisRepository.removeTokenById(userInfo.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        if (Objects.equals(id, RequestContext.getUserInfo().getId())) {
            throw new BizException(RbacExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        UserDO userInfo = userRepository.selectById(id);
        if (Objects.isNull(userInfo)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        userInfo.setStatus(CommonStatusEnum.ENABLE);
        userRepository.updateById(userInfo);
    }

    @Override
    public void logout() {
        redisRepository.removeToken(RequestContext.getToken());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(UserUpdatePasswordRequest request) {
        UserDO userDO = userRepository.selectById(RequestContext.getUserInfo().getId());
        if (!CommonUtil.matchPassword(request.getOriginPassword(), userDO.getPassword())) {
            throw new BizException(RbacExceptionEnum.ORIGIN_PASSWORD_ERROR);
        }
        userDO.setPassword(CommonUtil.encodePassword(request.getNewPassword()));
        userRepository.updateById(userDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindRole(Long id, Set<Long> roleIds) {
        UserDO userDO = userRepository.selectById(id);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        if (Objects.equals(userDO.getUsername(), RequestContext.getUserInfo().getUsername())) {
            throw new BizException(RbacExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        List<RoleDO> roles = roleRepository.selectByIds(roleIds);
        if (CollectionUtils.isEmpty(roles)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        Set<String> existRoleNos = userRoleRepository.selectByUsername(userDO.getUsername());
        List<UserRoleDO> userRoleList = roles.stream()
                .filter(roleDO -> !existRoleNos.contains(roleDO.getNo()))
                .map(roleDO -> UserRoleConverter.INSTANCE.buildDOWithUserAndRole(userDO.getUsername(), roleDO))
                .toList();
        if (CollectionUtils.isNotEmpty(userRoleList)) {
            userRoleRepository.batchInsert(userRoleList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindRole(Long id, Set<Long> roleIds) {
        UserDO userDO = userRepository.selectById(id);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        if (Objects.equals(userDO.getUsername(), RequestContext.getUserInfo().getUsername())) {
            throw new BizException(RbacExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        List<RoleDO> roles = roleRepository.selectByIds(roleIds);
        if (CollectionUtils.isEmpty(roles)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        Set<String> existRoleNos = roles.stream().map(RoleDO::getNo).collect(Collectors.toSet());
        userRoleRepository.deleteByUsernameAndRoleNos(userDO.getUsername(), existRoleNos);
    }

    @Override
    public List<UserRoleResponse> queryUserRoles(Long userId) {
        UserDO userDO = userRepository.selectById(userId);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        Future<List<AppInfoDO>> appInfosFuture = commonExecutorService.submit(() -> appInfoRepository.selectByUsername(userDO.getUsername()));
        List<AppInfoDO> appInfoDOS;
        List<RoleDO> roleDOS = roleRepository.queryByUsernameAndAppkey(userDO.getUsername(), null);
        try {
            appInfoDOS = appInfosFuture.get(AdminTemplateConstant.ASYNC_TASK_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("查询失败：userId = {}", userId, e);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR, e);
        }
        Map<String, List<RoleDO>> appkeyRolesMap = roleDOS.stream().collect(Collectors.groupingBy(RoleDO::getAppKey));
        Map<String, AppInfoDO> appInfoMap = appInfoDOS.stream().collect(Collectors.toMap(AppInfoDO::getAppKey, Function.identity(), (a, b) -> a));
        return StreamUtil.ofNullable(appInfoDOS)
                .map(AppInfoDO::getAppKey).distinct()
                .map(appKey -> {
                    UserRoleResponse response = new UserRoleResponse();
                    response.setAppInfo(AppInfoConverter.INSTANCE.convertDOToEntity(appInfoMap.get(appKey)));
                    List<RoleInfoEntity> roleInfoEntities = StreamUtil.ofNullable(appkeyRolesMap.get(appKey))
                            .map(roleDO -> RoleConverter.INSTANCE.convertDOToEntity(roleDO, appInfoMap.get(appKey), null)).toList();
                    response.setRoleInfos(roleInfoEntities);
                    return response;
                }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePasswordNoLogin(UpdatePasswordRequest request) {
        UserDO userDO = userRepository.selectUserByTwoFlag(request.getUsername());
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        if (!redisRepository.checkCaptcha(request.getCaptcha(), request.getUsername())) {
            throw new BizException(RbacExceptionEnum.CAPTCHA_ERROR);
        }
        userDO.setPassword(CommonUtil.encodePassword(request.getNewPassword()));
        userRepository.updateById(userDO);
    }

    @Override
    public PageResult<RoleInfoEntity> queryUserAppRoles(Long userId, String appKey, PageRequest request) {
        UserDO userDO = userRepository.selectById(userId);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        PageInfo<RoleDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<RoleDO> list = roleRepository.queryByUsernameAndAppkey(userDO.getUsername(), appKey.trim());
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        return PageHelperUtil.convertToPage(pageInfo, roleDO -> RoleConverter.INSTANCE.convertDOToEntity(roleDO, null, null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindApp(Long userId, Set<String> appKeys) {
        UserDO userDO = userRepository.selectById(userId);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByAppKeys(appKeys);
        if (CollectionUtils.isEmpty(appInfoDOS)) {
            throw new BizException(RbacExceptionEnum.APP_NOT_EXISTS);
        }
        Set<String> existAppKeys = userAppRepository.selectByUsername(userDO.getUsername());
        List<UserAppDO> userAppInfoDOS = appInfoDOS.stream()
                .filter(appInfoDO -> !existAppKeys.contains(appInfoDO.getAppKey()))
                .map(appInfoDO -> UserConverter.INSTANCE.buildUserAppInfo(userDO, appInfoDO))
                .toList();
        userAppRepository.batchInsert(userAppInfoDOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindApp(Long userId, Set<String> appKeys) {
        UserDO userDO = userRepository.selectById(userId);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByAppKeys(appKeys);
        if (CollectionUtils.isEmpty(appInfoDOS)) {
            log.warn("传入的appKey无效");
            return;
        }
        userAppRepository.deleteByUsernameAndAppKeys(userDO.getUsername(), appInfoDOS.stream().map(AppInfoDO::getAppKey).collect(Collectors.toSet()));
    }

    @Override
    public void refreshUserCache(Set<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        redisRepository.batchDisableToken(userIds);
    }

    @Override
    public UserAuthorInfoDO queryUserAuthorInfo(Long userId, String appKey) {
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(appKey);
        if (Objects.isNull(appInfoDO)) {
            throw new BizException(RbacExceptionEnum.APP_NOT_EXISTS);
        }
        if (appInfoDO.getStatus() != CommonStatusEnum.ENABLE) {
            throw new BizException(RbacExceptionEnum.APP_DISABLE);
        }
        UserDO userDO = userRepository.selectById(userId);
        if (Objects.isNull(userDO)) {
            throw new BizException(RbacExceptionEnum.USER_NOT_EXISTS);
        }
        List<BackendFunctionDO> urls = backendFunctionRepository.queryAuthorizedFunctions(userDO.getUsername(), appKey);
        Set<String> authorizedFunctions = urls.stream()
                .map(item -> CommonUtil.buildAuthorizedUrl(item.getRequestMethod(), item.getRequestUrl()))
                .collect(Collectors.toSet());
        authorizedFunctions.addAll(AdminTemplateConstant.BASIC_AUTHORIZED_URLS);
        authorizedFunctions.addAll(AdminTemplateConstant.LOGIN_ACCOUNT_BASIC_ACCESS_URLS);
        return UserConverter.INSTANCE.buildUserAuthorInfo(userDO, authorizedFunctions, appKey);
    }

    private void cacheLoginUserAndToken(String token, Long userId, String appKey) {
        UserAuthorInfoDO loginInfo = queryUserAuthorInfo(userId, appKey);
        redisRepository.setToken(token, loginInfo);
    }
}

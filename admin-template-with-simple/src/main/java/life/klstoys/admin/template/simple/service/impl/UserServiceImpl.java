package life.klstoys.admin.template.simple.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.simple.converter.UserConverter;
import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.simple.dal.repository.RedisRepository;
import life.klstoys.admin.template.simple.dal.repository.UserRepository;
import life.klstoys.admin.template.simple.entity.UserInfoEntity;
import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import life.klstoys.admin.template.simple.exceptions.SimpleExceptionEnum;
import life.klstoys.admin.template.simple.service.UserService;
import life.klstoys.admin.template.simple.web.context.RequestContext;
import life.klstoys.admin.template.simple.web.controller.request.RegisterRequest;
import life.klstoys.admin.template.simple.web.controller.request.SendCaptchaRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserListRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdatePasswordRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdatePermissionRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private RedisRepository redisRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        UserDO userDO = userRepository.selectUserByUsername(request.getUsername());
        if (Objects.nonNull(userDO)) {
            throw new BizException(SimpleExceptionEnum.USER_IS_EXISTS);
        }
        userDO = userRepository.selectUserByEmail(request.getEmail());
        if (Objects.nonNull(userDO)) {
            throw new BizException(SimpleExceptionEnum.EMAIL_IS_EXISTS);
        }
        userDO = userRepository.selectUserByPhone(request.getPhone());
        if (Objects.nonNull(userDO)) {
            throw new BizException(SimpleExceptionEnum.PHONE_IS_EXISTS);
        }
        userRepository.insert(UserConverter.INSTANCE.convertRequestToDO(request));
    }

    @Override
    public String loginByPassword(String username, String password) {
        UserDO userDO = userRepository.selectUserByMultiFlag(username);
        if (Objects.isNull(userDO)) {
            throw new BizException(SimpleExceptionEnum.USER_NOT_EXISTS);
        }
        if (!CommonUtil.matchPassword(password, userDO.getPassword())) {
            throw new BizException(SimpleExceptionEnum.USER_INFO_NOT_MATCH);
        }
        String token = CommonUtil.randomUUID();
        redisRepository.setToken(token, userDO);
        return token;
    }

    @Override
    public String loginByCaptcha(String username, String captcha) {
        UserDO userDO = userRepository.selectUserByMultiFlag(username);
        if (Objects.isNull(userDO)) {
            throw new BizException(SimpleExceptionEnum.USER_NOT_EXISTS);
        }
        if (!redisRepository.checkCaptcha(captcha)) {
            throw new BizException(SimpleExceptionEnum.CAPTCHA_ERROR);
        }
        String token = CommonUtil.randomUUID();
        redisRepository.removeCaptcha(captcha);
        redisRepository.setToken(token, userDO);
        return token;
    }

    @Override
    public void sendCaptcha(SendCaptchaRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserInfoEntity baseInfo() {
        UserDO userInfo = RequestContext.getUserInfo();
        return UserConverter.INSTANCE.convertDOToEntity(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserUpdateRequest request) {
        UserDO userInfo = RequestContext.getUserInfo();
        CommonUtil.callSetterIfParamNotBlank(userInfo::setUsername, request.getUsername());
        CommonUtil.callSetterIfParamNotBlank(userInfo::setEmail, request.getEmail());
        CommonUtil.callSetterIfParamNotBlank(userInfo::setPhone, request.getPhone());
        userRepository.updateById(userInfo);
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
        UserDO userInfo = userRepository.selectUserById(id);
        return UserConverter.INSTANCE.convertDOToEntity(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        if (Objects.equals(id, RequestContext.getUserInfo().getId())) {
            throw new BizException(SimpleExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        UserDO userInfo = userRepository.selectUserById(id);
        if (Objects.isNull(userInfo)) {
            throw new BizException(SimpleExceptionEnum.USER_NOT_EXISTS);
        }
        userInfo.setStatus(CommonStatusEnum.DISABLE);
        userRepository.updateById(userInfo);
        redisRepository.removeTokenById(userInfo.getId());
    }

    @Override
    public void enable(Long id) {
        if (Objects.equals(id, RequestContext.getUserInfo().getId())) {
            throw new BizException(SimpleExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        UserDO userInfo = userRepository.selectUserById(id);
        if (Objects.isNull(userInfo)) {
            throw new BizException(SimpleExceptionEnum.USER_NOT_EXISTS);
        }
        userInfo.setStatus(CommonStatusEnum.ENABLE);
        userRepository.updateById(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(UserUpdatePermissionRequest request) {
        if (Objects.equals(request.getId(), RequestContext.getUserInfo().getId())) {
            throw new BizException(SimpleExceptionEnum.CANNOT_UPDATE_SELF_OPERATION);
        }
        UserDO userInfo = userRepository.selectUserById(request.getId());
        if (Objects.isNull(userInfo)) {
            throw new BizException(SimpleExceptionEnum.USER_NOT_EXISTS);
        }
        if (userInfo.getStatus() == CommonStatusEnum.DISABLE) {
            throw new BizException(SimpleExceptionEnum.USER_DISABLE);
        }
        // 不能操作自己同级别的权限的人
        Set<UserPermissionEnum> currentUserPermissions = RequestContext.getUserInfo().getPermission();
        int currentUserMaxWeight = currentUserPermissions.stream().mapToInt(UserPermissionEnum::getWeight).max().orElse(0);
        int userMaxWeight = userInfo.getPermission().stream().mapToInt(UserPermissionEnum::getWeight).max().orElse(0);
        if (currentUserMaxWeight <= userMaxWeight) {
            throw new BizException(SimpleExceptionEnum.UNSUPPORTED_PERMISSION_OPERATION);
        }
        // 授予权限时，不能授予超出自己权限范围的权限
        Set<UserPermissionEnum> permittedPermissions = currentUserPermissions.stream()
                .map(UserPermissionEnum::getSubPermissions)
                .flatMap(Collection::stream).collect(Collectors.toSet());
        if (!permittedPermissions.containsAll(request.getPermissions())) {
            throw new BizException(SimpleExceptionEnum.EXCEEDING_THE_SCOPE_OF_PERMISSIONS);
        }
        userInfo.setPermission(request.getPermissions());
        userRepository.updateById(userInfo);
        redisRepository.removeTokenById(userInfo.getId());
    }

    @Override
    public void logout() {
        redisRepository.removeToken(RequestContext.getToken());
    }

    @Override
    public void updatePassword(UserUpdatePasswordRequest request) {
        UserDO userDO = userRepository.selectUserById(RequestContext.getUserInfo().getId());
        if (!CommonUtil.matchPassword(request.getOriginPassword(), userDO.getPassword())) {
            throw new BizException(SimpleExceptionEnum.ORIGIN_PASSWORD_ERROR);
        }
    }
}

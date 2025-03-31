package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.converter.RoleConverter;
import life.klstoys.admin.template.rbac.converter.UserConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import life.klstoys.admin.template.rbac.dal.domain.RoleMenuDO;
import life.klstoys.admin.template.rbac.dal.domain.UserDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.dal.repository.FunctionGroupRepository;
import life.klstoys.admin.template.rbac.dal.repository.RoleMenuRepository;
import life.klstoys.admin.template.rbac.dal.repository.RoleRepository;
import life.klstoys.admin.template.rbac.dal.repository.UserRepository;
import life.klstoys.admin.template.rbac.dal.repository.UserRoleRepository;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.RoleService;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:19
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    @Setter(onMethod_ = @Autowired)
    private RoleRepository roleRepository;
    @Setter(onMethod_ = @Autowired)
    private UserRepository userRepository;
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    @Setter(onMethod_ = @Autowired)
    private UserRoleRepository userRoleRepository;
    @Setter(onMethod_ = @Autowired)
    private RoleMenuRepository roleMenuRepository;
    @Setter(onMethod_ = @Autowired)
    private FunctionGroupRepository functionGroupRepository;
    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserService userService;


    @Override
    public RoleInfoEntity getRoleById(Long id) {
        RoleDO roleDO = roleRepository.selectById(id);
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(roleDO.getAppKey());
        Set<String> roleMenuNos = roleMenuRepository.selectByRoleNo(roleDO.getNo());
        return RoleConverter.INSTANCE.convertDOToEntity(roleDO, appInfoDO, roleMenuNos);
    }

    @Override
    public PageResult<RoleInfoEntity> list(RoleListRequest request) {
        PageInfo<RoleDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<RoleDO> list = roleRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        Set<String> appKeys = pageInfo.getList().stream().map(RoleDO::getAppKey).collect(Collectors.toSet());
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByAppKeys(appKeys);
        Map<String, AppInfoDO> appInfoMap = appInfoDOS.stream().collect(Collectors.toMap(AppInfoDO::getAppKey, Function.identity()));
        return PageHelperUtil.convertToPage(pageInfo, roleDO -> RoleConverter.INSTANCE.convertDOToEntity(roleDO, appInfoMap.get(roleDO.getAppKey()), null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleSaveOrUpdateRequest request) {
        RoleDO roleDO = RoleConverter.INSTANCE.convertRequestToDO(request);
        roleRepository.insert(roleDO);
        if (CollectionUtils.isNotEmpty(request.getMenuNos())) {
            bindMenus(roleDO, request.getMenuNos());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleSaveOrUpdateRequest request) {
        RoleDO roleDO = roleRepository.selectById(request.getId());
        if (Objects.isNull(roleDO)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        CommonUtil.callSetterIfParamNotBlank(roleDO::setName, request.getName());
        CommonUtil.callSetterIfParamNotBlank(roleDO::setNameZh, request.getNameZh());
        CommonUtil.callSetterIfParamNotBlank(roleDO::setDescription, request.getDescription());
        roleRepository.updateById(roleDO);
        if (CollectionUtils.isNotEmpty(request.getMenuNos())) {
            bindMenus(roleDO, request.getMenuNos());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        RoleDO roleDO = roleRepository.selectById(id);
        if (Objects.isNull(roleDO)) {
            return;
        }
        roleRepository.deleteById(id);
        userRoleRepository.deleteByRole(roleDO.getNo());
        refreshUserCache(roleDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        RoleDO roleDO = roleRepository.selectById(id);
        if (Objects.isNull(roleDO)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        roleDO.setStatus(CommonStatusEnum.DISABLE);
        roleRepository.updateById(roleDO);
        refreshUserCache(roleDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        RoleDO roleDO = roleRepository.selectById(id);
        if (Objects.isNull(roleDO)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        roleDO.setStatus(CommonStatusEnum.ENABLE);
        roleRepository.updateById(roleDO);
        refreshUserCache(roleDO.getNo());
    }

    @Override
    public Boolean existUser(Long id) {
        RoleDO roleDO = roleRepository.selectById(id);
        if (Objects.isNull(roleDO)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        if (roleDO.getStatus() != CommonStatusEnum.ENABLE) {
            throw new BizException(RbacExceptionEnum.ROLE_DISABLED);
        }
        return userRoleRepository.countEnableByRole(roleDO.getNo()) > 0;
    }

    @Override
    public PageResult<UserInfoEntity> queryRoleUsers(Long roleId, PageRequest request) {
        RoleDO roleDO = roleRepository.selectById(roleId);
        if (Objects.isNull(roleDO)) {
            throw new BizException(RbacExceptionEnum.ROLE_NOT_EXISTS);
        }
        PageInfo<UserDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<UserDO> list = userRepository.queryByRoleNo(roleDO.getNo());
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        return PageHelperUtil.convertToPage(pageInfo, UserConverter.INSTANCE::convertDOToEntity);
    }


    private void bindMenus(RoleDO roleDO, Set<String> functionGroupNos) {
        List<FunctionGroupDO> functionGroupDOS = functionGroupRepository.selectByAppKeyAndNos(roleDO.getAppKey(), functionGroupNos);
        if (CollectionUtils.isEmpty(functionGroupDOS)) {
            throw new BizException(RbacExceptionEnum.MENU_MAP_INVALID);
        }
        List<RoleMenuDO> list = functionGroupDOS.stream()
                .filter(item -> Objects.equals(item.getAppKey(), roleDO.getAppKey()))
                .map(item -> RoleConverter.INSTANCE.buildRoleMenu(roleDO.getNo(), item)).toList();
        roleMenuRepository.deleteByRoleNo(roleDO.getNo());
        roleMenuRepository.batchInsert(list);
        refreshUserCache(roleDO.getNo());
    }

    private void refreshUserCache(String roleNo) {
        List<UserDO> list = userRepository.queryByRoleNo(roleNo);
        Set<Long> userIds = list.stream().map(UserDO::getId).collect(Collectors.toSet());
        userService.refreshUserCache(userIds);
    }
}

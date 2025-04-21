package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.exception.ExceptionEnum;
import life.klstoys.admin.template.rbac.constant.AdminTemplateConstant;
import life.klstoys.admin.template.rbac.converter.FunctionGroupConverter;
import life.klstoys.admin.template.rbac.converter.MenuConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.dal.repository.FrontendPageRepository;
import life.klstoys.admin.template.rbac.dal.repository.FunctionGroupRepository;
import life.klstoys.admin.template.rbac.dal.repository.RoleMenuRepository;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAppKeyDO;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorMenuDO;
import life.klstoys.admin.template.rbac.entity.EndPointMenuInfoEntity;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.entity.MenuInfoEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.MenuService;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
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
 * @since 2024/12/11 17:55
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {
    @Setter(onMethod_ = @Autowired)
    private FrontendPageRepository frontendPageRepository;
    @Setter(onMethod_ = @Autowired)
    private FunctionGroupRepository functionGroupRepository;
    @Setter(onMethod_ = @Autowired)
    private RoleMenuRepository roleMenuRepository;
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    @Setter(onMethod_ = {@Autowired, @Qualifier("commonExecutorService")})
    private ThreadPoolTaskExecutor commonExecutorService;
    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserService userService;

    @Override
    public List<EndPointMenuInfoEntity> queryUserMenus(String username, String appKey) {
        List<UserAuthorMenuDO> authorMenuDOS = roleMenuRepository.selectByUserIdAndAppKey(username, appKey);
        Map<String, List<UserAuthorMenuDO>> menuMap = authorMenuDOS.stream().collect(Collectors.groupingBy(UserAuthorMenuDO::getMenuComponentKey));
        return menuMap.values().stream().map(this::buildEndPointMenuInfoEntityByMenus).filter(Objects::nonNull).toList();
    }

    @Override
    public MenuInfoEntity getMenuById(Long id) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(id);
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(frontendPageDO.getAppKey());
        List<FunctionGroupDO> functionGroupDOList = functionGroupRepository.selectByPageNo(frontendPageDO.getNo());
        List<FunctionGroupEntity> functionGroupEntities = functionGroupDOList.stream()
                .map(item -> FunctionGroupConverter.INSTANCE.buildEntity(item, appInfoDO, frontendPageDO, null))
                .toList();
        return MenuConverter.INSTANCE.convertDOToEntity(frontendPageDO, appInfoDO, functionGroupEntities);
    }

    @Override
    public PageResult<MenuInfoEntity> list(MenuListRequest request) {
        PageInfo<FrontendPageDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<FrontendPageDO> list = frontendPageRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        Set<String> appKeys = pageInfo.getList().stream().map(FrontendPageDO::getAppKey).collect(Collectors.toSet());
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByAppKeys(appKeys);
        Map<String, AppInfoDO> appInfoDOMap = appInfoDOS.stream().collect(Collectors.toMap(AppInfoDO::getAppKey, Function.identity()));
        return PageHelperUtil.convertToPage(pageInfo, item -> MenuConverter.INSTANCE.convertDOToEntity(item, appInfoDOMap.get(item.getAppKey()), null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(MenuSaveOrUpdateRequest request) {
        FrontendPageDO frontendPageDO = MenuConverter.INSTANCE.convertRequestToDO(request);
        frontendPageRepository.insert(frontendPageDO);
        // 如果是静态页面。就添加相应的访问功能组
        if (request.isStaticPage()) {
            FunctionGroupDO functionGroupDO = FunctionGroupConverter.INSTANCE.buildStaticFunctionGroup(frontendPageDO);
            functionGroupRepository.insert(functionGroupDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuSaveOrUpdateRequest request) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(request.getId());
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        if (StringUtils.isNotBlank(request.getParentNo())) {
            FrontendPageDO parent = frontendPageRepository.selectByNo(request.getParentNo());
            if (Objects.isNull(parent)) {
                throw new BizException(RbacExceptionEnum.PARENT_MENU_NOT_EXIST);
            }
        }
        if (request.isStaticPage() != frontendPageDO.isStaticPage()) {
            functionGroupRepository.deleteByFrontendPageNo(frontendPageDO.getNo());
            // 如果菜单由动态页面改为静态页面，添加静态页面功能组
            if (request.isStaticPage()) {
                FunctionGroupDO functionGroupDO = FunctionGroupConverter.INSTANCE.buildStaticFunctionGroup(frontendPageDO);
                functionGroupRepository.insert(functionGroupDO);
            }
            // 如果菜单由静态页面改为动态页面，不做后续操作，功能组需要重新绑定
        }
        CommonUtil.callSetterIfParamNotBlank(frontendPageDO::setTitle, request.getTitle());
        CommonUtil.callSetterIfParamNotBlank(frontendPageDO::setComponentKey, request.getComponentKey());
        CommonUtil.callSetterIfParamNotBlank(frontendPageDO::setParentNo, request.getParentNo());
        CommonUtil.callSetterIfParamNotNull(frontendPageDO::setStatus, request.getStatus());
        frontendPageRepository.updateById(frontendPageDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(id);
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        frontendPageRepository.deleteById(id);
        functionGroupRepository.deleteByFrontendPageNo(frontendPageDO.getNo());
        refreshUserCache(frontendPageDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(id);
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        frontendPageDO.setStatus(CommonStatusEnum.DISABLE);
        frontendPageRepository.updateById(frontendPageDO);
        refreshUserCache(frontendPageDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(id);
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        frontendPageDO.setStatus(CommonStatusEnum.ENABLE);
        frontendPageRepository.updateById(frontendPageDO);
        refreshUserCache(frontendPageDO.getNo());
    }

    @Override
    public List<MenuInfoEntity> getMenuByAppKey(String appKey) {
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(appKey);
        if (appInfoDO == null) {
            throw new BizException(RbacExceptionEnum.APP_NOT_EXISTS);
        }
        Future<List<FrontendPageDO>> uiMenuDOSFuture = commonExecutorService.submit(() -> frontendPageRepository.selectByAppKey(appKey));
        Future<List<FunctionGroupDO>> functionGroupDOSFuture = commonExecutorService.submit(() -> functionGroupRepository.selectByAppKey(appKey));

        try {
            List<FrontendPageDO> frontendPageDOS = uiMenuDOSFuture.get(AdminTemplateConstant.ASYNC_TASK_TIMEOUT, TimeUnit.SECONDS);
            if (CollectionUtils.isEmpty(frontendPageDOS)) {
                return List.of();
            }
            List<FunctionGroupDO> functionGroupDOS = functionGroupDOSFuture.get(AdminTemplateConstant.ASYNC_TASK_TIMEOUT, TimeUnit.SECONDS);
            Map<String, List<FunctionGroupEntity>> functionMap = functionGroupDOS.stream()
                    .map(item -> FunctionGroupConverter.INSTANCE.buildEntity(item, appInfoDO, null, null))
                    .filter(this::isNotFrontEndPageFlagFunction)
                    .collect(Collectors.groupingBy(FunctionGroupEntity::getFrontendPageNo));
            List<MenuInfoEntity> menuInfoEntities = frontendPageDOS.stream()
                    .filter(item -> !Objects.equals(item.getNo(), AdminTemplateConstant.LOGIN_USER_BASIC_AUTH))
                    .map(item -> MenuConverter.INSTANCE.convertDOToEntity(item, appInfoDO, functionMap.get(item.getNo())))
                    .toList();
            return CommonUtil.collectionToTree(menuInfoEntities, MenuInfoEntity::getNo, MenuInfoEntity::getParentNo, MenuInfoEntity::getChildrenMenus, MenuInfoEntity::setChildrenMenus);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("查询失败：appKey = {}", appKey, e);
            throw new BizException(ExceptionEnum.SYSTEM_ERROR, e);
        }
    }

    @Override
    public Boolean menuIsLeafNode(Long id) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectById(id);
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        List<FrontendPageDO> children = frontendPageRepository.list(MenuListRequest.builder().parentNo(frontendPageDO.getNo()).build());
        return CollectionUtils.isEmpty(children);
    }

    private void refreshUserCache(String menuNo) {
        Set<UserAppKeyDO> userAppKeyDOS = frontendPageRepository.selectUserIdsByMenuNo(menuNo);
        userService.refreshUserCache(userAppKeyDOS);
    }

    private boolean isNotFrontEndPageFlagFunction(FunctionGroupEntity functionGroupEntity) {
        return !Objects.equals(functionGroupEntity.getTitle(), AdminTemplateConstant.PAGE_ACCESS_AUTH_GROUP_NAME);
    }

    private EndPointMenuInfoEntity buildEndPointMenuInfoEntityByMenus(List<UserAuthorMenuDO> menuDOS) {
        if (CollectionUtils.isEmpty(menuDOS)) {
            return null;
        }
        EndPointMenuInfoEntity endPointMenuInfoEntity = new EndPointMenuInfoEntity();
        endPointMenuInfoEntity.setKey(menuDOS.get(0).getMenuComponentKey());
        endPointMenuInfoEntity.setTitle(menuDOS.get(0).getMenuTitle());
        endPointMenuInfoEntity.setInnerComponentKeys(menuDOS.stream()
                .filter(item -> !Objects.equals(item.getMenuTitle(), AdminTemplateConstant.PAGE_ACCESS_AUTH_GROUP_NAME))
                .map(UserAuthorMenuDO::getFunctionGroupNo)
                .collect(Collectors.toSet()));
        return endPointMenuInfoEntity;
    }
}

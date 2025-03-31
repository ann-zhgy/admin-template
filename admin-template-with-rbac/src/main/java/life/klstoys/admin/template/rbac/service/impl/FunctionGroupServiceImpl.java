package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.converter.FunctionGroupConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupMapDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.dal.repository.BackendFunctionRepository;
import life.klstoys.admin.template.rbac.dal.repository.FrontendPageRepository;
import life.klstoys.admin.template.rbac.dal.repository.FunctionGroupMapRepository;
import life.klstoys.admin.template.rbac.dal.repository.FunctionGroupRepository;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.FunctionGroupService;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import lombok.Setter;
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
 * @since 2025/1/17 13:04
 */
@Service
public class FunctionGroupServiceImpl implements FunctionGroupService {
    @Setter(onMethod_ = @Autowired)
    private FunctionGroupRepository functionGroupRepository;
    @Setter(onMethod_ = @Autowired)
    private FrontendPageRepository frontendPageRepository;
    @Setter(onMethod_ = @Autowired)
    private FunctionGroupMapRepository functionGroupMapRepository;
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    @Setter(onMethod_ = @Autowired)
    private BackendFunctionRepository functionRepository;
    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserService userService;

    @Override
    public FunctionGroupEntity getFunctionGroupById(Long id) {
        FunctionGroupDO functionGroupDO = functionGroupRepository.selectById(id);
        if (Objects.isNull(functionGroupDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_GROUP_NOT_EXIST);
        }
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(functionGroupDO.getAppKey());
        List<BackendFunctionDO> functionDOS = functionRepository.selectByGroupNo(functionGroupDO.getNo());
        return FunctionGroupConverter.INSTANCE.buildEntity(functionGroupDO, appInfoDO, functionDOS);
    }

    @Override
    public PageResult<FunctionGroupEntity> list(FunctionGroupListRequest request) {
        PageInfo<FunctionGroupDO> pageInfo;
        try (Page<?> ignored = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<FunctionGroupDO> list = functionGroupRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        Set<String> appKeys = pageInfo.getList().stream().map(FunctionGroupDO::getAppKey).collect(Collectors.toSet());
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByAppKeys(appKeys);
        Map<String, AppInfoDO> appInfoDOMap = appInfoDOS.stream().collect(Collectors.toMap(AppInfoDO::getAppKey, Function.identity()));
        return PageHelperUtil.convertToPage(pageInfo, item -> FunctionGroupConverter.INSTANCE.buildEntity(item, appInfoDOMap.get(item.getAppKey()), null));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(FunctionGroupSaveOrUpdateRequest request) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectByNo(request.getFrontendPageNo());
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        FunctionGroupDO functionGroupDO = FunctionGroupConverter.INSTANCE.convertRequestToDO(request);
        functionGroupRepository.insert(functionGroupDO);
        if (CollectionUtils.isEmpty(request.getFunctionNos())) {
            return;
        }
        List<BackendFunctionDO> functionDOS = functionRepository.selectByAppKeyAndNos(functionGroupDO.getAppKey(), request.getFunctionNos());
        if (CollectionUtils.isEmpty(functionDOS)) {
            return;
        }
        if (functionDOS.size() != request.getFunctionNos().size()) {
            throw new BizException(RbacExceptionEnum.FUNCTION_GROUP_UPDATE_APPKEY_NOT_MATCH);
        }
        List<FunctionGroupMapDO> list = functionDOS.stream()
                .map(item -> FunctionGroupConverter.INSTANCE.buildFunctionGroupMap(functionGroupDO.getNo(), item))
                .toList();
        functionGroupMapRepository.batchInsert(list);
        refreshUserCache(functionGroupDO.getFrontendPageNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FunctionGroupSaveOrUpdateRequest request) {
        FrontendPageDO frontendPageDO = frontendPageRepository.selectByNo(request.getFrontendPageNo());
        if (Objects.isNull(frontendPageDO)) {
            throw new BizException(RbacExceptionEnum.MENU_NOT_EXIST);
        }
        FunctionGroupDO functionGroupDO = functionGroupRepository.selectById(request.getId());
        if (Objects.isNull(functionGroupDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_GROUP_NOT_EXIST);
        }
        CommonUtil.callSetterIfParamNotBlank(functionGroupDO::setTitle, request.getTitle());
        CommonUtil.callSetterIfParamNotBlank(functionGroupDO::setAppKey, request.getAppKey());
        CommonUtil.callSetterIfParamNotNull(functionGroupDO::setGroupCallType, request.getGroupCallType());
        functionGroupRepository.updateById(functionGroupDO);
        Set<String> existFunctionNos = functionGroupMapRepository.selectFunctionNosByGroupNo(functionGroupDO.getNo());
        if (CollectionUtils.isEqualCollection(existFunctionNos, request.getFunctionNos())) {
            return;
        }
        functionGroupMapRepository.deleteByGroupNo(functionGroupDO.getNo());
        List<BackendFunctionDO> functionDOS = functionRepository.selectByAppKeyAndNos(functionGroupDO.getAppKey(), request.getFunctionNos());
        if (CollectionUtils.isEmpty(functionDOS)) {
            return;
        }
        if (functionDOS.size() != request.getFunctionNos().size()) {
            throw new BizException(RbacExceptionEnum.FUNCTION_GROUP_UPDATE_APPKEY_NOT_MATCH);
        }
        List<FunctionGroupMapDO> list = functionDOS.stream()
                .map(item -> FunctionGroupConverter.INSTANCE.buildFunctionGroupMap(functionGroupDO.getNo(), item))
                .toList();
        functionGroupMapRepository.batchInsert(list);
        refreshUserCache(functionGroupDO.getFrontendPageNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        FunctionGroupDO functionGroupDO = functionGroupRepository.selectById(id);
        if (Objects.isNull(functionGroupDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_GROUP_NOT_EXIST);
        }
        functionGroupRepository.deleteById(functionGroupDO.getId());
        functionGroupMapRepository.deleteByGroupNo(functionGroupDO.getNo());
        refreshUserCache(functionGroupDO.getFrontendPageNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        FunctionGroupDO functionGroupDO = functionGroupRepository.selectById(id);
        if (Objects.isNull(functionGroupDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        functionGroupDO.setStatus(CommonStatusEnum.DISABLE);
        functionGroupRepository.updateById(functionGroupDO);
        refreshUserCache(functionGroupDO.getFrontendPageNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        FunctionGroupDO functionGroupDO = functionGroupRepository.selectById(id);
        if (Objects.isNull(functionGroupDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        functionGroupDO.setStatus(CommonStatusEnum.ENABLE);
        functionGroupRepository.updateById(functionGroupDO);
        refreshUserCache(functionGroupDO.getFrontendPageNo());
    }

    private void refreshUserCache(String menuNo) {
        Set<Long> userIds = frontendPageRepository.selectUserIdsByMenuNo(menuNo);
        userService.refreshUserCache(userIds);
    }
}

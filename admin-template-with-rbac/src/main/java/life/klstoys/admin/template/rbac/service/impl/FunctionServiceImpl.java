package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.converter.FunctionConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.dal.repository.BackendFunctionRepository;
import life.klstoys.admin.template.rbac.entity.FunctionInfoEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.FunctionService;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionSaveOrUpdateRequest;
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
 * @since 2024/12/18 15:26
 */
@Slf4j
@Service
public class FunctionServiceImpl implements FunctionService {
    @Setter(onMethod_ = @Autowired)
    private BackendFunctionRepository backendFunctionRepository;
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserService userService;

    @Override
    public FunctionInfoEntity getFunctionById(Long id) {
        BackendFunctionDO backendFunctionDO = backendFunctionRepository.selectById(id);
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(backendFunctionDO.getAppKey());
        return FunctionConverter.INSTANCE.convertDOToEntity(backendFunctionDO, appInfoDO);
    }

    @Override
    public PageResult<FunctionInfoEntity> list(FunctionListRequest request) {
        PageInfo<BackendFunctionDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<BackendFunctionDO> list = backendFunctionRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        Set<String> appKeys = pageInfo.getList().stream().map(BackendFunctionDO::getAppKey).collect(Collectors.toSet());
        List<AppInfoDO> appInfoDOList = appInfoRepository.selectByAppKeys(appKeys);
        Map<String, AppInfoDO> appInfoMap = appInfoDOList.stream().collect(Collectors.toMap(AppInfoDO::getAppKey, Function.identity()));
        return PageHelperUtil.convertToPage(pageInfo, item -> FunctionConverter.INSTANCE.convertDOToEntity(item, appInfoMap.get(item.getAppKey())));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(FunctionSaveOrUpdateRequest request) {
        BackendFunctionDO backendFunctionDO = FunctionConverter.INSTANCE.convertRequestToDO(request);
        backendFunctionRepository.insert(backendFunctionDO);
        refreshUserCache(backendFunctionDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(FunctionSaveOrUpdateRequest request) {
        BackendFunctionDO backendFunctionDO = backendFunctionRepository.selectById(request.getId());
        if (Objects.isNull(backendFunctionDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        CommonUtil.callSetterIfParamNotBlank(backendFunctionDO::setTitle, request.getTitle());
        CommonUtil.callSetterIfParamNotBlank(backendFunctionDO::setRequestUrl, request.getRequestUrl());
        CommonUtil.callSetterIfParamNotNull(backendFunctionDO::setRequestMethod, request.getRequestMethod());
        backendFunctionRepository.updateById(backendFunctionDO);
        refreshUserCache(backendFunctionDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        BackendFunctionDO backendFunctionDO = backendFunctionRepository.selectById(id);
        if (Objects.isNull(backendFunctionDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        backendFunctionRepository.deleteById(id);
        if (backendFunctionDO.getStatus() == CommonStatusEnum.ENABLE) {
            refreshUserCache(backendFunctionDO.getNo());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Long id) {
        BackendFunctionDO backendFunctionDO = backendFunctionRepository.selectById(id);
        if (Objects.isNull(backendFunctionDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        backendFunctionDO.setStatus(CommonStatusEnum.DISABLE);
        backendFunctionRepository.updateById(backendFunctionDO);
        refreshUserCache(backendFunctionDO.getNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        BackendFunctionDO backendFunctionDO = backendFunctionRepository.selectById(id);
        if (Objects.isNull(backendFunctionDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        backendFunctionDO.setStatus(CommonStatusEnum.ENABLE);
        backendFunctionRepository.updateById(backendFunctionDO);
        refreshUserCache(backendFunctionDO.getNo());
    }

    private void refreshUserCache(String functionNo) {
        Set<Long> userIds = backendFunctionRepository.selectUserIdsByFunctionNo(functionNo);
        userService.refreshUserCache(userIds);
    }
}

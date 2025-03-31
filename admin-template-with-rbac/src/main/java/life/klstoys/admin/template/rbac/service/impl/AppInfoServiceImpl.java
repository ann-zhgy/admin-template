package life.klstoys.admin.template.rbac.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.converter.AppInfoConverter;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.entity.AppInfoEntity;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.service.AppInfoService;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListByUserRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.PageHelperUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:16
 */
@Slf4j
@Service
public class AppInfoServiceImpl implements AppInfoService {
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;

    @Override
    public AppInfoEntity getAppInfoById(Long id) {
        AppInfoDO appInfoDO = appInfoRepository.selectById(id);
        return AppInfoConverter.INSTANCE.convertDOToEntity(appInfoDO);
    }

    @Override
    public PageResult<AppInfoEntity> list(AppInfoListRequest request) {
        PageInfo<AppInfoDO> pageInfo;
        try (Page<?> ignore = PageHelper.startPage(request.getPageNo(), request.getPageSize())) {
            List<AppInfoDO> list = appInfoRepository.list(request);
            if (CollectionUtils.isEmpty(list)) {
                return PageResult.empty(request.getPageSize());
            }
            pageInfo = PageInfo.of(list);
        }
        return PageHelperUtil.convertToPage(pageInfo, AppInfoConverter.INSTANCE::convertDOToEntity);
    }

    @Override
    public void save(AppInfoSaveOrUpdateRequest request) {
        AppInfoDO appInfoDO = AppInfoConverter.INSTANCE.convertRequestToDO(request);
        appInfoRepository.insert(appInfoDO);
    }

    @Override
    public void update(AppInfoSaveOrUpdateRequest request) {
        AppInfoDO appInfoDO = appInfoRepository.selectById(request.getId());
        if (Objects.isNull(appInfoDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        CommonUtil.callSetterIfParamNotBlank(appInfoDO::setAppName, request.getAppName());
        CommonUtil.callSetterIfParamNotBlank(appInfoDO::setDescription, request.getDescription());
        CommonUtil.callSetterIfParamNotNull(appInfoDO::setAccessControlBy, request.getAccessControlBy());
        CommonUtil.callSetterIfParamNotNull(appInfoDO::setGrantAccessPermissionBy, request.getGrantAccessPermissionBy());
        CommonUtil.callSetterIfParamNotEmpty(appInfoDO::setAppType, request.getAppType());
        appInfoRepository.updateById(appInfoDO);
    }

    @Override
    public void deleteById(Long id) {
        appInfoRepository.deleteById(id);
    }

    @Override
    public void disable(Long id) {
        AppInfoDO appInfoDO = appInfoRepository.selectById(id);
        if (Objects.isNull(appInfoDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        appInfoDO.setStatus(CommonStatusEnum.DISABLE);
        appInfoRepository.updateById(appInfoDO);
    }

    @Override
    public void enable(Long id) {
        AppInfoDO appInfoDO = appInfoRepository.selectById(id);
        if (Objects.isNull(appInfoDO)) {
            throw new BizException(RbacExceptionEnum.FUNCTION_NOT_EXIST);
        }
        appInfoDO.setStatus(CommonStatusEnum.ENABLE);
        appInfoRepository.updateById(appInfoDO);
    }

    @Override
    public List<AppInfoEntity> listByUser(AppInfoListByUserRequest request) {
        List<AppInfoDO> appInfoDOS = appInfoRepository.selectByUsername(request.getUsername());
        return appInfoDOS.stream().map(AppInfoConverter.INSTANCE::convertDOToEntity).toList();
    }
}

package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.entity.FunctionInfoEntity;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 15:18
 */
@Mapper(imports = {RequestContext.class, CommonUtil.class, CommonStatusEnum.class}, uses = {AppInfoConverter.class})
public interface FunctionConverter {
    FunctionConverter INSTANCE = Mappers.getMapper(FunctionConverter.class);

    @Mappings({
            @Mapping(target = "id", source = "backendFunctionDO.id"),
            @Mapping(target = "no", source = "backendFunctionDO.no"),
            @Mapping(target = "title", source = "backendFunctionDO.title"),
            @Mapping(target = "requestMethod", source = "backendFunctionDO.requestMethod"),
            @Mapping(target = "requestUrl", source = "backendFunctionDO.requestUrl"),
            @Mapping(target = "appKey", source = "backendFunctionDO.appKey"),
            @Mapping(target = "appInfo", expression = "java(AppInfoConverter.INSTANCE.convertDOToEntity(appInfoDO))"),
            @Mapping(target = "status", source = "backendFunctionDO.status"),
            @Mapping(target = "creator", source = "backendFunctionDO.creator"),
            @Mapping(target = "createTime", source = "backendFunctionDO.createTime"),
            @Mapping(target = "updater", source = "backendFunctionDO.updater"),
            @Mapping(target = "updateTime", source = "backendFunctionDO.updateTime"),
    })
    FunctionInfoEntity convertDOToEntity(BackendFunctionDO backendFunctionDO, AppInfoDO appInfoDO);

    @Mappings({
            @Mapping(target = "no", expression = "java(CommonUtil.generateNo())"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.DISABLE)"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    BackendFunctionDO convertRequestToDO(FunctionSaveOrUpdateRequest request);
}

package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.constant.AdminTemplateConstant;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupMapDO;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.StreamUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 13:13
 */
@Mapper(imports = {Optional.class, AppInfoConverter.class, StreamUtil.class, CommonStatusEnum.class, CommonUtil.class, RequestContext.class, GroupCallTypeEnum.class, AdminTemplateConstant.class})
public interface FunctionGroupConverter {
    FunctionGroupConverter INSTANCE = Mappers.getMapper(FunctionGroupConverter.class);

    @Mappings({
            @Mapping(target = "id", source = "functionGroupDO.id"),
            @Mapping(target = "no", source = "functionGroupDO.no"),
            @Mapping(target = "title", source = "functionGroupDO.title"),
            @Mapping(target = "appKey", source = "functionGroupDO.appKey"),
            @Mapping(target = "appInfo", expression = "java(AppInfoConverter.INSTANCE.convertDOToEntity(appInfoDO))"),
            @Mapping(target = "functionInfos", expression = "java(StreamUtil.ofNullable(functionDOS).map(item -> FunctionConverter.INSTANCE.convertDOToEntity(item, null)).toList())"),
            @Mapping(target = "status", source = "functionGroupDO.status"),
            @Mapping(target = "groupCallType", source = "functionGroupDO.groupCallType"),
            @Mapping(target = "creator", source = "functionGroupDO.creator"),
            @Mapping(target = "createTime", source = "functionGroupDO.createTime"),
            @Mapping(target = "updater", source = "functionGroupDO.updater"),
            @Mapping(target = "updateTime", source = "functionGroupDO.updateTime"),
    })
    FunctionGroupEntity buildEntity(FunctionGroupDO functionGroupDO, AppInfoDO appInfoDO, List<BackendFunctionDO> functionDOS);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "no", expression = "java(CommonUtil.generateNo())"),
            @Mapping(target = "title", expression = "java(AdminTemplateConstant.PAGE_ACCESS_AUTH_GROUP_NAME)"),
            @Mapping(target = "appKey", source = "frontendPageDO.appKey"),
            @Mapping(target = "frontendPageNo", source = "frontendPageDO.no"),
            @Mapping(target = "groupCallType", expression = "java(GroupCallTypeEnum.NONE)"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.ENABLE)"),
    })
    FunctionGroupDO buildNoneDO(FrontendPageDO frontendPageDO);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "no", expression = "java(CommonUtil.generateNo())"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.ENABLE)"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    FunctionGroupDO convertRequestToDO(FunctionGroupSaveOrUpdateRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "backendFunctionNo", source = "item.no"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    FunctionGroupMapDO buildFunctionGroupMap(String groupNo, BackendFunctionDO item);
}

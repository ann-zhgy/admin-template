package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import life.klstoys.admin.template.rbac.dal.domain.RoleMenuDO;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:28
 */
@Mapper(imports = {Set.class, CommonStatusEnum.class, CommonUtil.class, RequestContext.class, AppInfoConverter.class})
public interface RoleConverter {
    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    @Mappings({
            @Mapping(target = "id", source = "roleDO.id"),
            @Mapping(target = "description", source = "roleDO.description"),
            @Mapping(target = "appKey", source = "roleDO.appKey"),
            @Mapping(target = "status", source = "roleDO.status"),
            @Mapping(target = "creator", source = "roleDO.creator"),
            @Mapping(target = "createTime", source = "roleDO.createTime"),
            @Mapping(target = "updater", source = "roleDO.updater"),
            @Mapping(target = "updateTime", source = "roleDO.updateTime"),
            @Mapping(target = "appInfo", expression = "java(AppInfoConverter.INSTANCE.convertDOToEntity(appInfoDO))"),
    })
    RoleInfoEntity convertDOToEntity(RoleDO roleDO, AppInfoDO appInfoDO, Set<String> menuNos);

    @Mappings({
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "no", expression = "java(CommonUtil.generateNo())"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.ENABLE)")
    })
    RoleDO convertRequestToDO(RoleSaveOrUpdateRequest request);

    @Mappings({
            @Mapping(target = "groupNo", source = "functionGroupDO.no"),
            @Mapping(target = "frontendPageNo", source = "functionGroupDO.frontendPageNo"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
    })
    RoleMenuDO buildRoleMenu(String roleNo, FunctionGroupDO functionGroupDO);
}

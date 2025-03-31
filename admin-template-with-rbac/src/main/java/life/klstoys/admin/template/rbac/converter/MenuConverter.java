package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.entity.FunctionGroupEntity;
import life.klstoys.admin.template.rbac.entity.MenuInfoEntity;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 14:00
 */
@Mapper(imports = {RequestContext.class, CommonUtil.class, CommonStatusEnum.class}, uses = {AppInfoConverter.class})
public interface MenuConverter {
    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

    @Mappings({
            @Mapping(target = "id", source = "frontendPageDO.id"),
            @Mapping(target = "no", source = "frontendPageDO.no"),
            @Mapping(target = "title", source = "frontendPageDO.title"),
            @Mapping(target = "componentKey", source = "frontendPageDO.componentKey"),
            @Mapping(target = "parentNo", source = "frontendPageDO.parentNo"),
            @Mapping(target = "appKey", source = "frontendPageDO.appKey"),
            @Mapping(target = "appInfo", expression = "java(AppInfoConverter.INSTANCE.convertDOToEntity(appInfoDO))"),
            @Mapping(target = "status", source = "frontendPageDO.status"),
            @Mapping(target = "childrenMenus", ignore = true),
            @Mapping(target = "functionGroupInfos", source = "functionGroupInfos"),
            @Mapping(target = "creator", source = "frontendPageDO.creator"),
            @Mapping(target = "createTime", source = "frontendPageDO.createTime"),
            @Mapping(target = "updater", source = "frontendPageDO.updater"),
            @Mapping(target = "updateTime", source = "frontendPageDO.updateTime"),
    })
    MenuInfoEntity convertDOToEntity(FrontendPageDO frontendPageDO, AppInfoDO appInfoDO, List<FunctionGroupEntity> functionGroupInfos);

    @Mappings({
            @Mapping(target = "no", expression = "java(CommonUtil.generateNo())"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.DISABLE)"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true)
    })
    FrontendPageDO convertRequestToDO(MenuSaveOrUpdateRequest request);
}

package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.entity.AppInfoEntity;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoSaveOrUpdateRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Collections;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:21
 */
@Mapper(imports = {RequestContext.class, CommonUtil.class, CommonStatusEnum.class, Collections.class})
public interface AppInfoConverter {
    AppInfoConverter INSTANCE = Mappers.getMapper(AppInfoConverter.class);

    AppInfoEntity convertDOToEntity(AppInfoDO appInfoDO);

    @Mappings({
            @Mapping(target = "appType", source = "appType", defaultExpression = "java(Collections.emptySet())"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.DISABLE)"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "id", ignore = true)
    })
    AppInfoDO convertRequestToDO(AppInfoSaveOrUpdateRequest request);
}

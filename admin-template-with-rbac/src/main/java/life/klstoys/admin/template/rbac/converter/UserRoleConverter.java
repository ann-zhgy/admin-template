package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import life.klstoys.admin.template.rbac.dal.domain.UserRoleDO;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 14:08
 */
@Mapper(imports = {RequestContext.class})
public interface UserRoleConverter {
    UserRoleConverter INSTANCE = Mappers.getMapper(UserRoleConverter.class);

    @Mappings({
            @Mapping(target = "roleNo", source = "roleDO.no"),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())")
    })
    UserRoleDO buildDOWithUserAndRole(String username, RoleDO roleDO);
}

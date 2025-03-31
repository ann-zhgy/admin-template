package life.klstoys.admin.template.rbac.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.domain.UserAppDO;
import life.klstoys.admin.template.rbac.dal.domain.UserDO;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
import life.klstoys.admin.template.rbac.entity.EndPointMenuInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.rbac.web.controller.request.author.RegisterRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 14:46
 */
@Mapper(imports = {Set.class, CommonStatusEnum.class, CommonUtil.class, RequestContext.class})
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.ENABLE)"),
            @Mapping(target = "password", expression = "java(CommonUtil.encodePassword(request.getPassword()))"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "nickname", source = "username")
    })
    UserDO convertRequestToDO(RegisterRequest request);

    @Mappings({
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "status", ignore = true)
    })
    UserInfoEntity convertAuthorInfoDOToEntity(UserAuthorInfoDO userInfo, List<EndPointMenuInfoEntity> menus);

    @Mappings({
            @Mapping(target = "menus", ignore = true)
    })
    UserInfoEntity convertDOToEntity(UserDO userDO);

    UserAuthorInfoDO buildUserAuthorInfo(UserDO userDO, Set<String> authorizedUrls, String appKey);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "creator", expression = "java(RequestContext.getUserInfo().getUsername())"),
            @Mapping(target = "updater", expression = "java(RequestContext.getUserInfo().getUsername())"),
    })
    UserAppDO buildUserAppInfo(UserDO userDO, AppInfoDO appInfoDO);
}

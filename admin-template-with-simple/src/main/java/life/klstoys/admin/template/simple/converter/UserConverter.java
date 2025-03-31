package life.klstoys.admin.template.simple.converter;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.simple.entity.UserInfoEntity;
import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import life.klstoys.admin.template.simple.web.controller.request.RegisterRequest;
import life.klstoys.admin.template.utils.CommonUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 14:46
 */
@Mapper(imports = {UserPermissionEnum.class, Set.class, CommonStatusEnum.class, CommonUtil.class})
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "permission", expression = "java(Set.of(UserPermissionEnum.BASIC))"),
            @Mapping(target = "status", expression = "java(CommonStatusEnum.ENABLE)"),
            @Mapping(target = "password", expression = "java(CommonUtil.encodePassword(request.getPassword()))"),
            @Mapping(target = "createTime", ignore = true),
            @Mapping(target = "updateTime", ignore = true),
            @Mapping(target = "nickname", ignore = true)
    })
    UserDO convertRequestToDO(RegisterRequest request);

    UserInfoEntity convertDOToEntity(UserDO userDO);
}

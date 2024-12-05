package top.ann.zhgy.admin.template.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import top.ann.zhgy.admin.template.dal.domain.UserDO;
import top.ann.zhgy.admin.template.entity.UserInfoEntity;
import top.ann.zhgy.admin.template.enums.CommonStatusEnum;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;
import top.ann.zhgy.admin.template.utils.CommonUtil;
import top.ann.zhgy.admin.template.web.controller.request.RegisterRequest;

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
            @Mapping(target = "updateTime", ignore = true)
    })
    UserDO convertRequestToDO(RegisterRequest request);

    UserInfoEntity convertDOToEntity(UserDO userDO);
}

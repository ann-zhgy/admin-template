package top.ann.zhgy.admin.template.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import top.ann.zhgy.admin.template.aop.annotation.Permission;
import top.ann.zhgy.admin.template.web.context.RequestContext;
import top.ann.zhgy.admin.template.dal.domain.UserDO;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.exception.ExceptionEnum;

import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:22
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {
    @Pointcut(value = "@annotation(top.ann.zhgy.admin.template.aop.annotation.Permission) && execution(* top.ann.zhgy.admin.template.web.controller..*.*(..))", argNames = "permission")
    public void pointCut(Permission permission) {}

    @Before(value = "pointCut(permission)", argNames = "permission")
    public void before(Permission permission) {
        if (Objects.isNull(permission) || ArrayUtils.isEmpty(permission.value()) || Objects.isNull(RequestContext.getUserInfo())) {
            return;
        }
        UserPermissionEnum[] value = permission.value();
        UserDO userInfo = RequestContext.getUserInfo();
        for (UserPermissionEnum permissionEnum : value) {
            if (userInfo.getPermission().contains(permissionEnum)) {
                return;
            }
        }
        throw new BizException(ExceptionEnum.NO_PERMISSION);
    }
}

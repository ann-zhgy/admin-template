package life.klstoys.admin.template.simple.aop.aspect;

import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.simple.aop.annotation.Permission;
import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import life.klstoys.admin.template.simple.exceptions.SimpleExceptionEnum;
import life.klstoys.admin.template.simple.web.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:22
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {
    @Pointcut(value = "@annotation(life.klstoys.admin.template.simple.aop.annotation.Permission) && execution(* life.klstoys.admin.template.simple.web.controller..*.*(..))", argNames = "permission")
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
        throw new BizException(SimpleExceptionEnum.NO_PERMISSION);
    }
}

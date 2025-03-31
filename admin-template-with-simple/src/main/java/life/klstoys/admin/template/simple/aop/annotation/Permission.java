package life.klstoys.admin.template.simple.aop.annotation;

import life.klstoys.admin.template.simple.enums.UserPermissionEnum;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:18
 */
public @interface Permission {
    UserPermissionEnum[] value() default {};
}

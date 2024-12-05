package top.ann.zhgy.admin.template.aop.annotation;

import top.ann.zhgy.admin.template.enums.UserPermissionEnum;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:18
 */
public @interface Permission {
    UserPermissionEnum[] value() default {};
}

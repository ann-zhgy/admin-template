package life.klstoys.admin.template.rbac.validate.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import life.klstoys.admin.template.rbac.validate.validator.AppKeyChecker;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 18:05
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AppKeyChecker.class)
public @interface AppKeyCheck {
    /** 检验不通过的提示消息 */
    String message() default "appKey无效";
    AppCheckStatusEnum status() default AppCheckStatusEnum.ENABLED;
    /** 分组 */
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    enum AppCheckStatusEnum {
        ENABLED, DISABLED, ALL
    }
}

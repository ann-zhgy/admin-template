package life.klstoys.admin.template.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import life.klstoys.admin.template.validator.validation.impl.PhoneValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * 
 * @author zhanggaoyu@workatdata.com
 * @since 2024/9/26 11:28
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    /** 检验不通过的提示消息 */
    String message() default "手机号格式不正确";
    /** 分组 */
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

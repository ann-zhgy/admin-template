package top.ann.zhgy.admin.template.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import top.ann.zhgy.admin.template.validator.validation.impl.InValidator;

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
@Constraint(validatedBy = InValidator.class)
public @interface In {
    /** 检验不通过的提示消息 */
    String message() default "参数必须为指定值";
    /** 分组 */
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    /** 允许的string值 */
    String[] allowedStringValue() default {};
    /** 允许的int值 */
    int[] allowedIntValue() default {};
}

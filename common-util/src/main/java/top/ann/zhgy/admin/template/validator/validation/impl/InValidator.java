package top.ann.zhgy.admin.template.validator.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.exception.ExceptionEnum;
import top.ann.zhgy.admin.template.validator.annotation.In;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/9/26 11:31
 */
@Slf4j
public class InValidator implements ConstraintValidator<In, Object> {
    private Set<Object> allowedValues;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // 此注解校验器不负责处理null，null需要由 @NotNull 负责处理
        if (value == null) {
            return true;
        }
        return allowedValues.contains(value);
    }

    @Override
    public void initialize(In constraintAnnotation) {
        if (constraintAnnotation.allowedIntValue().length > 0) {
            allowedValues = Arrays.stream(constraintAnnotation.allowedIntValue()).boxed().collect(Collectors.toUnmodifiableSet());
        } else if (constraintAnnotation.allowedStringValue().length > 0) {
            allowedValues = Arrays.stream(constraintAnnotation.allowedStringValue()).collect(Collectors.toUnmodifiableSet());
        } else {
            log.error("InValidator initialize fail: allowedIntValue 和 allowedStringValue 不可以同时设置");
            throw BizException.of(ExceptionEnum.VALIDATOR_IN_INITIAL_FAIL);
        }
    }
}

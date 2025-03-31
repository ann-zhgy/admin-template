package life.klstoys.admin.template.validator.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import life.klstoys.admin.template.validator.annotation.Phone;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Pattern;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/9/26 11:31
 */
@Slf4j
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    private static final Pattern PHONE_REGEX = Pattern.compile("^1[3-9]\\d{9}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 此注解校验器不负责处理null，null需要由 @NotNull 负责处理
        if (value == null) {
            return true;
        }
        return PHONE_REGEX.matcher(value).matches();
    }
}

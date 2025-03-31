package life.klstoys.admin.template.rbac.validate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.repository.AppInfoRepository;
import life.klstoys.admin.template.rbac.validate.annotation.AppKeyCheck;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 18:07
 */
public class AppKeyChecker implements ConstraintValidator<AppKeyCheck, String> {
    @Setter(onMethod_ = @Autowired)
    private AppInfoRepository appInfoRepository;
    private AppKeyCheck.AppCheckStatusEnum status;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            return true;
        }
        AppInfoDO appInfoDO = appInfoRepository.selectByAppKey(value);
        return Objects.nonNull(appInfoDO)
               && (status == AppKeyCheck.AppCheckStatusEnum.ALL ||
                   (status == AppKeyCheck.AppCheckStatusEnum.ENABLED ?
                           appInfoDO.getStatus() == CommonStatusEnum.ENABLE :
                           appInfoDO.getStatus() == CommonStatusEnum.DISABLE));
    }

    @Override
    public void initialize(AppKeyCheck constraintAnnotation) {
        status = constraintAnnotation.status();
    }
}

package life.klstoys.admin.template.captcha.config;

import cloud.tianai.captcha.application.ImageCaptchaProperties;
import cloud.tianai.captcha.spring.autoconfiguration.SecondaryVerificationProperties;
import cloud.tianai.captcha.spring.autoconfiguration.SpringImageCaptchaProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/6 15:04
 */
public class ImageCaptchaPropertiesPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        if (bean instanceof ImageCaptchaProperties properties) {
            properties.setPrefix("behavior-captcha");
            Map<String, Long> expire = new HashMap<>();
            expire.put("default", 10000L);
            expire.put("WORD_IMAGE_CLICK", 20000L);
            properties.setExpire(expire);
            properties.setLocalCacheEnabled(true);
            properties.setLocalCacheSize(20);
            properties.setLocalCacheWaitTime(5000);
            properties.setLocalCachePeriod(2000);
            if (bean instanceof SpringImageCaptchaProperties springProperties) {
                springProperties.setFontPath(List.of("classpath:captcha/font/SourceHanSansSC-Normal.otf"));
                SecondaryVerificationProperties secondaryVerificationProperties = new SecondaryVerificationProperties();
                secondaryVerificationProperties.setEnabled(false);
                secondaryVerificationProperties.setExpire(120000L);
                secondaryVerificationProperties.setKeyPrefix("behavior-captcha:secondary");
                springProperties.setSecondary(secondaryVerificationProperties);
            }
        }
        return bean;
    }
}

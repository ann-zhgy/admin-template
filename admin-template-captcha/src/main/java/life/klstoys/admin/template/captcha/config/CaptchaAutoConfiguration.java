package life.klstoys.admin.template.captcha.config;

import cloud.tianai.captcha.spring.autoconfiguration.ImageCaptchaAutoConfiguration;
import life.klstoys.admin.template.captcha.config.properties.CaptchaResourcePathProperties;
import life.klstoys.admin.template.captcha.web.CaptchaController;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/5 16:20
 */
@AutoConfiguration(before = ImageCaptchaAutoConfiguration.class)
@EnableConfigurationProperties(CaptchaResourcePathProperties.class)
public class CaptchaAutoConfiguration {
    @Bean
    public ImageCaptchaPropertiesPostProcessor imageCaptchaProperties() {
        return new ImageCaptchaPropertiesPostProcessor();
    }

    @Bean
    public ResourceStorePostProcessor resourceStorePostProcessor(CaptchaResourcePathProperties properties) {
        return new ResourceStorePostProcessor(properties);
    }

    @Bean
    public CaptchaController captchaController() {
        return new CaptchaController();
    }
}

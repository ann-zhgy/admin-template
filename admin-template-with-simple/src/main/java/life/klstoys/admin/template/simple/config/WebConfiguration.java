package life.klstoys.admin.template.simple.config;

import life.klstoys.admin.template.simple.web.interceptor.LoginAndAuthorityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Nonnull;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 16:30
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addInterceptors(@Nonnull InterceptorRegistry registry) {
        registry.addInterceptor(loginAndAuthorityInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/login*", "/register", "/send-captcha", "/captcha/*");
    }

    @Bean
    public LoginAndAuthorityInterceptor loginAndAuthorityInterceptor() {
        return new LoginAndAuthorityInterceptor();
    }
}

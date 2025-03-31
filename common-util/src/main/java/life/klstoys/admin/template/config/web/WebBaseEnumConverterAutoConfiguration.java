package life.klstoys.admin.template.config.web;

import jakarta.annotation.Nonnull;
import jakarta.servlet.Filter;
import life.klstoys.admin.template.config.properties.WebAuthFilterProperties;
import life.klstoys.admin.template.config.properties.WebExceptionProperties;
import life.klstoys.admin.template.config.web.convert.BooleanToBaseEnumConverterFactory;
import life.klstoys.admin.template.config.web.convert.IntegerToBaseEnumConverterFactory;
import life.klstoys.admin.template.config.web.convert.StringToBaseEnumConverterFactory;
import life.klstoys.admin.template.web.filter.AuthenticateFilter;
import life.klstoys.admin.template.web.filter.LogAndExceptionHandleFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/12 13:37
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({WebExceptionProperties.class, WebAuthFilterProperties.class})
public class WebBaseEnumConverterAutoConfiguration implements WebMvcConfigurer {
    @Override
    public void addFormatters(@Nonnull FormatterRegistry registry) {
        registry.addConverterFactory(new StringToBaseEnumConverterFactory());
        registry.addConverterFactory(new BooleanToBaseEnumConverterFactory());
        registry.addConverterFactory(new IntegerToBaseEnumConverterFactory());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("*")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean<Filter> logAndExceptionHandleFilterRegistrationBean(WebExceptionProperties properties) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        LogAndExceptionHandleFilter logAndExceptionHandleFilter = new LogAndExceptionHandleFilter(properties);
        registrationBean.setFilter(logAndExceptionHandleFilter);
        registrationBean.setOrder(logAndExceptionHandleFilter.getOrder());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(name = "web.base.auth.filter.enabled", havingValue = "true")
    public FilterRegistrationBean<Filter> authFilterRegistrationBean(WebAuthFilterProperties properties) {
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
        AuthenticateFilter authenticateFilter = new AuthenticateFilter(properties.getUsername(), properties.getPassword());
        registrationBean.setFilter(authenticateFilter);
        registrationBean.setOrder(authenticateFilter.getOrder());
        registrationBean.addUrlPatterns("/*");
        registrationBean.addInitParameter("excludedUris", String.join(",", properties.getExcludeUrls()));
        return registrationBean;
    }

    // 兼容vue history模式的url
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
    }
}

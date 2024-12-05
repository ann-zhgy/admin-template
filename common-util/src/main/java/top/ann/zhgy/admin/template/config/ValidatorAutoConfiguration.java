/*
 * Copyright ©2022-2024 workatdata Corporation, All Rights Reserved.
 */

package top.ann.zhgy.admin.template.config;

import jakarta.validation.Validator;
import jakarta.validation.executable.ExecutableValidator;
import org.hibernate.validator.BaseHibernateValidatorConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.boot.validation.MessageInterpolatorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Role;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/9/26 14:16
 */
@ConditionalOnClass({ExecutableValidator.class, BaseHibernateValidatorConfiguration.class})
@AutoConfiguration(before = ValidationAutoConfiguration.class)
@ConditionalOnProperty(prefix = "web.exception", value = "validation-fast-fail", havingValue = "true")
@ConditionalOnResource(resources = "classpath:META-INF/services/jakarta.validation.spi.ValidationProvider")
public class ValidatorAutoConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean(Validator.class)
    public LocalValidatorFactoryBean defaultValidator(ApplicationContext applicationContext,
                                                      ObjectProvider<ValidationConfigurationCustomizer> customizers) {
        LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean();
        factoryBean.setConfigurationInitializer((configuration) -> customizers.orderedStream()
                .forEach((customizer) -> customizer.customize(configuration)));
        MessageInterpolatorFactory interpolatorFactory = new MessageInterpolatorFactory(applicationContext);
        factoryBean.getValidationPropertyMap().put(BaseHibernateValidatorConfiguration.FAIL_FAST, Boolean.TRUE.toString());
        factoryBean.setMessageInterpolator(interpolatorFactory.getObject());
        return factoryBean;
    }
}

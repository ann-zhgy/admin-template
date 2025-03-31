package life.klstoys.admin.template.config;

import life.klstoys.admin.template.SpringUtilBeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * applicationContextInitializer
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 14:48:33
 */
public class SpringUtilApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new SpringUtilBeanDefinitionRegistryPostProcessor());
    }
}

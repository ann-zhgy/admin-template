package life.klstoys.admin.template;

import jakarta.annotation.Nonnull;
import life.klstoys.admin.template.utils.SpringUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 * BeanFactoryPostProcessor
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 14:50:47
 */
public class SpringUtilBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registry.registerBeanDefinition("springUtil", new RootBeanDefinition(SpringUtil.class));
    }

    @Override
    public void postProcessBeanFactory(@Nonnull ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}

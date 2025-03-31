package life.klstoys.admin.template.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * spring 工具
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 10:43:56
 */
public class SpringUtil implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 通过bean的类型获取bean
     *
     * @param beanType bean类型
     * @return bean
     * @param <T> bean的类型
     */
    public static <T> T getBean(Class<T> beanType) {
        return getApplicationContext().getBean(beanType);
    }

    /**
     * 通过bean的名称获取bean
     *
     * @param beanName bean名称
     * @return bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) getApplicationContext().getBean(beanName);
    }

    /**
     * 通过bean的类型和bean的名称获取bean
     *
     * @param beanType bean类型
     * @param beanName bean名称
     * @return bean
     * @param <T> bean的类型
     */
    public static <T> T getBean(Class<T> beanType, String beanName) {
        return getApplicationContext().getBean(beanName, beanType);
    }

    /**
     * 获取某一类型所有的bean
     *
     * @param beanType bean类型
     * @return beans
     * @param <T> bean的类型
     */
    public static <T> Map<String, T> getBeansByType(Class<T> beanType) {
        return getApplicationContext().getBeansOfType(beanType);
    }


    /**
     * 发布事件
     *
     * @param event event
     */
    public static void publishEvent(Object event) {
        getApplicationContext().publishEvent(event);
    }

    /**
     * 发布事件
     *
     * @param event event
     */
    public static void publishEvent(ApplicationEvent event) {
        getApplicationContext().publishEvent(event);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    @Nullable
    public static String getEnvironmentProperty(String key) {
        return getApplicationContext().getEnvironment().getProperty(key);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    public static String getEnvironmentProperty(String key, String defaultValue) {
        return getApplicationContext().getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    @Nullable
    public static <T> T getEnvironmentProperty(String key, Class<T> targetType) {
        return getApplicationContext().getEnvironment().getProperty(key, targetType);
    }

    /**
     * 获取配置文件配置项的值
     *
     * @param key 配置项key
     */
    public static <T> T getEnvironmentProperty(String key, Class<T> targetType, T defaultValue) {
        return getApplicationContext().getEnvironment().getProperty(key, targetType, defaultValue);
    }

    /**
     * 获取 spring.profiles.active
     */
    public static String[] getActiveProfile() {
        return getApplicationContext().getEnvironment().getActiveProfiles();
    }
}

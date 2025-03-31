package life.klstoys.admin.template.config;

import com.alibaba.ttl.TtlRunnable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Nonnull;

/**
 * 线程池配置
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/9/13 15:29
 */
@ConditionalOnClass({ThreadPoolTaskExecutor.class})
@EnableConfigurationProperties({TaskExecutionProperties.class})
@AutoConfiguration(before = TaskExecutionAutoConfiguration.class)
public class ThreadPoolAutoConfiguration {
    private static final int MAX_BLOCKING_QUEUE_CAPACITY = 100000;

    @Bean
    public TaskDecorator taskDecorator() {
        return new TaskDecorator() {
            @Override
            public @Nonnull Runnable decorate(@Nonnull Runnable runnable) {
                return TtlRunnable.get(runnable);
            }
        };
    }

    @Bean
    @SuppressWarnings("removal")
    @ConditionalOnMissingBean({ TaskExecutorBuilder.class, ThreadPoolTaskExecutorBuilder.class })
    ThreadPoolTaskExecutorBuilder threadPoolTaskExecutorBuilder(TaskExecutionProperties properties,
                                                                ObjectProvider<ThreadPoolTaskExecutorCustomizer> threadPoolTaskExecutorCustomizers,
                                                                ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
                                                                ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        ThreadPoolTaskExecutorBuilder builder = new ThreadPoolTaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity() == Integer.MAX_VALUE ? MAX_BLOCKING_QUEUE_CAPACITY : pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        int defaultMaxSize = Runtime.getRuntime().availableProcessors() * 3;
        builder = builder.maxPoolSize(pool.getMaxSize() == Integer.MAX_VALUE ? Math.max(pool.getCoreSize(), defaultMaxSize) : pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(threadPoolTaskExecutorCustomizers.orderedStream()::iterator);
        builder = builder.taskDecorator(taskDecorator.getIfUnique());
        // Apply the deprecated TaskExecutorCustomizers, too
        builder = builder.additionalCustomizers(taskExecutorCustomizers.orderedStream()
                .map(customizer -> (ThreadPoolTaskExecutorCustomizer) customizer::customize).toList());
        return builder;
    }

    @Bean
    @SuppressWarnings("removal")
    @ConditionalOnMissingBean
    TaskExecutorBuilder taskExecutorBuilder(TaskExecutionProperties properties,
                                            ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
                                            ObjectProvider<TaskDecorator> taskDecorator) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity() == Integer.MAX_VALUE ? MAX_BLOCKING_QUEUE_CAPACITY : pool.getQueueCapacity());
        builder = builder.corePoolSize(pool.getCoreSize());
        builder = builder.maxPoolSize(pool.getMaxSize());
        int defaultMaxSize = Runtime.getRuntime().availableProcessors() * 3;
        builder = builder.maxPoolSize(pool.getMaxSize() == Integer.MAX_VALUE ? Math.max(pool.getCoreSize(), defaultMaxSize) : pool.getMaxSize());
        builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
        builder = builder.keepAlive(pool.getKeepAlive());
        TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
        builder = builder.awaitTermination(shutdown.isAwaitTermination());
        builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
        builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
        builder = builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
        builder = builder.taskDecorator(taskDecorator.getIfUnique());
        return builder;
    }
}

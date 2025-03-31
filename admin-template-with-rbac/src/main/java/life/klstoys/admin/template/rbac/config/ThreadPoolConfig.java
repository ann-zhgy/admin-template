package life.klstoys.admin.template.rbac.config;

import com.alibaba.ttl.TtlRunnable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/8/28 15:38
 */
@Configuration
public class ThreadPoolConfig {
    @Bean(value = "commonExecutorService", destroyMethod = "shutdown")
    public ThreadPoolTaskExecutor combinationExecutorService() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(12);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(2000);
        executor.setThreadNamePrefix("thread-query-");
        executor.setKeepAliveSeconds(20);
        executor.setTaskDecorator(TtlRunnable::get);
        executor.initialize();
        return executor;
    }
}

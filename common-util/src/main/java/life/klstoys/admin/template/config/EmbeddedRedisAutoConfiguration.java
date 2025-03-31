package life.klstoys.admin.template.config;

import life.klstoys.admin.template.config.properties.RedisTypeProperties;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;
import redis.embedded.core.RedisServerBuilder;

import java.io.IOException;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 11:48
 */
@Slf4j
@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisTypeProperties.class)
@ConditionalOnProperty(name = "redis.type", havingValue = "embedded")
public class EmbeddedRedisAutoConfiguration {
    @Setter(onMethod_ = @Autowired)
    private RedisProperties redisProperties;
    @Setter(onMethod_ = @Autowired)
    private RedisTypeProperties redisTypeProperties;

    @Bean(destroyMethod = "stop")
    public RedisServer redisServer() throws IOException {
        RedisServerBuilder builder = RedisServer.newRedisServer();
        builder.port(redisProperties.getPort());
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            builder.setting("requirepass " + redisProperties.getPassword());
        }
        if (StringUtils.isNotBlank(redisTypeProperties.getMaxMemory())) {
            builder.setting("maxmemory " + redisTypeProperties.getMaxMemory());
        }
        RedisServer redisServer = builder.build();
        redisServer.start();
        log.info("embedded redis server started");
        return redisServer;
    }
}

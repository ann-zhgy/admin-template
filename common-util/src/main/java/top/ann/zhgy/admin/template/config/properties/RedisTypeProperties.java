package top.ann.zhgy.admin.template.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 12:12
 */
@Data
@ConfigurationProperties("redis")
public class RedisTypeProperties {
    private RedisType type = RedisType.NONE;
    private String maxMemory = "512mb";

    public enum RedisType {
        EMBEDDED, REMOTE, NONE
    }
}

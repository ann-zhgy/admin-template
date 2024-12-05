package top.ann.zhgy.admin.template.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 15:29
 */
@Data
@ConfigurationProperties("web.base.auth.filter")
public class WebAuthFilterProperties {
    private boolean enabled = false;
    private String username = "admin";
    private String password = "admin";
    private List<String> excludeUrls = List.of("/login");
}

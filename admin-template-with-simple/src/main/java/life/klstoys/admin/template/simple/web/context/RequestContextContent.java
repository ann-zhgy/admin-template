package life.klstoys.admin.template.simple.web.context;

import life.klstoys.admin.template.simple.dal.domain.UserDO;
import lombok.Data;

/**
 * 请求上下文的内容
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/18 17:54
 */
@Data
public class RequestContextContent {
    private String token;
    private UserDO userInfo;
    private String remoteIp;
}

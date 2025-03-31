package life.klstoys.admin.template.rbac.web.context;

import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
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
    private UserAuthorInfoDO userInfo;
    private String remoteIp;
}

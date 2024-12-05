package top.ann.zhgy.admin.template.web.context;

import lombok.Data;
import top.ann.zhgy.admin.template.dal.domain.UserDO;

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

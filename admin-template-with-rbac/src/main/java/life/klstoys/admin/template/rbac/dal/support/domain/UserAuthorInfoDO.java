package life.klstoys.admin.template.rbac.dal.support.domain;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:23
 */
@Data
public class UserAuthorInfoDO {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String appKey;
    private Set<String> authorizedUrls;
    private CommonStatusEnum status;
}

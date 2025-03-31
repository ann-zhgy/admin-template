package life.klstoys.admin.template.rbac.dal.support.domain;

import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 12:43
 */
@Data
public class UserAuthorMenuDO {
    private String menuNo;
    private String menuComponentKey;
    private String menuTitle;
    private String functionGroupNo;
}

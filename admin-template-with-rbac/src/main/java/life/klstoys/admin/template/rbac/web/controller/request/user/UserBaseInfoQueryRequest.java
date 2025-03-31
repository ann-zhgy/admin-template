package life.klstoys.admin.template.rbac.web.controller.request.user;

import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 13:35
 */
@Data
public class UserBaseInfoQueryRequest {
    /** 是否查询菜单，默认不查询 */
    private Boolean queryMenus = false;
    /** appKey */
    private String appKey;
}

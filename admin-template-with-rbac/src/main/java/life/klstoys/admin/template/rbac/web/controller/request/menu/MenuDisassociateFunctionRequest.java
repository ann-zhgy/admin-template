package life.klstoys.admin.template.rbac.web.controller.request.menu;

import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 16:48
 */
@Data
public class MenuDisassociateFunctionRequest {
    /** 功能id */
    private Set<Long> functionGroupIds;
}

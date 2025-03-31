package life.klstoys.admin.template.simple.web.controller.request;

import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:04
 */
@Data
public class UserUpdatePermissionRequest {
    private Long id;

    /**
     * 权限
     */
    private Set<UserPermissionEnum> permissions;
}

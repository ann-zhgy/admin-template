package top.ann.zhgy.admin.template.web.controller.request;

import lombok.Data;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;

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

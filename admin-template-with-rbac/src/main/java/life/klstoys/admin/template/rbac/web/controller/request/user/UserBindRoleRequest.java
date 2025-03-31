package life.klstoys.admin.template.rbac.web.controller.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 13:49
 */
@Data
public class UserBindRoleRequest {
    /** 角色id */
    @NotNull(message = "角色id不能为空")
    @NotEmpty(message = "角色id不能为空")
    @Size(min = 1, max = 20, message = "角色id数量不能超过20")
    private Set<Long> roleIds;
}

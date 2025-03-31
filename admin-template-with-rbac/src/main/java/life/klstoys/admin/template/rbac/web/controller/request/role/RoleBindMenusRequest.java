package life.klstoys.admin.template.rbac.web.controller.request.role;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/12 15:36
 */
@Data
public class RoleBindMenusRequest {
    /** 页面映射id */
    @Valid
    @NotNull(message = "菜单信息不能为空")
    @Size(min = 1, max = 20, message = "菜单信息长度不能超过20")
    private Set<String> functionGroupNos;
}

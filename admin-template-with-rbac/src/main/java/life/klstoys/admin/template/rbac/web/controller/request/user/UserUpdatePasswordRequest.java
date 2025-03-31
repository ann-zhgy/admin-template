package life.klstoys.admin.template.rbac.web.controller.request.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:04
 */
@Data
public class UserUpdatePasswordRequest {
    /**
     * 用户名
     */
    @NotNull(message = "用户名不能为空")
    private Long id;

    /**
     * 原密码
     */
    @NotEmpty(message = "原密码不能为空")
    private String originPassword;

    /**
     * 新密码
     */
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}

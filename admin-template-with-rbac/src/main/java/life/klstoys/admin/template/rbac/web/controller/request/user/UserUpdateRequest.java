package life.klstoys.admin.template.rbac.web.controller.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import life.klstoys.admin.template.validator.annotation.Phone;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:04
 */
@Data
public class UserUpdateRequest {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     */
    @Phone
    @NotEmpty(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @NotEmpty(message = "邮箱不能为空")
    private String email;
}

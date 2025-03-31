package life.klstoys.admin.template.simple.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 10:01
 */
@Data
public class RegisterRequest {
    /** 用户名 */
    @NotBlank(message = "用户名不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
    /** 手机号 */
    private String phone;
    /** 邮箱 */
    private String email;
}

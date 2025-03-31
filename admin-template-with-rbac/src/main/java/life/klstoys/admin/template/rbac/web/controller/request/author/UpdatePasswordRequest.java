package life.klstoys.admin.template.rbac.web.controller.request.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/19 18:46
 */
@Data
public class UpdatePasswordRequest {
    /** 邮箱/手机号 */
    @NotBlank(message = "邮箱/手机号不能为空")
    private String username;
    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
    /** 新密码 */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}

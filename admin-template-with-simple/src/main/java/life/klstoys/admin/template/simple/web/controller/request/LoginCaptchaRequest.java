package life.klstoys.admin.template.simple.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 12:44
 */
@Data
public class LoginCaptchaRequest {
    /** 用户名 */
    @NotBlank(message = "手机号/邮箱不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "验证码不能为空")
    private String captcha;
}

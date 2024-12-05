package top.ann.zhgy.admin.template.web.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 10:01
 */
@Data
public class LoginPasswordRequest {
    /** 用户名 */
    @NotBlank(message = "用户名/手机号/邮箱不能为空")
    private String username;
    /** 密码 */
    @NotBlank(message = "密码不能为空")
    private String password;
}

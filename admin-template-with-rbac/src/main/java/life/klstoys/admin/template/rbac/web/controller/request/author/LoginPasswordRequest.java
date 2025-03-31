package life.klstoys.admin.template.rbac.web.controller.request.author;

import jakarta.validation.constraints.NotBlank;
import life.klstoys.admin.template.rbac.validate.annotation.AppKeyCheck;
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
    /** 应用标识 */
    @AppKeyCheck
    @NotBlank(message = "应用标识不能为空")
    private String appKey;
}

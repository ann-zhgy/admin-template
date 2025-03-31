package life.klstoys.admin.template.simple.web.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:04
 */
@Data
public class UserUpdatePasswordRequest {
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

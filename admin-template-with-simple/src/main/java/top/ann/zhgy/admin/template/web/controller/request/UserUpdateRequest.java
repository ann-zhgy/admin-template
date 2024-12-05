package top.ann.zhgy.admin.template.web.controller.request;

import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "用户名不能为空")
    private String username;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空")
    private String phone;

    /**
     * 邮箱
     */
    @NotNull(message = "邮箱不能为空")
    private String email;
}

package life.klstoys.admin.template.simple.web.controller.request;

import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 12:44
 */
@Data
public class SendCaptchaRequest {
    /** 手机号 */
    private String phone;
    /** 邮箱 */
    private String email;
}

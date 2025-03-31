package life.klstoys.admin.template.captcha.web.request;

import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/28 14:07
 */
@Data
public class CaptchaCheckRequest {
    private String id;
    private ImageCaptchaTrack data;
}

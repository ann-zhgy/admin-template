package life.klstoys.admin.template.captcha.web;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import life.klstoys.admin.template.captcha.enums.CaptchaTypeEnum;
import life.klstoys.admin.template.captcha.web.request.CaptchaCheckRequest;
import life.klstoys.admin.template.common.response.BaseHttpResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/5 16:15
 */
@RestController
public class CaptchaController {
    @Setter(onMethod_ = @Autowired)
    private ImageCaptchaApplication captchaApplication;

    @GetMapping("/captcha/get")
    public BaseHttpResponse<CaptchaResponse<ImageCaptchaVO>> get(@RequestParam("captchaType") CaptchaTypeEnum captchaType) {
        CaptchaResponse<ImageCaptchaVO> response = captchaApplication.generateCaptcha(captchaType.getEncode());
        return BaseHttpResponse.success(response);
    }

    @PostMapping("/captcha/check")
    public BaseHttpResponse<Boolean> check(@RequestBody CaptchaCheckRequest request) {
        ApiResponse<?> matching = captchaApplication.matching(request.getId(), request.getData());
        if (matching.isSuccess()) {
            return BaseHttpResponse.success(matching.isSuccess());
        }
        return new BaseHttpResponse<>(String.valueOf(matching.getCode()), matching.getMsg(), false);
    }
}

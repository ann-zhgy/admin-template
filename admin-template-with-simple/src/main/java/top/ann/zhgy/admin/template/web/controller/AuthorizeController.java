package top.ann.zhgy.admin.template.web.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.ann.zhgy.admin.template.common.response.BaseHttpResponse;
import top.ann.zhgy.admin.template.enums.CaptchaTypeEnum;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.exception.ExceptionEnum;
import top.ann.zhgy.admin.template.service.UserService;
import top.ann.zhgy.admin.template.web.controller.request.CaptchaCheckRequest;
import top.ann.zhgy.admin.template.web.controller.request.LoginCaptchaRequest;
import top.ann.zhgy.admin.template.web.controller.request.LoginPasswordRequest;
import top.ann.zhgy.admin.template.web.controller.request.RegisterRequest;
import top.ann.zhgy.admin.template.web.controller.request.SendCaptchaRequest;
import top.ann.zhgy.admin.template.web.controller.response.LoginResponse;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:38
 */
@RestController
public class AuthorizeController {
    @Setter(onMethod_ = @Autowired)
    private UserService userService;
    @Setter(onMethod_ = @Autowired)
    private ImageCaptchaApplication captchaApplication;

    @PostMapping("login-by-password")
    public BaseHttpResponse<LoginResponse> loginByPassword(@Validated @RequestBody LoginPasswordRequest request) {
        return BaseHttpResponse.success(LoginResponse.of(userService.loginByPassword(request.getUsername(), request.getPassword())));
    }

    @PostMapping("login-by-captcha")
    public BaseHttpResponse<LoginResponse> loginByCaptcha(@Validated @RequestBody LoginCaptchaRequest request) {
        return BaseHttpResponse.success(LoginResponse.of(userService.loginByCaptcha(request.getUsername(), request.getCaptcha())));
    }

    @PostMapping("register")
    public BaseHttpResponse<String> register(@Validated @RequestBody RegisterRequest request) {
        userService.register(request);
        return BaseHttpResponse.success();
    }

    @PostMapping("send-captcha")
    public BaseHttpResponse<String> sendCaptcha(@Validated @RequestBody SendCaptchaRequest request) {
        if (StringUtils.isBlank(request.getEmail()) && StringUtils.isBlank(request.getPhone())) {
            throw new BizException(ExceptionEnum.PARAM_INVALID, "请传入手机号或邮箱");
        }
        userService.sendCaptcha(request);
        return BaseHttpResponse.success();
    }

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

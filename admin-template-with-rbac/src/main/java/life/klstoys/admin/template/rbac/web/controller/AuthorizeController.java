package life.klstoys.admin.template.rbac.web.controller;

import life.klstoys.admin.template.common.response.BaseHttpResponse;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.exception.ExceptionEnum;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginPasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.RegisterRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.SendCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.UpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.response.LoginResponse;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:38
 */
@RestController
public class AuthorizeController {
    @Setter(onMethod_ = @Autowired)
    private UserService userService;

    @PostMapping("login-by-password")
    public BaseHttpResponse<LoginResponse> loginByPassword(@Validated @RequestBody LoginPasswordRequest request) {
        return BaseHttpResponse.success(LoginResponse.of(userService.loginByPassword(request)));
    }

    @PostMapping("login-by-captcha")
    public BaseHttpResponse<LoginResponse> loginByCaptcha(@Validated @RequestBody LoginCaptchaRequest request) {
        return BaseHttpResponse.success(LoginResponse.of(userService.loginByCaptcha(request)));
    }

    @PostMapping("register")
    public BaseHttpResponse<String> register(@Validated @RequestBody RegisterRequest request) {
        userService.register(request);
        return BaseHttpResponse.success();
    }

    @PostMapping("update-password")
    public BaseHttpResponse<String> updatePassword(@Validated @RequestBody UpdatePasswordRequest request) {
        userService.updatePasswordNoLogin(request);
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
}

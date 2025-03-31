package life.klstoys.admin.template.simple.exceptions;

import life.klstoys.admin.template.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/7 14:56
 */
@Getter
@AllArgsConstructor
public enum SimpleExceptionEnum implements BaseExceptionType {
    TOKEN_IS_BLANK("10001", "账号未登陆，请登录"),
    TOKEN_INVALID("10002", "账号登陆状态异常，请重新登录"),
    USER_IS_EXISTS("10003", "账号已存在"),
    EMAIL_IS_EXISTS("10003", "邮件已注册"),
    PHONE_IS_EXISTS("10003", "手机号已注册"),
    USER_NOT_EXISTS("10004", "账号不存在"),
    USER_INFO_NOT_MATCH("10005", "账号和密码不匹配"),
    NO_PERMISSION("10007", "无权限"),
    CAPTCHA_ERROR("10009", "验证码错误"),
    EXCEEDING_THE_SCOPE_OF_PERMISSIONS("10010", "超出可授予的权限范围"),
    USER_DISABLE("10011", "账号已停用，不可操作！"),
    CANNOT_UPDATE_SELF_OPERATION("10012", "不能对自己的账号执行此操作！"),
    UNSUPPORTED_PERMISSION_OPERATION("10013", "无法执行此操作！"),
    ORIGIN_PASSWORD_ERROR("10014", "原密码不匹配！"),

    ;
    private final String code;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMessage(Object... param) {
        return String.format(message, param);
    }
}
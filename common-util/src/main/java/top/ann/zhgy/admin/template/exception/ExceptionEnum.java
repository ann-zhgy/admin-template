package top.ann.zhgy.admin.template.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常类型枚举
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 10:58:39
 */
@Getter
@AllArgsConstructor
public enum ExceptionEnum implements BaseExceptionType {
    TOKEN_IS_BLANK("10001", "账号未登陆，请登录"),
    TOKEN_INVALID("10002", "账号登陆状态异常，请重新登录"),
    USER_IS_EXISTS("10003", "账号已存在"),
    EMAIL_IS_EXISTS("10003", "邮件已注册"),
    PHONE_IS_EXISTS("10003", "手机号已注册"),
    USER_NOT_EXISTS("10004", "账号不存在"),
    USER_INFO_NOT_MATCH("10005", "账号和密码不匹配"),
    NOTICE_NOT_EXISTS("10006", "公告不存在"),
    NO_PERMISSION("10007", "无权限"),
    LOGIN_PARAM_INVALID("10008", "登录参数异常：%s"),
    CAPTCHA_ERROR("10009", "验证码错误"),
    EXCEEDING_THE_SCOPE_OF_PERMISSIONS("10010", "超出可授予的权限范围"),
    USER_DISABLE("10011", "账号已停用，不可操作！"),
    CANNOT_UPDATE_SELF_OPERATION("10012", "不能对自己的账号执行此操作！"),
    UNSUPPORTED_PERMISSION_OPERATION("10013", "无法执行此操作！"),

    // 传入参数异常
    PARAM_INVALID("20001", "参数不合法：%s"),


    VALIDATOR_IN_INITIAL_FAIL("80001", "in注解检验器初始化失败"),
    // 系统异常
    SYSTEM_ERROR("99999", "系统异常"),
    JSON_SERIALIZE_ERROR("99998", "系统异常"),
    // 插入数据异常
    DATABASE_INSERT_ROW_ERROR("99996", "系统异常"),
    // 插入数据为空
    DATABASE_INSERT_DATA_NULL("99995", "系统异常"),
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

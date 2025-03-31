package life.klstoys.admin.template.rbac.exceptions;

import life.klstoys.admin.template.exception.BaseExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:20
 */
@Getter
@AllArgsConstructor
public enum RbacExceptionEnum implements BaseExceptionType {
    // 账号及账号权限相关
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
    ROLE_NOT_EXISTS("10015", "角色不存在"),
    MENU_NOT_EXIST("10016", "菜单不存在"),
    PARENT_MENU_NOT_EXIST("10017", "父级菜单不存在"),
    MENU_IS_DISABLED("10018", "菜单为禁用状态"),
    FUNCTION_NOT_EXIST("10019", "对象不存在"),
    MENU_MAP_INVALID("10020", "菜单信息无效"),
    APP_NOT_EXISTS("10021", "appkey不存在"),
    FUNCTION_GROUP_NOT_EXIST("10022", "功能组不存在"),
    FUNCTION_GROUP_UPDATE_APPKEY_NOT_MATCH("10023", "传入的功能与功能组的appKey不匹配"),
    ROLE_DISABLED("10024", "角色不可用"),
    USER_CACHE_NOT_EXISTS("10025", "账号缓存不可用"),
    APP_DISABLE("10026", "appkey不可用"),
    ;
    private final String code;
    private final String message;
}

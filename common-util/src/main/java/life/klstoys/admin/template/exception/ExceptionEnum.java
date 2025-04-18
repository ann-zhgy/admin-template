package life.klstoys.admin.template.exception;

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
    PARAM_INVALID("20001", "参数不合法：%s"),
    // 系统异常
    SYSTEM_ERROR("99999", "系统异常"),
    JSON_SERIALIZE_ERROR("99998", "系统异常"),
    // 插入数据异常
    DATABASE_INSERT_ROW_ERROR("99996", "系统异常"),
    DATABASE_UPDATE_ROW_ERROR("99996", "系统异常"),
    DATABASE_DELETE_ROW_ERROR("99996", "系统异常"),
    // 插入数据为空
    DATABASE_INSERT_DATA_NULL("99995", "系统异常"),
    DATABASE_UPDATE_DATA_NULL("99995", "系统异常"),
    DATABASE_DELETE_DATA_NULL("99995", "系统异常"),
    VALIDATOR_IN_INITIAL_FAIL("99994", "in注解检验器初始化失败"),
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

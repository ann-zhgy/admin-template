package top.ann.zhgy.admin.template.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * 基础异常类型
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 10:59:52
 */
public interface BaseExceptionType {
    String getCode();

    String getMessage();

    default String getMessage(Object... param) {
        String message = getMessage();
        if (StringUtils.isBlank(message)) {
            return message;
        }
        return String.format(message, param);
    }
}

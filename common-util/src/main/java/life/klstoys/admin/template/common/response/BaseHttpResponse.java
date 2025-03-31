package life.klstoys.admin.template.common.response;

import life.klstoys.admin.template.exception.BaseExceptionType;
import life.klstoys.admin.template.exception.BizException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 基础响应结构
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 15:00:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseHttpResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 6610370925769846891L;
    private static final String SUCCESS_CODE = "00000";
    private static final String SUCCESS_MESSAGE = "请求成功";

    /**
     * 响应码，正常业务系统中00000表示成功响应，数据接口 200 表示响应成功
     *
     * @mock 000000
     */
    private String code;
    /**
     * 响应消息
     *
     * @mock success
     */
    private String message;
    /**
     * 响应数据
     *
     * @mock ...
     */
    private T data;

    public static <T> BaseHttpResponse<T> success() {
        return new BaseHttpResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, null);
    }

    public static <T> BaseHttpResponse<T> success(T data) {
        return new BaseHttpResponse<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static <T> BaseHttpResponse<T> failure(BizException exception) {
        return new BaseHttpResponse<>(exception.getCode(), exception.getMessage(), null);
    }

    public static <T> BaseHttpResponse<T> failure(BaseExceptionType exception) {
        return new BaseHttpResponse<>(exception.getCode(), exception.getMessage(), null);
    }
}

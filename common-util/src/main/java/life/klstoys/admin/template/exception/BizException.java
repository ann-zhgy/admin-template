package life.klstoys.admin.template.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 业务异常
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 10:56:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BizException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -5978276071567184001L;
    private String code;
    private String message;

    public BizException() {
        this(ExceptionEnum.SYSTEM_ERROR);
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BizException(BaseExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public BizException(BaseExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage(), cause);
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
    }

    public BizException(BaseExceptionType exceptionType, Object... messageParam) {
        super(exceptionType.getMessage(messageParam));
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage(messageParam);
    }

    public BizException(BaseExceptionType exceptionType, Throwable cause, Object... messageParam) {
        super(exceptionType.getMessage(messageParam), cause);
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage(messageParam);
    }

    public static BizException of(BaseExceptionType exceptionType) {
        return new BizException(exceptionType);
    }

    public static BizException of(BaseExceptionType exceptionType, Throwable cause) {
        return new BizException(exceptionType, cause);
    }

    public static BizException of(BaseExceptionType exceptionType, Object... messageParam) {
        return new BizException(exceptionType, messageParam);
    }

    public static BizException of(BaseExceptionType exceptionType, Throwable cause, Object... messageParam) {
        return new BizException(exceptionType, cause, messageParam);
    }
}

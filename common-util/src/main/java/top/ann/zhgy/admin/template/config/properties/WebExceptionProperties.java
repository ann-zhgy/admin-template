package top.ann.zhgy.admin.template.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * web filter 相关的配置
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 14:56:53
 */
@Data
@Validated
@ConfigurationProperties("web.exception")
public class WebExceptionProperties {
    public static final String TEMPLATE_MESSAGE_PLACEHOLDER = "message";
    public static final String TEMPLATE_CODE_PLACEHOLDER = "code";
    public static final String DEFAULT_CODE = "500";

    /** hibernate validator 的快速失败配置，默认为true */
    private boolean validationFastFail = true;
    /** 异常的通常有多层，可能最外层并不是我们想要判断的异常，所以需要往下寻找，此参数表示最多往下寻找几层，默认 5 层 */
    private int depth = 5;
    /** 异常消息模板，遵循 SpEL 语法，使用 message 表示使用异常的 e.getMessage()，使用 code 表示使用配置中的 code。默认：{"code":#{code},"message":"#{message}"} */
    private String template = "{\"code\":#{code},\"message\":\"#{message}\"}";
    /** 默认异常响应 */
    private String defaultMessage = "{\"code\":500,\"message\":\"服务异常\"}";
    /** web中使用validator注解校验失败后的code值，默认400 */
    private String validationFailCode = "400";
    /** 需要拦截的异常类，默认仅拦截 com.dtsx.common.api.exception.BizException. 如果需要拦截其他的异常类需要自行配置 */
    private List<ExceptionResponseConfig> exceptions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor(staticName = "of")
    public static class ExceptionResponseConfig {
        /** 异常类型 */
        @NotNull
        private Class<? extends Throwable> exception;
        /** 异常code */
        @NotBlank
        private String code;
        /** 异常消息 */
        private String message;
        /** 是否转换为 BizException. 转换为 BizException 后会在后续的filter中抛出，日志中有异常栈信息. 一般类似 MethodArgumentTypeMismatchException 这种填充request参数引起异常是不需要转换的 */
        private boolean convertToBizException = false;
    }
}

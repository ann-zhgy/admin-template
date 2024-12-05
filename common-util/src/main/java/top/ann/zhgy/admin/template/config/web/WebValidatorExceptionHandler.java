package top.ann.zhgy.admin.template.config.web;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.expression.MapAccessor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.ann.zhgy.admin.template.config.properties.WebExceptionProperties;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.utils.WebExceptionUtil;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * web层hibernate校验异常处理
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/19 14:52
 */
@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = {RestController.class})
public class WebValidatorExceptionHandler {
    private final WebExceptionProperties properties;
    private Map<Class<? extends Throwable>, WebExceptionProperties.ExceptionResponseConfig> exceptionResponseConfigMap;

    @ConditionalOnClass(MethodArgumentNotValidException.class)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct().collect(Collectors.joining("; "));
        return buildErrorResponse(properties.getValidationFailCode(), errorMessage);
    }

    @ConditionalOnClass(BindException.class)
    @ExceptionHandler(BindException.class)
    public String bindExceptionHandle(BindException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct().collect(Collectors.joining("; "));
        return buildErrorResponse(properties.getValidationFailCode(), errorMessage);
    }

    @ConditionalOnClass(ConstraintViolationException.class)
    @ExceptionHandler(ConstraintViolationException.class)
    public String constraintViolationExceptionHandle(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .distinct().collect(Collectors.joining("; "));
        return buildErrorResponse(properties.getValidationFailCode(), errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public String otherExceptionHandle(Exception ex) throws Exception {
        if (ex instanceof BizException bizException) {
            throw bizException;
        }
        WebExceptionProperties.ExceptionResponseConfig exceptionResponseConfig = getExceptionResponseConfigMap().get(ex.getClass());
        if (exceptionResponseConfig == null) {
            throw ex;
        }
        if (exceptionResponseConfig.isConvertToBizException()) {
            throw new BizException(exceptionResponseConfig.getCode(), exceptionResponseConfig.getMessage(), ex);
        }
        return WebExceptionUtil.buildExceptionResponse(
                properties.getTemplate(), properties.getDefaultMessage(),
                exceptionResponseConfig.getCode(),
                Optional.ofNullable(exceptionResponseConfig.getMessage()).orElse(ex.getMessage()));
    }

    private String buildErrorResponse(String errorCode, String errorMessage) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Map<String, String> exContext = Map.of(
                WebExceptionProperties.TEMPLATE_CODE_PLACEHOLDER, errorCode,
                WebExceptionProperties.TEMPLATE_MESSAGE_PLACEHOLDER, errorMessage);
        StandardEvaluationContext context = new StandardEvaluationContext(exContext);
        // 添加对 map 的支持，但是当map中不存在key的时候，依旧会报错，所以还是建议尽量别用map
        context.addPropertyAccessor(new MapAccessor());
        try {
            Object contextResult = parser.parseExpression(properties.getTemplate(), new TemplateParserContext()).getValue(context);
            return Optional.ofNullable(contextResult).map(Object::toString).orElse(properties.getDefaultMessage());
        } catch (EvaluationException | ParseException e) {
            return properties.getDefaultMessage();
        }
    }

    private Map<Class<? extends Throwable>, WebExceptionProperties.ExceptionResponseConfig> getExceptionResponseConfigMap() {
        if (exceptionResponseConfigMap == null) {
            if (CollectionUtils.isEmpty(properties.getExceptions())) {
                exceptionResponseConfigMap = Collections.emptyMap();
            } else {
                exceptionResponseConfigMap = properties.getExceptions().stream()
                        .filter(item -> Objects.nonNull(item.getException()) && StringUtils.isNotBlank(item.getCode()))
                        .collect(Collectors.toMap(WebExceptionProperties.ExceptionResponseConfig::getException, Function.identity(), (a, b) -> a));
            }
        }
        return exceptionResponseConfigMap;
    }
}

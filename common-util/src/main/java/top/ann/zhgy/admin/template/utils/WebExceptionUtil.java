package top.ann.zhgy.admin.template.utils;

import lombok.experimental.UtilityClass;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.ParseException;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import top.ann.zhgy.admin.template.config.properties.WebExceptionProperties;

import java.util.Map;
import java.util.Optional;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/10/17 16:49
 */
@UtilityClass
public class WebExceptionUtil {
    public static String buildExceptionResponse(String responseTemplate, String defaultResponse, String code, String message) {
        SpelExpressionParser parser = new SpelExpressionParser();
        Map<String, String> exContext = Map.of(
                WebExceptionProperties.TEMPLATE_CODE_PLACEHOLDER, code,
                WebExceptionProperties.TEMPLATE_MESSAGE_PLACEHOLDER, message);
        StandardEvaluationContext context = new StandardEvaluationContext(exContext);
        // 添加对 map 的支持，但是当map中不存在key的时候，依旧会报错，所以还是建议尽量别用map
        context.addPropertyAccessor(new MapAccessor());
        try {
            Object contextResult = parser.parseExpression(responseTemplate, new TemplateParserContext()).getValue(context);
            return Optional.ofNullable(contextResult).map(Object::toString).orElse(defaultResponse);
        } catch (EvaluationException | ParseException e) {
            return defaultResponse;
        }
    }
}

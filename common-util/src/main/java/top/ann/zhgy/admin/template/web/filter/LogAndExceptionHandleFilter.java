package top.ann.zhgy.admin.template.web.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;
import top.ann.zhgy.admin.template.config.properties.WebExceptionProperties;
import top.ann.zhgy.admin.template.constant.CommonConstant;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.utils.JsonUtil;
import top.ann.zhgy.admin.template.utils.WebExceptionUtil;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * controller 层打印日志的filter
 *
 * @author zhangaoyu@workatdata.com
 * @since 2024-4-8 10:09:44
 */
@Slf4j
@AllArgsConstructor
public class LogAndExceptionHandleFilter extends OncePerRequestFilter implements Ordered {
    private WebExceptionProperties properties;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        if (!isRequestValid(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        RequestWrapper requestWrapper = new RequestWrapper(request);
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        try {
            log.info("remote host address: {}", request.getRemoteHost());
            log.info("request uri: {}, requestId: {}, request method: {}, request param: {}, request body: {}",
                    requestWrapper.getRequestURI(), requestWrapper.getRequestId(), requestWrapper.getMethod(),
                    JsonUtil.toJson(requestWrapper.getParameterMap()), requestWrapper.getRequestBody());
            long startTime = System.currentTimeMillis();
            response.addHeader(CommonConstant.REQUEST_ID_HEADER_NAME, requestWrapper.getRequestId());
            filterChain.doFilter(requestWrapper, response);
            log.info("request uri: {}, requestId: {}, response status: {}, responseTime: {}, response body: {}",
                    requestWrapper.getRequestURI(), requestWrapper.getRequestId(), response.getStatus(),
                    System.currentTimeMillis() - startTime, getResponseBody(response));
        } catch (Exception e) {
            log.error("request uri: {}. ex: {}", request.getRequestURI(), getMessage(e), e);
            String errorResponse = parseErrorResponseFromException(e);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(errorResponse.toCharArray());
        } finally {
            updateResponse(response);
        }
    }

    private static String getMessage(Throwable cause) {
        if (cause.getClass().getName().equals("org.springframework.web.util.NestedServletException")) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

    /**
     * 通过异常获取exception的响应消息
     *
     * @param cause cause
     * @return 错误响应
     */
    private String parseErrorResponseFromException(Exception cause) {
        if (StringUtils.isBlank(properties.getTemplate())) {
            return properties.getDefaultMessage();
        }
        // 寻找我们自己定义的异常，最多寻找5层
        int currentDepth = 1;
        Throwable throwable = cause;
        do {
            if (throwable instanceof BizException bizException) {
                return WebExceptionUtil.buildExceptionResponse(
                        properties.getTemplate(), properties.getDefaultMessage(),
                        StringUtils.isBlank(bizException.getCode()) ? WebExceptionProperties.DEFAULT_CODE : bizException.getCode(),
                        bizException.getMessage());
            }
            if (Objects.isNull(throwable = throwable.getCause())) {
                break;
            }
            currentDepth++;
        } while (currentDepth < properties.getDepth());
        return properties.getDefaultMessage();
    }

    /**
     * 判断请求是否合法
     *
     * @param request request
     * @return true|false
     */
    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    /**
     * 获取响应体
     *
     * @param response response
     * @return 响应体
     */
    private String getResponseBody(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (Objects.isNull(wrapper)) {
            return "";
        }
        String responseContent = new String(wrapper.getContentAsByteArray(), StandardCharsets.UTF_8);
        if (responseContent.length() < 300) {
            return responseContent;
        }
        return "response too long... " + responseContent.substring(0, 300);
    }

    /**
     * 响应后处理：将缓存中的响应正文复制到响应中
     *
     * @param response response
     * @throws IOException IOException
     */
    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }
}

package life.klstoys.admin.template.simple.web.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.simple.constant.AdminTemplateConstant;
import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.simple.dal.repository.RedisRepository;
import life.klstoys.admin.template.simple.exceptions.SimpleExceptionEnum;
import life.klstoys.admin.template.simple.web.context.RequestContext;
import life.klstoys.admin.template.utils.ServletRequestUtil;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

/**
 * token登陆拦截器
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/18 12:45
 */
public class LoginAndAuthorityInterceptor implements HandlerInterceptor {
    @Setter(onMethod_ = @Autowired)
    private RedisRepository redisRepository;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) throws Exception {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        String token = request.getHeader(AdminTemplateConstant.HEADER_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            throw new BizException(SimpleExceptionEnum.TOKEN_IS_BLANK);
        }
        UserDO userInfo = redisRepository.getToken(token);
        if (Objects.isNull(userInfo)) {
            redisRepository.removeToken(token);
            throw new BizException(SimpleExceptionEnum.TOKEN_INVALID);
        }
        try {
            RequestContext.setToken(token);
            RequestContext.setRemoteIp(ServletRequestUtil.getHttpRequestRemoteIp(request));
            RequestContext.setUserInfo(userInfo);
            return true;
        } catch (Exception e) {
            redisRepository.removeToken(token);
            throw new BizException(SimpleExceptionEnum.TOKEN_INVALID);
        }
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) throws Exception {
        RequestContext.clear();
    }
}

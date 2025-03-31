package life.klstoys.admin.template.rbac.web.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.constant.AdminTemplateConstant;
import life.klstoys.admin.template.rbac.dal.repository.RedisRepository;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import life.klstoys.admin.template.rbac.web.context.RequestContext;
import life.klstoys.admin.template.utils.CommonUtil;
import life.klstoys.admin.template.utils.ServletRequestUtil;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
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
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new BizException(RbacExceptionEnum.TOKEN_IS_BLANK);
        }
        UserAuthorInfoDO userInfo = redisRepository.getToken(token);
        if (Objects.isNull(userInfo)) {
            redisRepository.removeToken(token);
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            throw new BizException(RbacExceptionEnum.TOKEN_INVALID);
        }
        // todo: 测试代码，系统数据构建完毕之后需删除
        if (Objects.equals(userInfo.getUsername(), "admin")) {
            RequestContext.setToken(token);
            RequestContext.setRemoteIp(ServletRequestUtil.getHttpRequestRemoteIp(request));
            RequestContext.setUserInfo(userInfo);
            return true;
        }
        if (CollectionUtils.isEmpty(userInfo.getAuthorizedUrls())
            || !CommonUtil.matchAuthorizedUrl(userInfo.getAuthorizedUrls(), request.getMethod(), request.getRequestURI())) {
//            response.setStatus(HttpStatus.FORBIDDEN.value());
            throw new BizException(RbacExceptionEnum.NO_PERMISSION);
        }
        try {
            RequestContext.setToken(token);
            RequestContext.setRemoteIp(ServletRequestUtil.getHttpRequestRemoteIp(request));
            RequestContext.setUserInfo(userInfo);
            return true;
        } catch (Exception e) {
            redisRepository.removeToken(token);
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
            throw new BizException(RbacExceptionEnum.TOKEN_INVALID);
        }
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) throws Exception {
        RequestContext.clear();
    }
}

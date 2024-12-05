package top.ann.zhgy.admin.template.web.filter;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

/**
 * 当没有账号体系时，可以使用此类作为默认账号登录
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/12 17:52
 */
@Slf4j
public class AuthenticateFilter extends OncePerRequestFilter implements Ordered {
    private static final String FORM_USERNAME_KEY = "username";
    private static final String FORM_PASSWORD_KEY = "password";
    private static final String TOKEN_KEY = "authorization";

    private final String username;
    private final String password;
    private final String authHeaderValue;

    public AuthenticateFilter(String username, String password) {
        this.username = username;
        this.password = password;
        this.authHeaderValue = String.format("Basic %s", Base64.getEncoder().encodeToString(username.concat(":").concat(password).getBytes()));
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE - 10;
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(TOKEN_KEY);
        if (StringUtils.isNotBlank(authorization)) {
            if (Objects.equals(authorization, authHeaderValue)) {
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            String username = request.getParameter(FORM_USERNAME_KEY);
            String password = request.getParameter(FORM_PASSWORD_KEY);
            if (Objects.equals(username, this.username) && Objects.equals(password, this.password)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.flushBuffer();
    }
}

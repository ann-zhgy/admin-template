package life.klstoys.admin.template.rbac.web.context;

import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;

/**
 * 请求上下文
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/18 17:52
 */
public class RequestContext {
    private static final ThreadLocal<RequestContextContent> THREAD_LOCAL = ThreadLocal.withInitial(RequestContextContent::new);

    public static String getToken() {
        return THREAD_LOCAL.get().getToken();
    }

    public static void setToken(String token) {
        THREAD_LOCAL.get().setToken(token);
    }

    public static String getRemoteIp() {
        return THREAD_LOCAL.get().getRemoteIp();
    }

    public static void setRemoteIp(String remoteIp) {
        THREAD_LOCAL.get().setRemoteIp(remoteIp);
    }

    public static void setUserInfo(UserAuthorInfoDO userDO) {
        THREAD_LOCAL.get().setUserInfo(userDO);
    }

    public static UserAuthorInfoDO getUserInfo() {
        return THREAD_LOCAL.get().getUserInfo();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}

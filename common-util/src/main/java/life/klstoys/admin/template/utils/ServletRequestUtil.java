package life.klstoys.admin.template.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/8/7 14:07
 */
@UtilityClass
public class ServletRequestUtil {
    public static String getHttpRequestRemoteIp(HttpServletRequest request) {
        String ipAddresses = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }
        if (StringUtils.isBlank(ipAddresses)) {
            return null;
        }
        return ipAddresses.split(",")[0];
    }
}

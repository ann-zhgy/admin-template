package life.klstoys.admin.template.rbac.constant;

import life.klstoys.admin.template.enums.RequestMethodEnum;
import life.klstoys.admin.template.utils.CommonUtil;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/26 15:23
 */
public interface AdminTemplateConstant {
    String HEADER_TOKEN_KEY = "token";
    /** 用于基础鉴权的请求信息 */
    List<String> BASIC_AUTHORIZED_URLS = List.of(
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/base-info"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/logout"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/update-password"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.PUT, "/user"));
    /** 账号登录后的可访问的基础URL */

    List<String> LOGIN_ACCOUNT_BASIC_ACCESS_URLS = List.of(
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/base-info"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.PUT, "/user"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/logout"),
            CommonUtil.buildAuthorizedUrl(RequestMethodEnum.POST, "/user/update-password")
    );
    /** 异步任务默认超时时间 */
    int ASYNC_TASK_TIMEOUT = 15;
    /** 页面访问权限功能组名称 */
    String PAGE_ACCESS_AUTH_GROUP_NAME = "页面访问权限: ";
    /** 登录用户基础鉴权功能组编号 */
    String LOGIN_USER_BASIC_AUTH = "202501201042364192c373fc0";
    String HEADER_APPKEY_KEY = "appkey";
}

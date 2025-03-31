package life.klstoys.admin.template.simple.service;

import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.simple.entity.UserInfoEntity;
import life.klstoys.admin.template.simple.web.controller.request.RegisterRequest;
import life.klstoys.admin.template.simple.web.controller.request.SendCaptchaRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserListRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdatePasswordRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdatePermissionRequest;
import life.klstoys.admin.template.simple.web.controller.request.UserUpdateRequest;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 12:28
 */
public interface UserService {

    /**
     * 注册账号
     *
     * @param request 注册需要的信息
     */
    void register(RegisterRequest request);

    /**
     * 通过密码登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String loginByPassword(String username, String password);

    /**
     * 通过验证码登录
     *
     * @param username 用户名
     * @param captcha  验证码
     * @return token
     */
    String loginByCaptcha(String username, String captcha);

    /**
     * 发送验证码
     *
     * @param request request
     */
    void sendCaptcha(SendCaptchaRequest request);

    /**
     * 获取当前登录账号的基本信息
     *
     * @return userInfo
     */
    UserInfoEntity baseInfo();

    /**
     * 更新账号信息
     *
     * @param request request
     */
    void update(UserUpdateRequest request);

    /**
     * 获取账号列表
     *
     * @param request request
     * @return 账号列表
     */
    PageResult<UserInfoEntity> list(UserListRequest request);

    /**
     * 根据id查询账号信息
     *
     * @param id id
     * @return 账号信息
     */
    UserInfoEntity query(Long id);

    /**
     * 禁用账号
     *
     * @param id id
     */
    void disable(Long id);

    /**
     * 启用账号
     *
     * @param id id
     */
    void enable(Long id);

    /**
     * 更新账号权限
     *
     * @param request request
     */
    void updatePermission(UserUpdatePermissionRequest request);

    /**
     * 登出
     */
    void logout();

    /**
     * 修改密码
     *
     * @param request request
     */
    void updatePassword(UserUpdatePasswordRequest request);
}

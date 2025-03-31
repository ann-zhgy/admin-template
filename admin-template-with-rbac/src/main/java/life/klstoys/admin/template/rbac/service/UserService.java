package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.LoginPasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.RegisterRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.SendCaptchaRequest;
import life.klstoys.admin.template.rbac.web.controller.request.author.UpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdatePasswordRequest;
import life.klstoys.admin.template.rbac.web.controller.request.user.UserUpdateRequest;
import life.klstoys.admin.template.rbac.web.controller.response.UserRoleResponse;

import java.util.List;
import java.util.Set;

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
     * @param request request
     * @return token
     */
    String loginByPassword(LoginPasswordRequest request);

    /**
     * 通过验证码登录
     *
     * @param request request
     * @return token
     */
    String loginByCaptcha(LoginCaptchaRequest request);

    /**
     * 发送验证码
     *
     * @param request request
     */
    void sendCaptcha(SendCaptchaRequest request);

    /**
     * 获取当前登录账号的基本信息
     *
     * @param queryMenus 是否查询菜单
     * @param appKey     appKey
     * @return userInfo
     */
    UserInfoEntity baseInfo(boolean queryMenus, String appKey);

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
     * 登出
     */
    void logout();

    /**
     * 修改密码
     *
     * @param request request
     */
    void updatePassword(UserUpdatePasswordRequest request);

    /**
     * 绑定角色
     *
     * @param id      用户id
     * @param roleIds 角色id
     */
    void bindRole(Long id, Set<Long> roleIds);

    /**
     * 解绑角色
     *
     * @param id      用户id
     * @param roleIds 角色id
     */
    void unbindRole(Long id, Set<Long> roleIds);

    /**
     * 查询用户角色
     *
     * @param userId 用户id
     * @return 用户角色
     */
    List<UserRoleResponse> queryUserRoles(Long userId);

    /**
     * 修改密码（不登录）
     *
     * @param request request
     */
    void updatePasswordNoLogin(UpdatePasswordRequest request);

    /**
     * 查询用户指定应用的角色
     *
     * @param userId  用户id
     * @param appKey  应用key
     * @param request request
     * @return 角色
     */
    PageResult<RoleInfoEntity> queryUserAppRoles(Long userId, String appKey, PageRequest request);

    /**
     * 绑定应用
     *
     * @param userId  用户id
     * @param appKeys appkey
     */
    void bindApp(Long userId, Set<String> appKeys);

    /**
     * 解绑应用
     *
     * @param userId  用户id
     * @param appKeys appkey
     */
    void unbindApp(Long userId, Set<String> appKeys);

    /**
     * 刷新用户缓存
     *
     * @param userIds userIds
     */
    void refreshUserCache(Set<Long> userIds);

    /**
     * 查询用户权限信息
     *
     * @param userId userId
     * @param appKey appKey
     * @return 用户权限信息
     */
    UserAuthorInfoDO queryUserAuthorInfo(Long userId, String appKey);
}

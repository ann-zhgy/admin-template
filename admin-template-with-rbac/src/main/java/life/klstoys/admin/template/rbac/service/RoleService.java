package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import life.klstoys.admin.template.rbac.entity.UserInfoEntity;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleSaveOrUpdateRequest;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:40
 */
public interface RoleService {
    /**
     * 根据id查询角色信息
     *
     * @param id id
     * @return 角色信息
     */
    RoleInfoEntity getRoleById(Long id);

    /**
     * 查询角色列表
     *
     * @param request request
     * @return 角色列表
     */
    PageResult<RoleInfoEntity> list(RoleListRequest request);

    /**
     * 保存角色
     *
     * @param request request
     */
    void save(RoleSaveOrUpdateRequest request);

    /**
     * 保存角色
     *
     * @param request request
     */
    void update(RoleSaveOrUpdateRequest request);

    /**
     * 删除角色
     *
     * @param id id
     */
    void deleteById(Long id);

    /**
     * 禁用角色
     *
     * @param id id
     */
    void disable(Long id);

    /**
     * 启用角色
     *
     * @param id id
     */
    void enable(Long id);

    /**
     * 判断指定角色下是否绑定有用户
     *
     * @param id id
     * @return true/false
     */
    Boolean existUser(Long id);

    /**
     * 查询角色下的用户列表
     *
     * @param roleId  roleNo
     * @param request request
     * @return 用户列表
     */
    PageResult<UserInfoEntity> queryRoleUsers(Long roleId, PageRequest request);
}

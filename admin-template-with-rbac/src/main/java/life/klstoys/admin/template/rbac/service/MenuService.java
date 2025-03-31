package life.klstoys.admin.template.rbac.service;

import life.klstoys.admin.template.common.response.PageResult;
import life.klstoys.admin.template.rbac.entity.EndPointMenuInfoEntity;
import life.klstoys.admin.template.rbac.entity.MenuInfoEntity;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuListRequest;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuSaveOrUpdateRequest;

import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 12:21
 */
public interface MenuService {
    /**
     * 查询指定 appKey 下指定用户的菜单信息
     *
     * @param username 用户名
     * @param appKey   appKey
     * @return 菜单信息
     */
    List<EndPointMenuInfoEntity> queryUserMenus(String username, String appKey);

    /**
     * 查询菜单详情
     *
     * @param id id
     * @return 菜单信息
     */
    MenuInfoEntity getMenuById(Long id);

    /**
     * 查询菜单列表
     *
     * @param request request
     * @return 菜单列表
     */
    PageResult<MenuInfoEntity> list(MenuListRequest request);

    /**
     * 新增菜单
     *
     * @param request request
     */
    void save(MenuSaveOrUpdateRequest request);

    /**
     * 更新菜单
     *
     * @param request request
     */
    void update(MenuSaveOrUpdateRequest request);

    /**
     * 删除菜单
     *
     * @param id id
     */
    void deleteById(Long id);

    /**
     * 禁用菜单
     *
     * @param id id
     */
    void disable(Long id);

    /**
     * 启用菜单
     *
     * @param id id
     */
    void enable(Long id);

    /**
     * 通过appKey查询菜单，返回树形结构
     *
     * @param appKey appKey
     * @return 菜单信息
     */
    List<MenuInfoEntity> getMenuByAppKey(String appKey);

    /**
     * 判断菜单是否是叶子结点
     *
     * @param id id
     */
    Boolean menuIsLeafNode(Long id);
}

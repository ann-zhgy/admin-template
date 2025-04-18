package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAppKeyDO;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuListRequest;

import java.util.List;
import java.util.Set;

public interface FrontendPageMapper extends WithBatchInsertBaseMapper<FrontendPageDO> {
    /**
     * 查询所有叶子节点
     *
     * @param request request
     * @return List<FrontendPageDO>
     */
    List<FrontendPageDO> selectLeafMenus(MenuListRequest request);

    /**
     * 查询所有非叶子节点
     *
     * @param request request
     * @return List<FrontendPageDO>
     */
    List<FrontendPageDO> selectNoLeafMenus(MenuListRequest request);

    /**
     * 根据菜单编号查询用户ID
     */
    Set<UserAppKeyDO> selectUserIdsByMenuNo(String menuNo);
}
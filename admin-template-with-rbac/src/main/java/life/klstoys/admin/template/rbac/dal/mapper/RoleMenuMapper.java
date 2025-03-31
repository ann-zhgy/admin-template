package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.RoleMenuDO;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorMenuDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuMapper extends WithBatchInsertBaseMapper<RoleMenuDO> {
    List<UserAuthorMenuDO> selectByUsernameAndAppKey(@Param("username") String username, @Param("appKey") String appKey);

    void deleteByList(@Param("appKey") String appKey, @Param("list") List<RoleMenuDO> list);
}
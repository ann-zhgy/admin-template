package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface RoleMapper extends WithBatchInsertBaseMapper<RoleDO> {
    List<RoleDO> selectByUsernameAndAppkey(@Param("username") String username, @Param("appkey") String appkey);

    Set<String> queryRoleAppkeyByUsername(@Param("username") String username);
}
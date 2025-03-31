package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends WithBatchInsertBaseMapper<UserDO> {
    List<UserDO> queryByRoleId(@Param("roleNo") String roleNo);
}
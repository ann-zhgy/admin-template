package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.UserRoleDO;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper extends WithBatchInsertBaseMapper<UserRoleDO> {
    long countEnableByRole(@Param("roleNo") String roleNo);
}
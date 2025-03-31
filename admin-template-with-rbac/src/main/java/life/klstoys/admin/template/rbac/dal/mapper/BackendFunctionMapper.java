package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface BackendFunctionMapper extends WithBatchInsertBaseMapper<BackendFunctionDO> {
    List<BackendFunctionDO> queryAuthorizedFunctions(@Param("username") String username, @Param("appKey") String appKey);

    List<BackendFunctionDO> selectByGroupNo(@Param("groupNo") String groupNo);

    Set<Long> selectUserIdsByFunctionNo(@Param("functionNo") String functionNo);
}
package life.klstoys.admin.template.rbac.dal.mapper;

import life.klstoys.admin.template.config.mybatis.plus.WithBatchInsertBaseMapper;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;

import java.util.List;

public interface AppInfoMapper extends WithBatchInsertBaseMapper<AppInfoDO> {
    List<AppInfoDO> selectByUsername(String username);
}
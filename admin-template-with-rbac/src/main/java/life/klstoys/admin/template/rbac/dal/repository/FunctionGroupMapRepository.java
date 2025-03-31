package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupMapDO;
import life.klstoys.admin.template.rbac.dal.mapper.FunctionGroupMapMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 10:30
 */
@Component
public class FunctionGroupMapRepository extends BaseRepository<FunctionGroupMapMapper, FunctionGroupMapDO> {
    public void deleteByGroupNo(String groupNo) {
        if (StringUtils.isBlank(groupNo)) {
            return;
        }
        QueryWrapper<FunctionGroupMapDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupMapDO::getGroupNo, groupNo);
        getBaseMapper().delete(queryWrapper);
    }

    public Set<String> selectFunctionNosByGroupNo(String groupNo) {
        if (StringUtils.isBlank(groupNo)) {
            return Collections.emptySet();
        }
        QueryWrapper<FunctionGroupMapDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupMapDO::getGroupNo, groupNo);
        return getBaseMapper().selectList(queryWrapper).stream()
                .map(FunctionGroupMapDO::getBackendFunctionNo)
                .collect(Collectors.toSet());
    }
}

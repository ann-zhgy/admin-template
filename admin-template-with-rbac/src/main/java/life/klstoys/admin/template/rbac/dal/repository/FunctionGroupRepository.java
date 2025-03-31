package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.FunctionGroupDO;
import life.klstoys.admin.template.rbac.dal.mapper.FunctionGroupMapper;
import life.klstoys.admin.template.rbac.web.controller.request.function.group.FunctionGroupListRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 10:32
 */
@Component
public class FunctionGroupRepository extends BaseRepository<FunctionGroupMapper, FunctionGroupDO> {
    public List<FunctionGroupDO> list(FunctionGroupListRequest request) {
        QueryWrapper<FunctionGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(request.getNo()), FunctionGroupDO::getNo, request.getNo())
                .likeLeft(StringUtils.isNotBlank(request.getTitle()), FunctionGroupDO::getTitle, request.getTitle())
                .eq(StringUtils.isNotBlank(request.getAppKey()), FunctionGroupDO::getAppKey, request.getAppKey())
                .eq(Objects.nonNull(request.getGroupCallType()), FunctionGroupDO::getGroupCallType, request.getGroupCallType())
                .eq(Objects.nonNull(request.getStatus()), FunctionGroupDO::getStatus, request.getStatus())
                .orderByAsc(FunctionGroupDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<FunctionGroupDO> selectByAppKey(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return Collections.emptyList();
        }
        QueryWrapper<FunctionGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupDO::getAppKey, appKey);
        return getBaseMapper().selectList(queryWrapper);
    }

    public FunctionGroupDO selectByNo(String no) {
        if (StringUtils.isBlank(no)) {
            return null;
        }
        return getBaseMapper().selectOne(new QueryWrapper<FunctionGroupDO>().lambda().eq(FunctionGroupDO::getNo, no));
    }

    public void deleteByFrontendPageNo(String frontendPageNo) {
        if (StringUtils.isBlank(frontendPageNo)) {
            return;
        }
        QueryWrapper<FunctionGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupDO::getFrontendPageNo, frontendPageNo);
        getBaseMapper().delete(queryWrapper);
    }

    public List<FunctionGroupDO> selectByAppKeyAndNos(String appKey, Set<String> functionGroupNos) {
        if (StringUtils.isBlank(appKey) || CollectionUtils.isEmpty(functionGroupNos)) {
            return Collections.emptyList();
        }
        QueryWrapper<FunctionGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupDO::getAppKey, appKey).in(FunctionGroupDO::getNo, functionGroupNos);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<FunctionGroupDO> selectByPageNo(String frontendPageNo) {
        if (StringUtils.isBlank(frontendPageNo)) {
            return Collections.emptyList();
        }
        QueryWrapper<FunctionGroupDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FunctionGroupDO::getFrontendPageNo, frontendPageNo).orderByAsc(FunctionGroupDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }
}

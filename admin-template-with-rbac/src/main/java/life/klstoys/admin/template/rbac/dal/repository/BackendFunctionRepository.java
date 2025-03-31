package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.BackendFunctionDO;
import life.klstoys.admin.template.rbac.dal.mapper.BackendFunctionMapper;
import life.klstoys.admin.template.rbac.web.controller.request.function.FunctionListRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 16:15
 */
@Component
public class BackendFunctionRepository extends BaseRepository<BackendFunctionMapper, BackendFunctionDO> {
    public List<BackendFunctionDO> queryAuthorizedFunctions(String username, String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return Collections.emptyList();
        }
        return getBaseMapper().queryAuthorizedFunctions(username, appKey);
    }

    public List<BackendFunctionDO> selectByAppKey(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return Collections.emptyList();
        }
        QueryWrapper<BackendFunctionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BackendFunctionDO::getAppKey, appKey);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<BackendFunctionDO> list(FunctionListRequest request) {
        QueryWrapper<BackendFunctionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Objects.nonNull(request.getRequestMethod()), BackendFunctionDO::getRequestMethod, request.getRequestMethod())
                .eq(Objects.nonNull(request.getStatus()), BackendFunctionDO::getStatus, request.getStatus())
                .eq(StringUtils.isNotBlank(request.getAppKey()), BackendFunctionDO::getAppKey, request.getAppKey())
                .eq(StringUtils.isNotBlank(request.getNo()), BackendFunctionDO::getNo, request.getNo())
                .likeLeft(StringUtils.isNotBlank(request.getRequestUrl()), BackendFunctionDO::getRequestUrl, request.getRequestUrl())
                .likeLeft(StringUtils.isNotBlank(request.getTitle()), BackendFunctionDO::getTitle, request.getTitle())
                .orderByAsc(BackendFunctionDO::getAppKey, BackendFunctionDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }

    public List<BackendFunctionDO> selectByGroupNo(String no) {
        if (StringUtils.isBlank(no)) {
            return Collections.emptyList();
        }
        return getBaseMapper().selectByGroupNo(no);
    }

    public List<BackendFunctionDO> selectByAppKeyAndNos(String appKey, Set<String> functionNos) {
        if (CollectionUtils.isEmpty(functionNos) || StringUtils.isBlank(appKey)) {
            return Collections.emptyList();
        }
        QueryWrapper<BackendFunctionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BackendFunctionDO::getAppKey, appKey).in(BackendFunctionDO::getNo, functionNos);
        return getBaseMapper().selectList(queryWrapper);
    }

    public Set<Long> selectUserIdsByFunctionNo(String functionNo) {
        if (StringUtils.isBlank(functionNo)) {
            return Collections.emptySet();
        }
        return getBaseMapper().selectUserIdsByFunctionNo(functionNo);
    }
}

package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.FrontendPageDO;
import life.klstoys.admin.template.rbac.dal.mapper.FrontendPageMapper;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAppKeyDO;
import life.klstoys.admin.template.rbac.web.controller.request.menu.MenuListRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 17:56
 */
@Component
public class FrontendPageRepository extends BaseRepository<FrontendPageMapper, FrontendPageDO> {

    public List<FrontendPageDO> list(MenuListRequest request) {
        if (request.isOnlyLeafNode()) {
            return getBaseMapper().selectLeafMenus(request);
        }
        if (request.isNotLeafNode()) {
            return getBaseMapper().selectNoLeafMenus(request);
        }
        QueryWrapper<FrontendPageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(StringUtils.isNotBlank(request.getAppKey()), FrontendPageDO::getAppKey, request.getAppKey())
                .eq(StringUtils.isNotBlank(request.getNo()), FrontendPageDO::getNo, request.getNo())
                .likeLeft(StringUtils.isNotBlank(request.getTitle()), FrontendPageDO::getTitle, request.getTitle())
                .likeLeft(StringUtils.isNotBlank(request.getComponentKey()), FrontendPageDO::getComponentKey, request.getComponentKey())
                .eq(StringUtils.isNotBlank(request.getParentNo()), FrontendPageDO::getParentNo, request.getParentNo())
                .eq(Objects.nonNull(request.getStatus()), FrontendPageDO::getStatus, request.getStatus())
                .orderByAsc(FrontendPageDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }

    public FrontendPageDO selectByNo(String no) {
        if (StringUtils.isBlank(no)) {
            return null;
        }
        QueryWrapper<FrontendPageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FrontendPageDO::getNo, no);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public List<FrontendPageDO> selectByAppKey(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return Collections.emptyList();
        }
        QueryWrapper<FrontendPageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(FrontendPageDO::getAppKey, appKey);
        return getBaseMapper().selectList(queryWrapper);
    }

    public Set<UserAppKeyDO> selectUserIdsByMenuNo(String menuNo) {
        if (StringUtils.isBlank(menuNo)) {
            return Collections.emptySet();
        }
        return getBaseMapper().selectUserIdsByMenuNo(menuNo);
    }

    public List<FrontendPageDO> selectByNos(Collection<String> frontendPageNos) {
        if (CollectionUtils.isEmpty(frontendPageNos)) {
            return Collections.emptyList();
        }
        Set<String> frontendPageNoSet = new HashSet<>(frontendPageNos);
        QueryWrapper<FrontendPageDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(FrontendPageDO::getNo, frontendPageNoSet);
        return getBaseMapper().selectList(queryWrapper);
    }
}

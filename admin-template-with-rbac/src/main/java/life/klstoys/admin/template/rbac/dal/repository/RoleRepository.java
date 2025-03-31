package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.RoleDO;
import life.klstoys.admin.template.rbac.dal.mapper.RoleMapper;
import life.klstoys.admin.template.rbac.web.controller.request.role.RoleListRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 13:57
 */
@Component
public class RoleRepository extends BaseRepository<RoleMapper, RoleDO> {
    public List<RoleDO> queryByUsernameAndAppkey(String username, String appkey) {
        return getBaseMapper().selectByUsernameAndAppkey(username, appkey);
    }

    public List<RoleDO> list(RoleListRequest request) {
        if (Objects.isNull(request)) {
            return getBaseMapper().selectList(new QueryWrapper<>());
        }
        QueryWrapper<RoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(Objects.nonNull(request.getStatus()), RoleDO::getStatus, request.getStatus())
                .likeLeft(StringUtils.isNotBlank(request.getName()), RoleDO::getName, request.getName())
                .likeLeft(StringUtils.isNotBlank(request.getNameZh()), RoleDO::getNameZh, request.getNameZh())
                .eq(Objects.nonNull(request.getNo()), RoleDO::getNo, request.getNo())
                .eq(Objects.nonNull(request.getAppKey()), RoleDO::getAppKey, request.getAppKey())
                .orderByAsc(RoleDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }
}

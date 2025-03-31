package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.RoleMenuDO;
import life.klstoys.admin.template.rbac.dal.mapper.RoleMenuMapper;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorMenuDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 12:38
 */
@Component
public class RoleMenuRepository extends BaseRepository<RoleMenuMapper, RoleMenuDO> {
    public List<UserAuthorMenuDO> selectByUserIdAndAppKey(String username, String appKey) {
        return getBaseMapper().selectByUsernameAndAppKey(username, appKey);
    }

    public Set<String> selectByRoleNo(String roleNo) {
        if (StringUtils.isBlank(roleNo)) {
            return Collections.emptySet();
        }
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getRoleNo, roleNo);
        List<RoleMenuDO> roleMenuDOS = getBaseMapper().selectList(queryWrapper);
        return roleMenuDOS.stream().map(RoleMenuDO::getGroupNo).collect(Collectors.toSet());
    }

    public void deleteByRoleNo(String roleNo) {
        if (StringUtils.isBlank(roleNo)) {
            return;
        }
        QueryWrapper<RoleMenuDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(RoleMenuDO::getRoleNo, roleNo);
        getBaseMapper().delete(queryWrapper);
    }
}

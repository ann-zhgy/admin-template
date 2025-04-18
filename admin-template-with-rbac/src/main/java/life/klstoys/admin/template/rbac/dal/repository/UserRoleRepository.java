package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.exception.BizException;
import life.klstoys.admin.template.rbac.dal.domain.UserRoleDO;
import life.klstoys.admin.template.rbac.dal.mapper.UserRoleMapper;
import life.klstoys.admin.template.rbac.exceptions.RbacExceptionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 14:05
 */
@Slf4j
@Component
public class UserRoleRepository extends BaseRepository<UserRoleMapper, UserRoleDO> {
    public void deleteByUsernameAndRoleNos(String username, Set<String> existRoleNos) {
        if (StringUtils.isBlank(username) || CollectionUtils.isEmpty(existRoleNos)) {
            throw new BizException(RbacExceptionEnum.PARAM_INVALID, "传入参数为空");
        }
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getUsername, username).in(UserRoleDO::getRoleNo, existRoleNos);
        getBaseMapper().delete(queryWrapper);
    }

    public void deleteByRole(String roleNo) {
        if (StringUtils.isBlank(roleNo)) {
            return;
        }
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getRoleNo, roleNo);
        getBaseMapper().delete(queryWrapper);
    }

    public long countEnableByRole(String roleNo) {
        if (StringUtils.isBlank(roleNo)) {
            return 0;
        }
        return getBaseMapper().countEnableByRole(roleNo);
    }

    public Set<String> selectByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return Collections.emptySet();
        }
        QueryWrapper<UserRoleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserRoleDO::getUsername, username);
        List<UserRoleDO> userRoleDOS = getBaseMapper().selectList(queryWrapper);
        return userRoleDOS.stream().map(UserRoleDO::getRoleNo).collect(Collectors.toSet());
    }
}

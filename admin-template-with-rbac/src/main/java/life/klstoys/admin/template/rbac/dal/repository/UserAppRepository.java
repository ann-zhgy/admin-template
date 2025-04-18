package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.UserAppDO;
import life.klstoys.admin.template.rbac.dal.mapper.UserAppMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/3 12:25
 */
@Component
public class UserAppRepository extends BaseRepository<UserAppMapper, UserAppDO> {
    public void deleteByUsernameAndAppKeys(String username, Set<String> appKeys) {
        if (StringUtils.isBlank(username) || CollectionUtils.isEmpty(appKeys)) {
            return;
        }
        QueryWrapper<UserAppDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserAppDO::getUsername, username)
                .in(UserAppDO::getAppKey, appKeys);
        getBaseMapper().delete(queryWrapper);
    }

    public Set<String> selectByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return Collections.emptySet();
        }
        QueryWrapper<UserAppDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAppDO::getUsername, username);
        List<UserAppDO> userAppDOS = getBaseMapper().selectList(queryWrapper);
        return userAppDOS.stream().map(UserAppDO::getAppKey).collect(Collectors.toSet());
    }

    public boolean hasAppPermission(String username, String appKey) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(appKey)) {
            return false;
        }
        QueryWrapper<UserAppDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserAppDO::getUsername, username).eq(UserAppDO::getAppKey, appKey);
        return getBaseMapper().selectCount(queryWrapper) > 0;
    }
}

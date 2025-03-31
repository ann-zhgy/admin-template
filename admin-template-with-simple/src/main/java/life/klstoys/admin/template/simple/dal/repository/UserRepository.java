package life.klstoys.admin.template.simple.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.simple.dal.mapper.UserMapper;
import life.klstoys.admin.template.simple.web.controller.request.UserListRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:30
 */
@Component
public class UserRepository extends BaseRepository<UserMapper, UserDO> {
    public UserDO selectUserByMultiFlag(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getUsername, username)
                .or().eq(UserDO::getEmail, username)
                .or().eq(UserDO::getPhone, username);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public UserDO selectUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getUsername, username);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public UserDO selectUserByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getEmail, email);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public UserDO selectUserByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getPhone, phone);
        return getBaseMapper().selectOne(queryWrapper);
    }

    public List<UserDO> list(UserListRequest request) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isNotBlank(request.getPhone()), UserDO::getPhone, request.getPhone())
                .eq(StringUtils.isNotBlank(request.getUsername()), UserDO::getUsername, request.getUsername())
                .eq(StringUtils.isNotBlank(request.getEmail()), UserDO::getEmail, request.getEmail())
                .eq(Objects.nonNull(request.getStatus()), UserDO::getStatus, request.getStatus());
        return getBaseMapper().selectList(queryWrapper);
    }

    public UserDO selectUserById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getId, id);
        return getBaseMapper().selectOne(queryWrapper);
    }
}

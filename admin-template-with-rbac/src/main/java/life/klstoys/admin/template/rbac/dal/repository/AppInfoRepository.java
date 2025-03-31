package life.klstoys.admin.template.rbac.dal.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import life.klstoys.admin.template.config.mybatis.plus.BaseRepository;
import life.klstoys.admin.template.rbac.dal.domain.AppInfoDO;
import life.klstoys.admin.template.rbac.dal.mapper.AppInfoMapper;
import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import life.klstoys.admin.template.rbac.web.controller.request.app.info.AppInfoListRequest;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:16
 */
@Component
public class AppInfoRepository extends BaseRepository<AppInfoMapper, AppInfoDO> {
    @Setter(onMethod_ = @Autowired)
    private RedisRepository redisRepository;

    public List<AppInfoDO> list(AppInfoListRequest request) {
        QueryWrapper<AppInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ge(Objects.nonNull(request.getAppType()), AppInfoDO::getAppType, AppTypeEnum.toCode(request.getAppType()))
                .eq(Objects.nonNull(request.getStatus()), AppInfoDO::getStatus, request.getStatus())
                .eq(Objects.nonNull(request.getAccessControlBy()), AppInfoDO::getAccessControlBy, request.getAccessControlBy())
                .eq(Objects.nonNull(request.getGrantAccessPermissionBy()), AppInfoDO::getGrantAccessPermissionBy, request.getGrantAccessPermissionBy())
                .like(Objects.nonNull(request.getAppKey()), AppInfoDO::getAppKey, request.getAppKey())
                .like(Objects.nonNull(request.getAppName()), AppInfoDO::getAppName, request.getAppName())
                .orderByAsc(AppInfoDO::getId);
        return getBaseMapper().selectList(queryWrapper);
    }

    public AppInfoDO selectByAppKey(String appKey) {
        if (StringUtils.isBlank(appKey)) {
            return null;
        }
        return redisRepository.getCache(appKey, new TypeReference<>() {
        }, () -> {
            QueryWrapper<AppInfoDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(AppInfoDO::getAppKey, appKey);
            return getBaseMapper().selectOne(queryWrapper);
        });
    }

    public List<AppInfoDO> selectByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return Collections.emptyList();
        }
        return getBaseMapper().selectByUsername(username);
    }

    public List<AppInfoDO> selectByAppKeys(Set<String> appKeys) {
        if (CollectionUtils.isEmpty(appKeys)) {
            return Collections.emptyList();
        }
        QueryWrapper<AppInfoDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(AppInfoDO::getAppKey, appKeys);
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public int updateById(AppInfoDO entity) {
        redisRepository.removeCache(entity.getAppKey());
        return super.updateById(entity);
    }

    @Override
    public int deleteById(Serializable id) {
        AppInfoDO appInfoDO = selectById(id);
        if (Objects.isNull(appInfoDO)) {
            return 0;
        }
        redisRepository.removeCache(appInfoDO.getAppKey());
        return super.deleteById(id);
    }
}

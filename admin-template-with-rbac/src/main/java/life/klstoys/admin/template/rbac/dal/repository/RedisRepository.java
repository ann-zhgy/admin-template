package life.klstoys.admin.template.rbac.dal.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.support.domain.UserAuthorInfoDO;
import life.klstoys.admin.template.rbac.service.UserService;
import life.klstoys.admin.template.utils.JsonUtil;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:58
 */
@Component
public class RedisRepository {
    private static final int TOKEN_EXPIRE = 60 * 60 * 24 * 2;
    private static final int CAPTCHA_EXPIRE = 60 * 10;

    @Setter(onMethod_ = {@Autowired})
    private StringRedisTemplate redisTemplate;
    @Setter(onMethod_ = {@Autowired, @Lazy})
    private UserService userService;

    public <T> T getCache(String no, TypeReference<T> typeReference, Supplier<T> supplier) {
        String json = redisTemplate.opsForValue().get(buildCacheKey(no));
        if (StringUtils.isNotBlank(json)) {
            return JsonUtil.fromJson(json, typeReference);
        }
        T item = supplier.get();
        if (Objects.nonNull(item)) {
            redisTemplate.opsForValue().set(buildCacheKey(no), Objects.requireNonNull(JsonUtil.toJson(item)));
        }
        return item;
    }

    public void removeCache(String no) {
        redisTemplate.delete(buildCacheKey(no));
    }

    private String buildCacheKey(String key) {
        return "cache-temp:" + key;
    }

    public void setToken(String token, UserAuthorInfoDO loginInfo) {
        redisTemplate.opsForValue().set(buildTokenKey(token), Objects.requireNonNull(JsonUtil.toJson(loginInfo)), TOKEN_EXPIRE, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(buildTokenKey(String.valueOf(loginInfo.getId())), token, TOKEN_EXPIRE, TimeUnit.SECONDS);
    }

    public UserAuthorInfoDO getToken(String token) {
        String json = redisTemplate.opsForValue().get(buildTokenKey(token));
        UserAuthorInfoDO authorInfo = JsonUtil.fromJson(json, UserAuthorInfoDO.class);
        if (Objects.isNull(authorInfo)) {
            return null;
        }
        if (authorInfo.getStatus() == CommonStatusEnum.ENABLE) {
            return authorInfo;
        }
        try {
            UserAuthorInfoDO authorInfoDO = userService.queryUserAuthorInfo(authorInfo.getId(), authorInfo.getAppKey());
            if (Objects.isNull(authorInfoDO)) {
                removeToken(token);
                return null;
            }
            redisTemplate.opsForValue().set(buildTokenKey(token), Objects.requireNonNull(JsonUtil.toJson(authorInfoDO)), TOKEN_EXPIRE, TimeUnit.SECONDS);
            return authorInfoDO;
        } catch (Exception e) {
            return null;
        }
    }

    public void batchDisableToken(Set<Long> userIds) {
        userIds.forEach(userId -> {
            String token = redisTemplate.opsForValue().get(buildTokenKey(String.valueOf(userId)));
            if (StringUtils.isBlank(token)) {
                return;
            }
            String json = redisTemplate.opsForValue().get(buildTokenKey(token));
            UserAuthorInfoDO authorInfo = JsonUtil.fromJson(json, UserAuthorInfoDO.class);
            if (Objects.isNull(authorInfo)) {
                return;
            }
            authorInfo.setStatus(CommonStatusEnum.DISABLE);
            redisTemplate.opsForValue().set(buildTokenKey(token), Objects.requireNonNull(JsonUtil.toJson(authorInfo)), TOKEN_EXPIRE, TimeUnit.SECONDS);
        });
    }

    public void removeToken(String token) {
        UserAuthorInfoDO user = getToken(token);
        redisTemplate.delete(buildTokenKey(token));
        if (Objects.nonNull(user)) {
            redisTemplate.delete(buildTokenKey(String.valueOf(user.getId())));
        }
    }

    public void removeTokenById(long id) {
        String token = redisTemplate.opsForValue().get(buildTokenKey(String.valueOf(id)));
        redisTemplate.delete(buildTokenKey(token));
        redisTemplate.delete(buildTokenKey(String.valueOf(id)));
    }

    private String buildTokenKey(String token) {
        return "token:" + token;
    }

    public void setCaptcha(String captcha, String senderNo) {
        redisTemplate.opsForValue().set(buildCaptchaKey(captcha), senderNo, CAPTCHA_EXPIRE, TimeUnit.SECONDS);
    }

    public boolean checkCaptcha(String captcha, String senderNo) {
        String captchaInfo = redisTemplate.opsForValue().get(buildCaptchaKey(captcha));
        return Objects.equals(captchaInfo, senderNo);
    }

    public void removeCaptcha(String captcha) {
        redisTemplate.delete(buildCaptchaKey(captcha));
    }

    private String buildCaptchaKey(String captcha) {
        return "captcha:" + captcha;
    }
}

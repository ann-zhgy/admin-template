package life.klstoys.admin.template.simple.dal.repository;

import life.klstoys.admin.template.simple.dal.domain.UserDO;
import life.klstoys.admin.template.utils.JsonUtil;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    public void setToken(String token, UserDO userDO) {
        redisTemplate.opsForValue().set(buildTokenKey(token), Objects.requireNonNull(JsonUtil.toJson(userDO)), TOKEN_EXPIRE, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(buildTokenKey(String.valueOf(userDO.getId())), token, TOKEN_EXPIRE, TimeUnit.SECONDS);
    }

    public UserDO getToken(String token) {
        String json = redisTemplate.opsForValue().get(buildTokenKey(token));
        return JsonUtil.fromJson(json, UserDO.class);
    }

    public void removeToken(String token) {
        UserDO user = getToken(token);
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

    public void setCaptcha(String captcha) {
        redisTemplate.opsForValue().set(buildCaptchaKey(captcha), captcha, CAPTCHA_EXPIRE, TimeUnit.SECONDS);
    }

    public boolean checkCaptcha(String captcha) {
        String captchaOnRedis = redisTemplate.opsForValue().get(buildCaptchaKey(captcha));
        return Objects.equals(captchaOnRedis, captcha);
    }

    public void removeCaptcha(String captcha) {
        redisTemplate.delete(buildCaptchaKey(captcha));
    }

    private String buildCaptchaKey(String captcha) {
        return "captcha:" + captcha;
    }
}

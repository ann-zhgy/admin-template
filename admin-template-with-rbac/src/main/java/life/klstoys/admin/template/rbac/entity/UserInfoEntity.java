package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 12:59
 */
@Data
public class UserInfoEntity {
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private CommonStatusEnum status;

    /**
     * 菜单信息，用于登录后前端动态生成菜单
     */
    private List<EndPointMenuInfoEntity> menus;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

package top.ann.zhgy.admin.template.entity;

import lombok.Data;
import top.ann.zhgy.admin.template.enums.CommonStatusEnum;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;

import java.time.LocalDateTime;
import java.util.Set;

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
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 权限
     */
    private Set<UserPermissionEnum> permission;

    /**
     * 状态
     */
    private CommonStatusEnum status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

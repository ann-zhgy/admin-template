package life.klstoys.admin.template.simple.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.simple.dal.type.handler.UserPermissionsTypeHandler;
import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * 账号信息
 */
@Data
@TableName(value = "`user`", autoResultMap = true)
public class UserDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户名
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 手机号
     */
    @TableField(value = "phone", insertStrategy = FieldStrategy.NOT_EMPTY)
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email", insertStrategy = FieldStrategy.NOT_EMPTY)
    private String email;

    /**
     * 权限，使用位运算表示，1<<0-basic，1<<1-admin，1<<7-SuperAdmin
     */
    @TableField(value = "permission", typeHandler = UserPermissionsTypeHandler.class)
    private Set<UserPermissionEnum> permission;

    /**
     * 状态。1-使用中，2-已注销
     */
    @TableField(value = "`status`")
    private CommonStatusEnum status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;
}
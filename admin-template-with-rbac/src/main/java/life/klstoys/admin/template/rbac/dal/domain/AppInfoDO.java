package life.klstoys.admin.template.rbac.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.dal.type.handler.AppTypeEnumTypeHandler;
import life.klstoys.admin.template.rbac.enums.AccessControlEnum;
import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import life.klstoys.admin.template.rbac.enums.GrantAccessPermissionEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * 应用信息表
 */
@Data
@TableName(value = "app_info", autoResultMap = true)
public class AppInfoDO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * appKey
     */
    @TableField(value = "app_key")
    private String appKey;

    /**
     * app名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 权限控制方式。1-rbac服务控制，2-自己控制
     */
    @TableField(value = "access_control_by")
    private AccessControlEnum accessControlBy;

    /**
     * 访问权限授予方式。1-自动，2-手动
     */
    @TableField(value = "grant_access_permission_by")
    private GrantAccessPermissionEnum grantAccessPermissionBy;

    /**
     * app 类型。1-后端，2-web端，3-小程序，4-移动端安卓，5-移动端ios
     */
    @TableField(value = "app_type", typeHandler = AppTypeEnumTypeHandler.class)
    private Set<AppTypeEnum> appType;

    /**
     * 状态。1-使用中，2-已停用
     */
    @TableField(value = "status")
    private CommonStatusEnum status;

    /**
     * 创建人
     */
    @TableField(value = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "updater")
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
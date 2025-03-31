package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.AccessControlEnum;
import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import life.klstoys.admin.template.rbac.enums.GrantAccessPermissionEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 16:56
 */
@Data
public class AppInfoEntity {
    private Long id;

    /**
     * appKey
     */
    private String appKey;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 应用描述
     */
    private String description;

    /**
     * app 类型
     */
    private Set<AppTypeEnum> appType;

    /**
     * 权限控制方式
     */
    private AccessControlEnum accessControlBy;

    /**
     * 访问权限授予方式
     */
    private GrantAccessPermissionEnum grantAccessPermissionBy;

    /**
     * 状态
     */
    private CommonStatusEnum status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

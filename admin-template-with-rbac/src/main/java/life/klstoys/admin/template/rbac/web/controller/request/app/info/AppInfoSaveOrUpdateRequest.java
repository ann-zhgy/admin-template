package life.klstoys.admin.template.rbac.web.controller.request.app.info;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import life.klstoys.admin.template.rbac.enums.AccessControlEnum;
import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import life.klstoys.admin.template.rbac.enums.GrantAccessPermissionEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:05
 */
@Data
public class AppInfoSaveOrUpdateRequest {
    private Long id;

    /**
     * appKey
     */
    @NotBlank(message = "appKey不能为空")
    private String appKey;

    /**
     * 应用名
     */
    @NotBlank(message = "应用名不能为空")
    private String appName;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 权限控制方式。
     */
    @NotNull(message = "权限控制方式不能为空")
    private AccessControlEnum accessControlBy = AccessControlEnum.BY_RBAC;

    /**
     * 访问权限授予方式
     */
    @NotNull(message = "访问权限授予方式不能为空")
    private GrantAccessPermissionEnum grantAccessPermissionBy = GrantAccessPermissionEnum.BY_MANUAL;

    /**
     * app 类型
     */
    private Set<AppTypeEnum> appType;
}

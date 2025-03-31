package life.klstoys.admin.template.rbac.web.controller.request.app.info;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.AccessControlEnum;
import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import life.klstoys.admin.template.rbac.enums.GrantAccessPermissionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 17:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppInfoListRequest extends PageRequest {
    /**
     * appKey
     */
    private String appKey;

    /**
     * appName
     */
    private String appName;

    /**
     * 权限控制方式。
     */
    private AccessControlEnum accessControlBy;

    /**
     * 访问权限授予方式
     */
    private GrantAccessPermissionEnum grantAccessPermissionBy;

    /**
     * app 类型
     */
    private Set<AppTypeEnum> appType;

    /**
     * 状态
     */
    private CommonStatusEnum status;
}

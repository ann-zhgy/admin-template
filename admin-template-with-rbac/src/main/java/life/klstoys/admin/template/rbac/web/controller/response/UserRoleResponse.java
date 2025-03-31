package life.klstoys.admin.template.rbac.web.controller.response;

import life.klstoys.admin.template.rbac.entity.AppInfoEntity;
import life.klstoys.admin.template.rbac.entity.RoleInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * 用户角色响应信息
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/3 15:29
 */
@Data
public class UserRoleResponse {
    private AppInfoEntity appInfo;
    private List<RoleInfoEntity> roleInfos;
}

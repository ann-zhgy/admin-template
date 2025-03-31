package life.klstoys.admin.template.rbac.web.controller.request.role;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 14:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleListRequest extends PageRequest {
    /**
     * 角色编码
     */
    private String no;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色中文名称
     */
    private String nameZh;

    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;
}

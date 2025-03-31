package life.klstoys.admin.template.rbac.web.controller.request.function.group;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 12:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionGroupListRequest extends PageRequest {
    /**
     * 功能组编码
     */
    private String no;

    /**
     * 功能组标题
     */
    private String title;

    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 功能组类型。1-菜单，2-按钮或链接
     */
    private GroupCallTypeEnum groupCallType;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;
}

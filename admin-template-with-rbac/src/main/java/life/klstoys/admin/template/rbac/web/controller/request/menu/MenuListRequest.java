package life.klstoys.admin.template.rbac.web.controller.request.menu;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 16:38
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MenuListRequest extends PageRequest {
    /**
     * 菜单编码
     */
    private String no;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 前端组件key
     */
    private String componentKey;

    /**
     * 父级菜单编码
     */
    private String parentNo;

    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;

    /**
     * 是否只查询父级菜单
     */
    private boolean notLeafNode;

    /**
     * 是否只查询叶子节点
     */
    private boolean onlyLeafNode;
}

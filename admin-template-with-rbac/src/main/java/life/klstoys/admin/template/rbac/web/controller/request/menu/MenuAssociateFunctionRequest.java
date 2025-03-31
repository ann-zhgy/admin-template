package life.klstoys.admin.template.rbac.web.controller.request.menu;

import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 16:48
 */
@Data
public class MenuAssociateFunctionRequest {
    /** 功能信息 */
    private Set<FunctionGroupInfo> functionGroupInfos;

    @Data
    public static class FunctionGroupInfo {
        /** 功能id */
        private Long id;
        /** 组件类型 */
        private GroupCallTypeEnum groupCallType;
    }
}

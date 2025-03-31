package life.klstoys.admin.template.rbac.web.controller.request.function;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.enums.RequestMethodEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FunctionListRequest extends PageRequest {
    /**
     * 菜单编码
     */
    private String no;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 请求方法
     */
    private RequestMethodEnum requestMethod;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;
}

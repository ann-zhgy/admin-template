package life.klstoys.admin.template.simple.web.controller.request;

import life.klstoys.admin.template.common.request.PageRequest;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 13:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListRequest extends PageRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态
     */
    private CommonStatusEnum status;
}

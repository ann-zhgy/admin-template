package life.klstoys.admin.template.rbac.web.controller.request.function;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import life.klstoys.admin.template.enums.RequestMethodEnum;
import life.klstoys.admin.template.rbac.validate.annotation.AppKeyCheck;
import life.klstoys.admin.template.validator.group.Save;
import life.klstoys.admin.template.validator.group.Update;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:12
 */
@Data
public class FunctionSaveOrUpdateRequest {
    private Long id;

    /**
     * 菜单标题
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "标题不能为空")
    private String title;

    /**
     * 请求方法
     */
    @NotNull(groups = {Save.class, Update.class}, message = "请求方法不能为空")
    private RequestMethodEnum requestMethod;

    /**
     * 请求url
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "url不能为空")
    private String requestUrl;

    /**
     * 系统标识
     */
    @AppKeyCheck(groups = {Save.class, Update.class})
    @NotBlank(groups = {Save.class, Update.class}, message = "系统标识不能为空")
    private String appKey;
}

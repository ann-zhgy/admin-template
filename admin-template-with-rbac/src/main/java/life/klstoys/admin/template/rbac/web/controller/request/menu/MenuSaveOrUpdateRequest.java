package life.klstoys.admin.template.rbac.web.controller.request.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.validate.annotation.AppKeyCheck;
import life.klstoys.admin.template.validator.group.Save;
import life.klstoys.admin.template.validator.group.Update;
import lombok.Data;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 16:39
 */
@Data
public class MenuSaveOrUpdateRequest {
    private Long id;

    /**
     * 菜单标题
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "标题不能为空")
    private String title;

    /**
     * 前端组件key
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "前端组件key不能为空")
    private String componentKey;

    /**
     * 父级菜单编码
     */
    private String parentNo;

    /**
     * 系统标识
     */
    @AppKeyCheck(groups = {Save.class, Update.class})
    @NotBlank(groups = {Save.class, Update.class}, message = "系统标识不能为空")
    private String appKey;

    /**
     * 状态
     */
    @NotNull(groups = {Update.class}, message = "状态不能为空")
    private CommonStatusEnum status;
}

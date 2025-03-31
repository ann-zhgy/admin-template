package life.klstoys.admin.template.rbac.web.controller.request.function.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import life.klstoys.admin.template.validator.group.Save;
import life.klstoys.admin.template.validator.group.Update;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/11 15:12
 */
@Data
public class FunctionGroupSaveOrUpdateRequest {
    private Long id;

    /**
     * 功能组标题
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "标题不能为空")
    private String title;

    /**
     * 系统标识
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "appkey不能为空")
    private String appKey;

    /**
     * 前端页面编码
     */
    @NotBlank(groups = {Save.class, Update.class}, message = "父页面编码不能为空")
    private String frontendPageNo;

    /**
     * 功能组类型。1-菜单，2-按钮或链接
     */
    @NotNull(groups = {Save.class, Update.class}, message = "功能组类型不能为空")
    private GroupCallTypeEnum groupCallType;

    /**
     * 状态。1-使用中，2-已停用
     */
    @NotNull(groups = {Update.class}, message = "状态不能为空")
    private CommonStatusEnum status;

    /**
     * 函数信息
     */
    private Set<String> functionNos;
}

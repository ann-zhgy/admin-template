package life.klstoys.admin.template.rbac.web.controller.request.role;

import jakarta.validation.constraints.NotEmpty;
import life.klstoys.admin.template.rbac.validate.annotation.AppKeyCheck;
import life.klstoys.admin.template.validator.group.Save;
import life.klstoys.admin.template.validator.group.Update;
import lombok.Data;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 15:01
 */
@Data
public class RoleSaveOrUpdateRequest {
    private Long id;

    /**
     * 角色名称
     */
    @NotEmpty(groups = {Update.class, Save.class}, message = "角色名称不能为空")
    private String name;

    /**
     * 角色中文名称
     */
    @NotEmpty(groups = {Update.class, Save.class}, message = "角色中文名称不能为空")
    private String nameZh;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 系统标识
     */
    @AppKeyCheck(groups = {Save.class, Update.class})
    @NotEmpty(groups = {Save.class}, message = "系统标识不能为空")
    private String appKey;

    private Set<String> menuNos;
}

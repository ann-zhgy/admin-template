package life.klstoys.admin.template.rbac.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色用户关联表
 */
@Data
@TableName(value = "role_menu", autoResultMap = true)
public class RoleMenuDO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色编号
     */
    @TableField(value = "role_no")
    private String roleNo;

    /**
     * web组件编号
     */
    @TableField(value = "frontend_page_no")
    private String frontendPageNo;

    /**
     * 后端组编号
     */
    @TableField(value = "group_no")
    private String groupNo;

    /**
     * 系统标识
     */
    @TableField(value = "app_key")
    private String appKey;

    /**
     * 创建人
     */
    @TableField(value = "creator")
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "updater")
    private String updater;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
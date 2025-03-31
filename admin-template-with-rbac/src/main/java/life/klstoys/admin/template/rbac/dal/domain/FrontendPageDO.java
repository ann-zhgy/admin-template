package life.klstoys.admin.template.rbac.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 前端菜单及组件信息
 */
@Data
@TableName(value = "frontend_page", autoResultMap = true)
public class FrontendPageDO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 菜单编码
     */
    @TableField(value = "`no`")
    private String no;

    /**
     * 菜单标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 前端组件key
     */
    @TableField(value = "component_key")
    private String componentKey;

    /**
     * 父级菜单编码
     */
    @TableField(value = "parent_no")
    private String parentNo;

    /**
     * 系统标识
     */
    @TableField(value = "app_key")
    private String appKey;

    /**
     * 状态。1-使用中，2-已停用
     */
    @TableField(value = "`status`")
    private CommonStatusEnum status;

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
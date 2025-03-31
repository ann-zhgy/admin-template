package life.klstoys.admin.template.rbac.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import lombok.Data;

/**
 * 功能组，对应前端某个页面的功能需要使用到的后端接口，比如：新增、编辑等
 */
@Data
@TableName(value = "function_group", autoResultMap = true)
public class FunctionGroupDO implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 功能组编码
     */
    @TableField(value = "`no`")
    private String no;

    /**
     * 功能组标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 系统标识
     */
    @TableField(value = "app_key")
    private String appKey;

    /**
     * 前端页面编码
     */
    @TableField(value = "frontend_page_no")
    private String frontendPageNo;

    /**
     * 功能组类型。1-菜单，2-按钮或链接
     */
    @TableField(value = "group_call_type")
    private GroupCallTypeEnum groupCallType;

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
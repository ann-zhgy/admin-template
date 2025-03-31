package life.klstoys.admin.template.rbac.dal.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.enums.RequestMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后端功能信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "backend_function", autoResultMap = true)
public class BackendFunctionDO implements Serializable {
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
     * 请求方法。1-GET，2-POST，3-PUT，4-DELETE
     */
    @TableField(value = "request_method")
    private RequestMethodEnum requestMethod;

    /**
     * 请求url
     */
    @TableField(value = "request_url")
    private String requestUrl;

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
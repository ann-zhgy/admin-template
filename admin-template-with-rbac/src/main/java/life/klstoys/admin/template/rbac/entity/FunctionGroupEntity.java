package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.rbac.enums.GroupCallTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/17 12:04
 */
@Data
public class FunctionGroupEntity {
    private Long id;

    /**
     * 功能组编码
     */
    private String no;

    /**
     * 功能组标题
     */
    private String title;

    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 系统信息
     */
    private AppInfoEntity appInfo;

    /**
     * 前端页面编码
     */
    private String frontendPageNo;

    /**
     * 前端页面编码
     */
    private MenuInfoEntity frontendPage;

    /**
     * 功能组类型。1-菜单，2-按钮或链接
     */
    private GroupCallTypeEnum groupCallType;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;

    /**
     * 函数信息
     */
    private List<FunctionInfoEntity> functionInfos;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

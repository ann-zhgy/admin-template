package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 10:29
 */
@Data
public class RoleInfoEntity {
    private Long id;

    /**
     * 角色编码
     */
    private String no;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色中文名称
     */
    private String nameZh;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 所属系统标识
     */
    private String appKey;

    /**
     * 所属系统信息
     */
    private AppInfoEntity appInfo;

    /**
     * 菜单编号列表
     */
    private Set<String> menuNos;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;

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

package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 10:29
 */
@Data
public class MenuInfoEntity {
    private Long id;

    /**
     * 菜单编码
     */
    private String no;

    /**
     * 菜单标题
     */
    private String title;

    /**
     * 前端组件key
     */
    private String componentKey;

    /**
     * 父级菜单编码
     */
    private String parentNo;



    /**
     * 系统标识
     */
    private String appKey;

    /**
     * 所属系统信息
     */
    private AppInfoEntity appInfo;

    /**
     * 状态。1-使用中，2-已停用
     */
    private CommonStatusEnum status;

    /**
     * 功能信息
     */
    private List<MenuInfoEntity> childrenMenus;

    /**
     * 功能组信息
     */
    private List<FunctionGroupEntity> functionGroupInfos;

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

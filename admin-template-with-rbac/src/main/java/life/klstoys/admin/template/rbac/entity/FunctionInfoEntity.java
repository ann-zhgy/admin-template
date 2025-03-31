package life.klstoys.admin.template.rbac.entity;

import life.klstoys.admin.template.enums.CommonStatusEnum;
import life.klstoys.admin.template.enums.RequestMethodEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:18
 */
@Data
public class FunctionInfoEntity {
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
     * 请求方法。1-GET，2-POST，3-PUT，4-DELETE
     */
    private RequestMethodEnum requestMethod;

    /**
     * 请求url
     */
    private String requestUrl;

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

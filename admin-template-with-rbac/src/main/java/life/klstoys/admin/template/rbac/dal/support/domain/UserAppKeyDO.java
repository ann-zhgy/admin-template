package life.klstoys.admin.template.rbac.dal.support.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/4/9 15:05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UserAppKeyDO {
    private Long userId;
    private String appKey;
}

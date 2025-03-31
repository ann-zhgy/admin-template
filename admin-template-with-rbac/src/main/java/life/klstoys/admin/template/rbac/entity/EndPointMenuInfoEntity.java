package life.klstoys.admin.template.rbac.entity;

import lombok.Data;

import java.util.Set;

/**
 * 端点菜单权限信息
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/10 12:01
 */
@Data
public class EndPointMenuInfoEntity {
    private String key;
    private String title;
    private Set<String> innerComponentKeys;
}

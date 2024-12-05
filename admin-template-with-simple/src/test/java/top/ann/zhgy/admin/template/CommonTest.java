package top.ann.zhgy.admin.template;

import org.junit.jupiter.api.Test;
import top.ann.zhgy.admin.template.enums.UserPermissionEnum;

import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/1 20:58
 */
public class CommonTest {
    @Test
    public void test_json() {
        System.out.println(UserPermissionEnum.valuesFromPermissionCode(128));
        System.out.println(UserPermissionEnum.toPermissionCode(Set.of(UserPermissionEnum.ADMIN, UserPermissionEnum.SUPER_ADMIN, UserPermissionEnum.BASIC)));
    }
}

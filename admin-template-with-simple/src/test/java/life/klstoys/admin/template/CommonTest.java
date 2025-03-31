package life.klstoys.admin.template;

import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
import life.klstoys.admin.template.utils.CommonUtil;
import org.junit.jupiter.api.Test;

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

    @Test
    public void test_() {
        for (int i = 0; i < 100; i++) {
            System.out.println(CommonUtil.generateNo());
        }
    }
}

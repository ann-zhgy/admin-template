package top.ann.zhgy.admin.template.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.ann.zhgy.admin.template.common.BaseEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 08:53
 */
@Getter
@AllArgsConstructor
public enum UserPermissionEnum implements BaseEnum<String> {
    BASIC(1, "basic", "基础权限", 1, Collections.emptySet()),
    ADMIN(1 << 1, "admin", "管理员", 2, Set.of(BASIC)),
    SUPER_ADMIN(1 << 7, "super-admin", "超级管理员", 3, Set.of(ADMIN, BASIC)),
    ;
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;
    private final int weight;
    // 子级 权限
    private final Set<UserPermissionEnum> subPermissions;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static UserPermissionEnum valueOfEncode(String encode) {
        for (UserPermissionEnum value : UserPermissionEnum.values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }

    public static Set<UserPermissionEnum> valuesFromPermissionCode(Integer permissionCode) {
        if (permissionCode == null) {
            return Collections.emptySet();
        }
        Set<UserPermissionEnum> result = new HashSet<>();
        for (UserPermissionEnum value : UserPermissionEnum.values()) {
            if ((permissionCode & value.getCode()) == value.getCode()) {
                result.add(value);
            }
        }
        return result;
    }

    public static int toPermissionCode(Collection<UserPermissionEnum> permissions) {
        if (permissions == null) {
            return 0;
        }
        int result = 0;
        for (UserPermissionEnum permissionEnum : permissions) {
            result = permissionEnum.getCode() | result;
        }
        return result;
    }
}

package life.klstoys.admin.template.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import life.klstoys.admin.template.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/3 13:28
 */
@Getter
@AllArgsConstructor
public enum GrantAccessPermissionEnum implements BaseEnum<String> {
    BY_AUTO(1, "auto", "自动授予"),
    BY_MANUAL(2, "manual", "手动授予"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GrantAccessPermissionEnum valueOfEncode(String encode) {
        for (GrantAccessPermissionEnum value : values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}

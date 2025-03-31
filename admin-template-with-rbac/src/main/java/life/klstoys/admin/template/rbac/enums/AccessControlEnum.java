package life.klstoys.admin.template.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import life.klstoys.admin.template.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2025/1/3 10:02
 */
@Getter
@AllArgsConstructor
public enum AccessControlEnum implements BaseEnum<String> {
    BY_RBAC(1, "by-rbac", "通过rbac服务控制"),
    BY_SELF(2, "by-self", "自己控制"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AccessControlEnum valueOfEncode(String encode) {
        for (AccessControlEnum value : values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}
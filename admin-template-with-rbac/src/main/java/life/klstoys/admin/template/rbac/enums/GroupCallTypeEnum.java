package life.klstoys.admin.template.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import life.klstoys.admin.template.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 14:58
 */
@Getter
@AllArgsConstructor
public enum GroupCallTypeEnum implements BaseEnum<String> {
    BY_PAGE(1, "by-page", "页面展示是调用"),
    BY_BUTTON(2, "by-button", "通过按钮或链接调用"),
    NONE(3, "none", "不调用，仅表示拥有此页面的前端访问权限"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static GroupCallTypeEnum valueOfEncode(String encode) {
        for (GroupCallTypeEnum value : values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}

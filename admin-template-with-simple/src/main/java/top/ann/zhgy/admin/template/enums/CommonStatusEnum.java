package top.ann.zhgy.admin.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.ann.zhgy.admin.template.common.BaseEnum;

/**
 * 通用状态
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 08:58
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements BaseEnum<String> {
    ENABLE(1, "enable", "可用"),
    DISABLE(2, "disable", "禁用"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CommonStatusEnum valueOfEncode(String encode) {
        for (CommonStatusEnum value : CommonStatusEnum.values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}

package life.klstoys.admin.template.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import life.klstoys.admin.template.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/9 15:05
 */
@Getter
@AllArgsConstructor
public enum RequestMethodEnum implements BaseEnum<String> {
    GET(1, "get", "GET"),
    POST(2, "post", "GET"),
    PUT(3, "put", "GET"),
    DELETE(4, "delete", "GET"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static RequestMethodEnum valueOfEncode(String encode) {
        for (RequestMethodEnum value : values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}

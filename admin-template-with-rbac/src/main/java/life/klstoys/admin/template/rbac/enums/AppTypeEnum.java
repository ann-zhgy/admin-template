package life.klstoys.admin.template.rbac.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import life.klstoys.admin.template.common.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1-后端，2-web端，3-小程序，4-移动端安卓，5-移动端ios
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/12/18 16:58
 */
@Getter
@AllArgsConstructor
public enum AppTypeEnum implements BaseEnum<String> {
    BACKEND(1, "backend", "服务端"),
    WEB(1 << 1, "web", "web端"),
    MINI_PROGRAM(1 << 2, "mini-program", "小程序"),
    ANDROID(1 << 3, "android", "安卓"),
    IOS(1 << 4, "ios", "ios"),
    ;
    @EnumValue
    private final int code;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static AppTypeEnum valueOfEncode(String encode) {
        for (AppTypeEnum value : values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }

    public static Set<AppTypeEnum> valuesFromCode(Integer permissionCode) {
        if (permissionCode == null) {
            return Collections.emptySet();
        }
        Set<AppTypeEnum> result = new HashSet<>();
        for (AppTypeEnum value : AppTypeEnum.values()) {
            if ((permissionCode & value.getCode()) == value.getCode()) {
                result.add(value);
            }
        }
        return result;
    }

    public static int toCode(Collection<AppTypeEnum> permissions) {
        if (permissions == null) {
            return 0;
        }
        int result = 0;
        for (AppTypeEnum permissionEnum : permissions) {
            result = permissionEnum.getCode() | result;
        }
        return result;
    }
}
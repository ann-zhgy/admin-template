package top.ann.zhgy.admin.template.enums;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.ann.zhgy.admin.template.common.BaseEnum;

/**
 * 验证码类型，基于tianai
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/28 14:02
 */
@Getter
@AllArgsConstructor
public enum CaptchaTypeEnum implements BaseEnum<String> {
    SLIDER(CaptchaTypeConstant.SLIDER, "滑块"),
    ROTATE(CaptchaTypeConstant.ROTATE, "旋转"),
    CONCAT(CaptchaTypeConstant.CONCAT, "连接"),
    WORD_IMAGE_CLICK(CaptchaTypeConstant.WORD_IMAGE_CLICK, "图片点选"),
    ;
    @JsonValue
    private final String encode;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CaptchaTypeEnum valueOfEncode(String encode) {
        for (CaptchaTypeEnum value : CaptchaTypeEnum.values()) {
            if (value.getEncode().equals(encode)) {
                return value;
            }
        }
        return null;
    }
}
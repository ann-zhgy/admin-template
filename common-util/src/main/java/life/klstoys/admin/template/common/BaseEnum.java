package life.klstoys.admin.template.common;

/**
 * base枚举类型
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 10:33
 */
public interface BaseEnum<CodeType> {
    /**
     * 约定使用 encode 作为前端传入的枚举类型参数的值
     */
    CodeType getEncode();

    /**
     * 约定使用 description 作为枚举类型数据的描述
     */
    String getDescription();
}

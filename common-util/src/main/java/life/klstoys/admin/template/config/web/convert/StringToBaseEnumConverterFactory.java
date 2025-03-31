package life.klstoys.admin.template.config.web.convert;

import com.google.common.collect.Maps;
import jakarta.annotation.Nonnull;
import life.klstoys.admin.template.common.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.Map;

/**
 * 枚举转换器工厂
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 13:49
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum<String>> {
    private static final Map<Class, Converter> CONVERTERS = Maps.newHashMap();

    @Nonnull
    @Override
    public <EnumType extends BaseEnum<String>> Converter<String, EnumType> getConverter(@Nonnull Class<EnumType> targetType) {
        Converter<String, EnumType> converter = CONVERTERS.get(targetType);
        if(converter == null){
            converter = new StringToBaseEnumConvert(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }

    static final class StringToBaseEnumConvert<T extends BaseEnum<String>> extends XToBaseEnumConverter<String, T> {
        public StringToBaseEnumConvert(Class<T> enumType) {
            super(enumType);
        }
    }
}

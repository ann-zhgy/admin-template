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
public class BooleanToBaseEnumConverterFactory implements ConverterFactory<Boolean, BaseEnum<Boolean>> {
    private static final Map<Class, Converter> CONVERTERS = Maps.newHashMap();

    @Nonnull
    @Override
    public <EnumType extends BaseEnum<Boolean>> Converter<Boolean, EnumType> getConverter(@Nonnull Class<EnumType> targetType) {
        Converter<Boolean, EnumType> converter = CONVERTERS.get(targetType);
        if(converter == null){
            converter = new BooleanToBaseEnumConvert(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }

    static final class BooleanToBaseEnumConvert<T extends BaseEnum<Boolean>> extends XToBaseEnumConverter<Boolean, T> {
        public BooleanToBaseEnumConvert(Class<T> enumType) {
            super(enumType);
        }
    }
}

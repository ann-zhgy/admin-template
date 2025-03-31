package life.klstoys.admin.template.config.web.convert;

import com.google.common.collect.Maps;
import jakarta.annotation.Nonnull;
import life.klstoys.admin.template.common.BaseEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;
import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 14:20
 */
public abstract class XToBaseEnumConverter<T, E extends BaseEnum<T>> implements Converter<T, E> {
    private final Map<T, E> enumMap = Maps.newHashMap();

    public XToBaseEnumConverter(Class<E> enumType){
        E[] enums = enumType.getEnumConstants();
        for (E e : enums) {
            enumMap.put(e.getEncode(), e);
        }
    }

    @Override
    public E convert(@Nonnull T source) {
        E e = enumMap.get(source);
        if(Objects.isNull(e)) {
            return null;
        }
        return e;
    }
}

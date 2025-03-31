package life.klstoys.admin.template.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * 集合流操作util
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/18 16:00
 */
@UtilityClass
public class StreamUtil {
    public static <T> Stream<T> ofNullable(Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return Stream.empty();
        }
        return collection.stream();
    }
}

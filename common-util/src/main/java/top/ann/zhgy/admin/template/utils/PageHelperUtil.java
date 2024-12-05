package top.ann.zhgy.admin.template.utils;

import com.github.pagehelper.PageInfo;
import lombok.experimental.UtilityClass;
import top.ann.zhgy.admin.template.common.response.PageResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * page转换
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/18 16:43
 */
@UtilityClass
public class PageHelperUtil {
    public static <T, E> PageResult<T> convertToPage(PageInfo<E> pageInfo, Function<E, T> itemConvertFunction) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalCount(pageInfo.getTotal());
        result.setTotalPage(pageInfo.getPages());
        List<T> data = StreamUtil.ofNullable(pageInfo.getList()).map(itemConvertFunction).collect(Collectors.toList());
        result.setItems(data);
        return result;
    }

    public static <T, E> PageResult<T> buildPage(PageInfo<E> pageInfo, List<T> data) {
        PageResult<T> result = new PageResult<>();
        result.setPageNum(pageInfo.getPageNum());
        result.setPageSize(pageInfo.getPageSize());
        result.setTotalCount(pageInfo.getTotal());
        result.setTotalPage(pageInfo.getPages());
        result.setItems(data);
        return result;
    }
}

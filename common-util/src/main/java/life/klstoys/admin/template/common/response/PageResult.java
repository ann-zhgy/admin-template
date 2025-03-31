package life.klstoys.admin.template.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页返回结果(通过页码分页)
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/17 10:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PageResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -3171478706409652261L;
    /** 页码 */
    private int pageNum;
    /** 每页条数 */
    private int pageSize;
    /** 数据总量 */
    private long totalCount;
    /** 总页数 */
    private int totalPage;
    /** 当前页数据 */
    private List<T> items;

    public static <T> PageResult<T> empty(int pageSize) {
        return PageResult.of(1, pageSize, 0, 0, new ArrayList<>());
    }

    public static <T> PageResult<T> of(List<T> items) {
        return PageResult.of(1, items.size(), items.size(), 1, items);
    }

    public static <T> PageResult<T> of(PageResult<?> pageResult, List<T> items) {
        return PageResult.of(pageResult.pageNum, pageResult.pageSize, pageResult.totalCount, pageResult.totalPage, items);
    }
}

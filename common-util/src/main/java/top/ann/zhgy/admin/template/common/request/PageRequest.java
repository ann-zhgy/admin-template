package top.ann.zhgy.admin.template.common.request;

import lombok.Data;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.exception.ExceptionEnum;

import java.util.Objects;

/**
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/12 15:26
 */
@Data
public class PageRequest {
    private static final int DEFAULT_PAGE_NO = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int DEFAULT_MAX_PAGE_SIZE = 100;

    /** 页码 */
    private Integer pageNo;

    /** 每页条数 */
    private Integer pageSize;

    public void checkAndFillDefaultPageIfNull() {
        checkAndFillDefaultPageIfNull(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE, DEFAULT_MAX_PAGE_SIZE);
    }

    public void checkAndFillDefaultPageIfNull(int pageNo, int pageSize, int maxPageSize) {
        if (Objects.isNull(this.pageNo)) {
            this.pageNo = pageNo;
        }
        if (Objects.isNull(this.pageSize)) {
            this.pageSize = pageSize;
        }
        if (this.pageNo <= 0) {
            throw new BizException(ExceptionEnum.PARAM_INVALID, "页码必须大于0");
        }
        if (this.pageSize > maxPageSize) {
            throw new BizException(ExceptionEnum.PARAM_INVALID, "每页条数最大为" + maxPageSize);
        }
    }
}

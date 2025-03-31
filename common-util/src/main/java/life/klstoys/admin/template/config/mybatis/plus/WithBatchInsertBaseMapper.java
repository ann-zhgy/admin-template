package life.klstoys.admin.template.config.mybatis.plus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

/**
 * mybatis plus base mapper 带有批量插入方法
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 10:38
 */
public interface WithBatchInsertBaseMapper<T> extends BaseMapper<T> {
    Integer insertBatchSomeColumn(Collection<T> entities);
}

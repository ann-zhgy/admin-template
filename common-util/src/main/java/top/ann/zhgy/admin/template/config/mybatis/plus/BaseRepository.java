package top.ann.zhgy.admin.template.config.mybatis.plus;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import top.ann.zhgy.admin.template.exception.BizException;
import top.ann.zhgy.admin.template.exception.ExceptionEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * base repository
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 10:43
 */
@Slf4j
@Getter
public abstract class BaseRepository<M extends WithBatchInsertBaseMapper<T>, T> {
    protected static final int DEFAULT_BATCH_SIZE = 500;

    @Setter(onMethod_ = {@Autowired})
    private M baseMapper;

    public void insert(T entity) {
        if (Objects.isNull(entity)) {
            log.error("插入数据库的数据为null");
            throw new BizException(ExceptionEnum.DATABASE_INSERT_DATA_NULL);
        }
        int insertRow = getBaseMapper().insert(entity);
        if (insertRow < 1) {
            log.error("插入数据库失败");
            throw new BizException(ExceptionEnum.DATABASE_INSERT_ROW_ERROR);
        }
    }

    /**
     * 批量插入，可能抛错，需要外部手动处理事务
     *
     * @param entities 实体
     * @param batchSize 批次数量
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(Collection<T> entities, int batchSize) {
        List<List<T>> partition = Lists.partition(new ArrayList<>(entities), batchSize);
        for (int i = 0; i < partition.size(); i++) {
            List<T> list = partition.get(i);
            Integer insertRow = getBaseMapper().insertBatchSomeColumn(list);
            if (insertRow != list.size()) {
                log.error("第 {} 批次插入数据库失败，应插入数据条数：{}，实际插入数据条数：{}", i + 1, list.size(), insertRow);
                throw new BizException(ExceptionEnum.DATABASE_INSERT_ROW_ERROR);
            }
        }
    }

    /**
     * 批量插入，按照每批次 500条 插入数据，可能抛错，需要外部手动处理事务
     *
     * @param entities 实体
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(Collection<T> entities) {
        batchInsert(entities, DEFAULT_BATCH_SIZE);
    }

    public int updateById(T entity) {
        return getBaseMapper().updateById(entity);
    }
}

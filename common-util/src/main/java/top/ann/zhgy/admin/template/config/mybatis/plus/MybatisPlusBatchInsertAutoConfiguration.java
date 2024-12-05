package top.ann.zhgy.admin.template.config.mybatis.plus;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

/**
 * mybatis plus 批量插入配置
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/4/30 10:51
 */
@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@ConditionalOnClass({MybatisPlusAutoConfiguration.class})
public class MybatisPlusBatchInsertAutoConfiguration {
    @Bean
    public BatchInsertSqlInjector batchInsertSqlInjector() {
        return new BatchInsertSqlInjector();
    }

    @Bean
    public GlobalConfig globalConfig(BatchInsertSqlInjector batchInsertSqlInjector) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSqlInjector(batchInsertSqlInjector);
        return globalConfig;
    }

    public static class BatchInsertSqlInjector extends DefaultSqlInjector {
        @Override
        public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
            List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
            Set<FieldStrategy> ignoreInsertStrategy = Set.of(FieldStrategy.NEVER);
            methodList.add(new InsertBatchSomeColumn(tableFieldInfo -> !ignoreInsertStrategy.contains(tableFieldInfo.getInsertStrategy())));
            return methodList;
        }
    }
}

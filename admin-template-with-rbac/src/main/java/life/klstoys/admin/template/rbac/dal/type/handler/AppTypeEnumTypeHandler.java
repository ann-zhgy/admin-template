package life.klstoys.admin.template.rbac.dal.type.handler;

import life.klstoys.admin.template.rbac.enums.AppTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

/**
 * 用户权限
 *
 * @author zhanggaoyu@workatdata.com
 * @since 2024/11/27 09:00
 */
public class AppTypeEnumTypeHandler extends BaseTypeHandler<Set<AppTypeEnum>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<AppTypeEnum> parameter, JdbcType jdbcType) throws SQLException {
        if (CollectionUtils.isEmpty(parameter)) {
            ps.setInt(i, 0);
            return;
        }
        ps.setInt(i, AppTypeEnum.toCode(parameter));
    }

    @Override
    public Set<AppTypeEnum> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return AppTypeEnum.valuesFromCode(rs.getInt(columnName));
    }

    @Override
    public Set<AppTypeEnum> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return AppTypeEnum.valuesFromCode(rs.getInt(columnIndex));
    }

    @Override
    public Set<AppTypeEnum> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return AppTypeEnum.valuesFromCode(cs.getInt(columnIndex));
    }
}

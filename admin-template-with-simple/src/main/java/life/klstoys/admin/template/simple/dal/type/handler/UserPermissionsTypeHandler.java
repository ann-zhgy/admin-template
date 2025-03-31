package life.klstoys.admin.template.simple.dal.type.handler;

import life.klstoys.admin.template.simple.enums.UserPermissionEnum;
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
public class UserPermissionsTypeHandler extends BaseTypeHandler<Set<UserPermissionEnum>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<UserPermissionEnum> parameter, JdbcType jdbcType) throws SQLException {
        if (CollectionUtils.isEmpty(parameter)) {
            ps.setInt(i, 0);
            return;
        }
        ps.setInt(i, UserPermissionEnum.toPermissionCode(parameter));
    }

    @Override
    public Set<UserPermissionEnum> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return UserPermissionEnum.valuesFromPermissionCode(rs.getInt(columnName));
    }

    @Override
    public Set<UserPermissionEnum> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return UserPermissionEnum.valuesFromPermissionCode(rs.getInt(columnIndex));
    }

    @Override
    public Set<UserPermissionEnum> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return UserPermissionEnum.valuesFromPermissionCode(cs.getInt(columnIndex));
    }
}

package pdsu.dao;

import org.apache.ibatis.annotations.Param;
import pdsu.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findByRoleId(@Param("roleId") Integer roleId);
}

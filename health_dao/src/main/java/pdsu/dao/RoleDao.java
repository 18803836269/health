package pdsu.dao;

import org.apache.ibatis.annotations.Param;
import pdsu.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findByUserId(@Param("userId") Integer userId);
}

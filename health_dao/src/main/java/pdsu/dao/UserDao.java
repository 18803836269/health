package pdsu.dao;

import org.apache.ibatis.annotations.Param;
import pdsu.pojo.User;

import java.util.List;

public interface UserDao {
    User findByUsername(@Param("username") String username);

    List<String> findMenuListByUsername(@Param("username") String username);
}

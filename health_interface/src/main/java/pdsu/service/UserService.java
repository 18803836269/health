package pdsu.service;

import pdsu.pojo.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    List<String> findMenuListById(Integer id);

    List<String> findMenuListByUsername(String username);
}

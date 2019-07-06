package pdsu.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import pdsu.pojo.CheckGroup;
import pdsu.pojo.CheckItem;

import java.util.List;

public interface CheckgroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(@Param("checkitemId") Integer checkitemId, @Param("checkGroupId") Integer checkGroupId);

    Page<CheckGroup> findPage(@Param("queryString") String queryString);

    CheckGroup findById(@Param("id")Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(@Param("checkGroupId")Integer checkGroupId);

    void edit(CheckGroup checkGroup);

    void deleteAssociation(@Param("checkGroupId")Integer checkGroupId);

    List<CheckItem> findAll();
}

package pdsu.dao;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import pdsu.pojo.CheckItem;

import java.util.List;

public interface CheckitemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(@Param("queryString") String queryString);

    Integer findCountByCheckItemId(@Param("id") Integer id);

    void delete(@Param("id") Integer id);

    CheckItem findById(@Param("id")Integer id);

    void edit(CheckItem checkItem);

}

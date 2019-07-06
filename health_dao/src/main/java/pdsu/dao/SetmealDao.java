package pdsu.dao;

import org.apache.ibatis.annotations.Param;
import pdsu.pojo.CheckGroup;
import pdsu.pojo.CheckItem;
import pdsu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(@Param("checkgroupId") Integer checkgroupId,
                                 @Param("setmealId") Integer setmealId);

    List<Setmeal> findPage(@Param("queryString") String queryString);

    List<Setmeal> getSetmeal();

    Setmeal findById(@Param("setmealId")Integer setmealId);

    List<CheckGroup> findCheckGroupBySetmealId(@Param("setmealId")Integer setmealId);

    List<CheckItem> findCheckItemByCheckGroupId(@Param("checkGroupId")Integer checkGroupId);

    List<Map<String, Object>> findSetmealCount();
}

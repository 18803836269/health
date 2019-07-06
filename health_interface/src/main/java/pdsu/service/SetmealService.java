package pdsu.service;

import pdsu.entity.PageResult;
import pdsu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findPage(String queryString, Integer currentPage, Integer pageSize);

    List<Setmeal> getSetmeal();

    Setmeal findById(Integer id);

    Setmeal findDetailById(Integer id);

    List<Map<String, Object>> findSetmealCount();

}

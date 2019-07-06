package pdsu.service;

import pdsu.entity.PageResult;
import pdsu.pojo.CheckGroup;
import pdsu.pojo.CheckItem;

import java.util.List;

public interface CheckgroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(Integer pageSize, Integer currentPage, String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer checkGroupId);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    List<CheckItem> findAll();
}

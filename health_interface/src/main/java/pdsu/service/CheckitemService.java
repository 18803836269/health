package pdsu.service;

import pdsu.entity.PageResult;
import pdsu.pojo.CheckItem;

import java.util.List;

public interface CheckitemService {
    void add(CheckItem checkItem);

    PageResult findPage(Integer currentPage, Integer pageSize, String queryString);

    int delete(Integer id);

    CheckItem findById(Integer id);

    void edit(CheckItem checkItem);

}

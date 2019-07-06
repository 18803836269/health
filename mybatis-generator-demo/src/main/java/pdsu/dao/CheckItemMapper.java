package pdsu.dao;

import pdsu.pojo.CheckItem;

public interface CheckItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheckItem record);

    int insertSelective(CheckItem record);

    CheckItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CheckItem record);

    int updateByPrimaryKey(CheckItem record);
}
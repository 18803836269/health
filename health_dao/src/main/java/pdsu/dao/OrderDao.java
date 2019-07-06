package pdsu.dao;

import org.apache.ibatis.annotations.Param;
import pdsu.pojo.Order;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderDao {
    public void add(Order order);
    public List<Order> findByCondition(Order order);
    public Map findById4Detail(Integer id);
    public Integer findOrderCountByDate(String date);
    public Integer findOrderCountAfterDate(String date);
    public Integer findVisitsCountByDate(String date);
    public Integer findVisitsCountAfterDate(String date);
    public List<Map> findHotSetmeal();

    void findName(@Param("name") String name);

    void findSetmeal(@Param("setmeal")String setmeal);

    void findOrderDate(@Param("orderDate")Date orderDate);

    void findOrderType(@Param("orderType")String orderType);

    /**
     * -- 体检人
     * select name from t_member where id in(select member_id from t_order where id = 29);
     * -- 体检套餐
     * select name from t_setmeal where id in(select setmeal_id from t_order where id = 29);
     * -- 体检日期
     * select orderDate from t_order where id = 29;
     * -- 预约类型
     * select orderType from t_order where id = 29;
     */
}

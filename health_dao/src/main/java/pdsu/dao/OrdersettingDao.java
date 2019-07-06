package pdsu.dao;

import cn.hutool.core.date.DateTime;
import pdsu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrdersettingDao {
    Integer findCountByDate(@Param("orderDate") Date orderDate);

    void edit(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    OrderSetting findByOrderDate(@Param("orderDate") Date orderDate);

    int  editReservationsByOrderDate(OrderSetting orderSetting);

    //    like CONCAT('%',#{bidProduct},'%')
    List<OrderSetting> getOrderSettingByDate(@Param("dateStart")String dateStart,@Param("dateEnd") String dateEnd);
}

package pdsu.service;

import pdsu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrdersettingService {
    void editNumberByDate(OrderSetting orderSetting);

    void add(List<OrderSetting> orderSettings);

    List<Map> getOrderSettingByDate(String dateStr);
}

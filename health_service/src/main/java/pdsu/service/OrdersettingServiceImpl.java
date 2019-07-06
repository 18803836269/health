package pdsu.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.dubbo.config.annotation.Service;
import pdsu.dao.OrdersettingDao;
import pdsu.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrdersettingService.class)
@Transactional
public class OrdersettingServiceImpl implements OrdersettingService {

    @Autowired
    OrdersettingDao ordersettingDao;

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        /**
         * 1、查询一下当前日期是否存在预约设置
         * 2、如果存在修改
         * 3、不存在插入
         */
        Integer count = ordersettingDao.findCountByDate(orderSetting.getOrderDate());
        if (count != null && count > 0){
            ordersettingDao.edit(orderSetting);
        } else {
            ordersettingDao.add(orderSetting);
        }
    }

    @Override
    public void add(List<OrderSetting> orderSettings) {
        //遍历orderSettings
        if (CollectionUtil.isNotEmpty(orderSettings)){
            for (OrderSetting orderSetting : orderSettings) {
                Integer count = ordersettingDao.findCountByDate(orderSetting.getOrderDate());
                if (count != null && count > 0){
                    ordersettingDao.edit(orderSetting);
                } else {
                    ordersettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByDate(String dateStr) {//2019-6
        //[{ date: 1, number: 120, reservations: 1 },{ date: 1, number: 120, reservations: 1 }]
        List<Map> result = new ArrayList<>();
        String dateStart = dateStr + "-1";
        String dateEnd = dateStr + "-31";
        List<OrderSetting> orderSettings = ordersettingDao.getOrderSettingByDate(dateStart,dateEnd);
        if(CollectionUtil.isNotEmpty(orderSettings)){
            for (OrderSetting orderSetting : orderSettings) {
                int number = orderSetting.getNumber();
                int reservations = orderSetting.getReservations();
                int date = orderSetting.getOrderDate().getDate();
                Map map = new HashMap();
                map.put("date",date);
                map.put("number",number);
                map.put("reservations",reservations);
                result.add(map);
            }
        }
        return result;
    }
}

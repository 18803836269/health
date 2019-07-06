package pdsu.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pdsu.constant.MessageConstant;
import pdsu.dao.MemberDao;
import pdsu.dao.OrderDao;
import pdsu.dao.OrdersettingDao;
import pdsu.entity.Result;
import pdsu.exception.RepeatException;
import pdsu.pojo.Member;
import pdsu.pojo.Order;
import pdsu.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrdersettingDao ordersettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public Result saveOrder(Map map) {
        String orderDateStr = (String) map.get("orderDate");
        DateTime orderDate = DateUtil.parseDate(orderDateStr);
        String telephone = (String) map.get("telephone");
        String setmealId = (String) map.get("setmealId");
        String orderType = (String) map.get("orderType");
        //检查用户选择预约日期是否存在预约设置
        OrderSetting orderSetting = ordersettingDao.findByOrderDate(orderDate);
//     ('1', '2019-06-29', '7', '101', '0');
        //     ('1', '2019-06-29', '7', '101', '0');

        if(null == orderSetting){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断可预约数是否充足
        int number = orderSetting.getNumber();
        int reservations = orderSetting.getReservations();
        if(reservations >= number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //收集用户信息（创建用户）
        Member member = memberDao.findByTelephone(telephone);
        if (null == member){
            member = new Member();
            member.setName((String)map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //判断用户选择的日期&选择的套餐是否存在预约
        Integer userId = member.getId();
        Order query = new Order();
        query.setMemberId(userId);
        query.setSetmealId(Integer.valueOf(setmealId));
        query.setOrderDate(orderDate);
        List<Order> orders = orderDao.findByCondition(query);
        if (CollectionUtil.isNotEmpty(orders)){
            return new Result(false,MessageConstant.HAS_ORDERED);
        }
        //保存预约信息Integer memberId, Date orderDate, String orderType, String orderStatus, Integer setmealId
        Order order = new Order(userId,orderDate,orderType,Order.ORDERSTATUS_NO,Integer.valueOf(setmealId));
        orderDao.add(order);
        //6 修改已预约数量 + 1
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        int i = ordersettingDao.editReservationsByOrderDate(orderSetting);
        if(1 != i){
            throw  new RepeatException("预约冲突");
        }
        return new Result(true,MessageConstant.ORDER_SUCCESS);
    }

    @Override
    public void findOrderMessage(String name, String setmeal, Date orderDate, String orderType) {
        orderDao.findName(name);
        orderDao.findSetmeal(setmeal);
        orderDao.findOrderDate(orderDate);
        orderDao.findOrderType(orderType);
    }
}

package pdsu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.constant.MessageConstant;
import pdsu.constant.RedisMessageConstant;
import pdsu.entity.Result;
import pdsu.exception.RepeatException;
import pdsu.pojo.Member;
import pdsu.pojo.Order;
import pdsu.pojo.Setmeal;
import pdsu.service.OrderService;
import pdsu.service.SetmealService;
import pdsu.utils.JuheSmsUtil;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    JedisPool jedisPool;
    @Reference
    OrderService orderService;
    @Reference
    SetmealService setmealService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map order){
        Result result = null;
        try {
            doSubmit(order);
        } catch (RepeatException e) {
            e.printStackTrace();
            for (int i = 0; i < 2; i++) {
                doSubmit(order);
            }
        }
        String setmealId = (String) order.get("setmealId");
        int id = Integer.parseInt(setmealId);
        return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,id);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        Map<String, Object> map = new HashMap<>();
        String name = new Member().getName();
        String setmeal = new Setmeal().getName();
        Date orderDate = new Order().getOrderDate();
        String orderType = new Order().getOrderType();
        map.put("name",name);
        map.put("setmeal",setmeal);
        map.put("orderDate",orderDate);
        map.put("orderType",orderType);
        try {
            orderService.findOrderMessage(name,setmeal,orderDate,orderType);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    private Result doSubmit( Map order){
        String telephone = (String)order.get("telephone");
        String validateCode = (String)order.get("validateCode");
        //验证短信验证码
        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + telephone);
        if(null == code){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        if(!code.equals(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        order.put("orderType",Order.ORDERTYPE_WEIXIN);
        //调用预约dubbo服务
        Result result = orderService.saveOrder(order);
        if (result.isFlag()){
            JuheSmsUtil.sendNotice(telephone);
        }
        return result;
    }
}

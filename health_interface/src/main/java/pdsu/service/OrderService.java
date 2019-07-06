package pdsu.service;

import pdsu.entity.Result;

import java.util.Date;
import java.util.Map;

public interface OrderService {
    Result saveOrder(Map order);

    void findOrderMessage(String name, String setmeal, Date orderDate, String orderType);
}

package pdsu.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import pdsu.constant.MessageConstant;
import pdsu.entity.Result;
import pdsu.pojo.OrderSetting;
import pdsu.service.OrdersettingService;
import pdsu.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    OrdersettingService ordersettingService;

    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            ordersettingService.editNumberByDate(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            //解析excel
            List<String[]> strings = POIUtils.readExcel(excelFile);
            if(CollectionUtil.isNotEmpty(strings)){
                List<OrderSetting> orderSettings = new ArrayList<>();
                //遍历解析数组（数组里面是excel的单元格数据）
                for (String[] row : strings) {
                    String dateStr = row[0];
                    String numberStr = row[1];
                    OrderSetting orderSetting = new OrderSetting(DateUtil.parse(dateStr,"yyyy/MM/dd"),Integer.parseInt(numberStr));
                    orderSettings.add(orderSetting);
                }
                ordersettingService.add(orderSettings);
            }
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/getOrderSettingByDate")
    public Result getOrderSettingByDate(String dateStr){
        try {
            //[{ date: 1, number: 120, reservations: 1 },{ date: 1, number: 120, reservations: 1 }]
            List<Map> result = ordersettingService.getOrderSettingByDate(dateStr);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }
}

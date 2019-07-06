package pdsu.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pdsu.constant.MessageConstant;
import pdsu.entity.Result;
import pdsu.service.ReportService;
import pdsu.service.SetmealService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    ReportService reportService;
    @Reference
    SetmealService setmealService;


    @RequestMapping("getMemberReport")
    public Result getMemberReport() {
        Date currentDate = new Date();
        ArrayList<String> months = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            DateTime dateTime = DateUtil.offsetMonth(currentDate, -i);
            months.add(dateTime.toString("yyyy-MM"));
        }
        //倒叙排列集合
        Collections.reverse(months);
        List<Integer> memberCount = reportService.getMemberCount(months);
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }

    /**
     * 套餐占比统计
     * @return
     */
    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){
        List<Map<String,Object>> list= setmealService.findSetmealCount();

        Map<String,Object> map = new HashMap<>();
        map.put("setmealCount",list);

        List<String> setmealNames = new ArrayList<>();
        for (Map<String, Object> m : list) {
            String name = (String) m.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    @RequestMapping("getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> result = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出Excel表格
     * @param request
     * @param response
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            /**
             *   reportDate:null,
             *   todayNewMember :0,
             *   totalMember :0,
             *   thisWeekNewMember :0,
             *   thisMonthNewMember :0,
             *   todayOrderNumber :0,
             *   todayVisitsNumber :0,
             *   thisWeekOrderNumber :0,
             *   thisWeekVisitsNumber :0,
             *   thisMonthOrderNumber :0,
             *   thisMonthVisitsNumber :0,
             */
            Map result = reportService.getBusinessReportData();
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");
            String template = request.getSession().getServletContext().getRealPath("template") + File.separator  + "report_template.xlsx";
            System.out.println(template);
            XSSFWorkbook workbook = new XSSFWorkbook(template);
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            //日期
            row.getCell(5).setCellValue(reportDate);

            row = sheet.getRow(4);
            //新增会员数（本日）
            row.getCell(5).setCellValue(todayNewMember);
            //总会员数
            row.getCell(7).setCellValue(totalMember);

            row = sheet.getRow(5);
            //本周新增会员数
            row.getCell(5).setCellValue(thisWeekNewMember);
            //本月新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);

            row = sheet.getRow(7);
            //今日预约数
            row.getCell(5).setCellValue(todayOrderNumber);
            //今日到诊数
            row.getCell(7).setCellValue(todayVisitsNumber);

            row = sheet.getRow(8);
            //本周预约数
            row.getCell(5).setCellValue(thisWeekOrderNumber);
            //本周到诊数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);

            row = sheet.getRow(9);
            //本月预约数
            row.getCell(5).setCellValue(thisMonthOrderNumber);
            //本月到诊数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);

            int num = 12;
            for (int i = 0; i < hotSetmeal.size(); i++) {
                Map map = hotSetmeal.get(i);
                row = sheet.getRow(num + i);
                row.getCell(4).setCellValue((String) map.get("name"));
                row.getCell(5).setCellValue((Long) map.get("setmeal_count"));
                row.getCell(6).setCellValue(((BigDecimal)map.get("proportion")).doubleValue());
            }

            ServletOutputStream outputStream = response.getOutputStream();
            //向浏览器说明输出的文本类型是Excel格式
            response.setHeader("Content-Type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setHeader("Content-Type","application/vnd.ms-excel");
            //向浏览器说明文件是以附件的形式展示，attachment：附件；filename：文件下载后的名字
            response.setHeader("Content-Disposition","attachment;filename=report.xlsx");
//            URLEncoder.encode("niand","UTF-8")
            workbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

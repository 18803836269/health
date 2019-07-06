package pdsu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Integer> getMemberCount(ArrayList<String> months);

    /**
     * 获得运营统计数据
     * Map数据格式：
     *      todayNewMember --> number
     *      totalMember --> number
     *      thisWeekNewMember --> number
     *      thisMonthNewMember --> number
     *      todayVisitsNumber --> number
     *      thisWeekOrderNumber --> number
     *      thisWeekVisitsNumber --> number
     *      thisMonthOrderNumber --> number
     *      thisMonthVisitsNumber --> number
     *      hotSetmeals --> number
     */
    Map<String, Object> getBusinessReportData();

}

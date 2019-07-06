package pdsu.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pdsu.dao.MemberDao;
import pdsu.dao.OrderDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    MemberDao memberDao;
    @Autowired
    OrderDao orderDao;

    @Override
    public List<Integer> getMemberCount(ArrayList<String> months) {
        List<Integer> memberCounts = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(months)){
            for (String month : months) {
                Integer count = memberDao.findMemberCountBeforeDate(month + "-31");
                memberCounts.add(count);
            }
        }
        return memberCounts;
    }

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
    @Override
    public Map<String, Object> getBusinessReportData() {
        //获得当前日期
        String format = "yyyy-MM-dd";
        //获取当前时间
        DateTime now = DateTime.now();
        //本周的第一天
        DateTime beginOfWeek = DateUtil.beginOfWeek(now);
        //本月的第一天
        DateTime beginOfMonth = DateUtil.beginOfMonth(now);

        String nowStr = now.toString(format);
        String beginOfWeekStr = beginOfWeek.toString(format);
        String beginOfMonthStr = beginOfMonth.toString(format);

        Map map = new HashMap();
        map.put("reportDate",now.toString());
        map.put("todayNewMember",memberDao.findMemberCountByDate(nowStr));
        map.put("totalMember",memberDao.findMemberTotalCount());
        map.put("thisWeekNewMember",memberDao.findMemberCountAfterDate(beginOfWeekStr));
        map.put("thisMonthNewMember",memberDao.findMemberCountAfterDate(beginOfMonthStr));

        map.put("todayOrderNumber",orderDao.findOrderCountByDate(nowStr));
        map.put("todayVisitsNumber",orderDao.findVisitsCountByDate(nowStr));

        map.put("thisWeekOrderNumber",orderDao.findOrderCountAfterDate(beginOfWeekStr));
        map.put("thisWeekVisitsNumber",orderDao.findVisitsCountAfterDate(beginOfWeekStr));
        map.put("thisMonthOrderNumber",orderDao.findOrderCountAfterDate(beginOfMonthStr));
        map.put("thisMonthVisitsNumber",orderDao.findVisitsCountAfterDate(beginOfMonthStr));
        map.put("hotSetmeal",orderDao.findHotSetmeal());
        return map;
    }

    public static void main(String[] args) {
        String str = "2019-06-20 00:00:00";
        DateTime parse = DateUtil.parse(str, "yyyy-MM-dd HH:mm:SS");
        DateTime now = DateTime.now();
        DateTime beginOfWeek = DateUtil.beginOfWeek(parse);
        DateTime beginOfMonth = DateUtil.beginOfMonth(parse);
        System.out.println(now.toString());
        System.out.println(beginOfMonth);
        System.out.println(beginOfWeek);
    }
}

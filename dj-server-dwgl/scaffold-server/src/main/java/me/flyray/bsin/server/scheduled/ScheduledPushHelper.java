//package me.flyray.bsin.server.scheduled;
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import me.flyray.bsin.facade.service.JcxfPartyMemberService;
//import me.flyray.bsin.facade.service.TzMessageAutoService;
//import me.flyray.bsin.server.mapper.TzMessageAutoMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class ScheduledPushHelper {
//
//    @Autowired
//    private TzMessageAutoService tzMessageAutoService;
//
//    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
////    @Transactional
////    @Scheduled(cron = "0 0/1 * * * ?") //每分钟执行一次
////    @ApiOperation("实际入住日期晚于申请入住日期")
////    public void timedTask() {
////        try {
////            List<Integer> auditStatusList = new ArrayList<>();
////            auditStatusList.add(3);
////            auditStatusList.add(4);
////            List<TYjstb> yjstbList = itYjstbService.queryTAllYjstbByStatus(auditStatusList);
////            TAccount account = null;
////            for (TYjstb yjstb : yjstbList) {
////                Date yyrzDate = sdf.parse(yjstb.getReserveDate().split("~")[0]);
////                Date checkInDate = sdf.parse(sdf.format(yjstb.getRealCheckInDate()));
////                long yyrzTime = yyrzDate.getTime();
////                long checkInTime = checkInDate.getTime();
////                account = new TAccount();
////                account.setId(yjstb.getAccountId());
////                TAccount byId = itAccountService.getById(yjstb.getAccountId());
////                //判断预约入住时间(第一个节点)与实际入住时间比较(预约入住时间小于实际入住时间，用户状态改为黑名单)
////                if(yyrzTime < checkInTime && byId.getBlackStatus() != 1){
////                    account.setBlackStatus(1);
////                    //itAccountService.updateById(account);
////                }
////            }
////        } catch (Exception e) {
////            log.error(e.getMessage());
////        }
////    }
////
////    @Transactional
////    @Scheduled(cron = "0 0/1 * * * ?") //每分钟执行一次
////    @ApiOperation("预定了青年驿站，实际并未入住的")
////    public void timedTask1() {
////        try {
////            List<Integer> auditStatusList = new ArrayList<>();
////            auditStatusList.add(2);
////            List<TYjstb> yjstbList = itYjstbService.queryTAllYjstbByStatus(auditStatusList);
////            TAccount account = null;
////            for (TYjstb yjstb : yjstbList) {
////                Date rzDate = sdf.parse(yjstb.getReserveDate().split("~")[1]);
////                Date nowDate = sdf.parse(sdf.format(System.currentTimeMillis()));
////                long rzTime = rzDate.getTime();
////                long nowTime = nowDate.getTime();
////                account = new TAccount();
////                account.setId(yjstb.getAccountId());
////                TAccount byId = itAccountService.getById(yjstb.getAccountId());
////                //判断预约入住时间(最后一个节点)与当前时间比较(预约入住时间小于当前时间，用户状态改为黑名单)
////                if(rzTime < nowTime && byId.getBlackStatus() != 1){
////                    account.setBlackStatus(1);
////                    itAccountService.updateById(account);
////                }
////            }
////        } catch (Exception e) {
////            log.error(e.getMessage());
////        }
////    }
//
//    // @Scheduled(cron = "0 0 12 * * ?") //每天中午12点执行一次
//    @Scheduled(cron = "0 15 10 * * ?") //每天中午12点执行一次
//    @ApiOperation("查询每天的生日党员")
//    public void timedTask1() {
//        try {
//            System.out.println("定时任务开始");
//            tzMessageAutoService.queryPolicyBirthdayToday();
//            tzMessageAutoService.queryBirthdayToday();
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
//
//
//
//    /**
//     * 给时间减一天   接受时间类型：yyyy-mm-dd的字符串
//     *
//     * @return
//     */
//    public static String minusOneDay(String time) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//        //将接收的time中的年月日截取成String数组
//        String[] timeStr = time.split("-");
//        Calendar calendar = Calendar.getInstance();
//        int year = Integer.valueOf(timeStr[0]);
//        int month = Integer.valueOf(timeStr[1]);
//        int day = Integer.valueOf(timeStr[2]);
//        //判断time中的日期是否是该年该月的一号，如果不是就往前减一天;如果是就看情况减月份和年份
//        if (day <= 1) {
//            String date = null;
//            //如果月份不是1月，就对月份减一；如果月份是1月，就对年份减一；
//            if (month > 1) {
//                month--;
//                Calendar c = Calendar.getInstance();
//                c.set(year, month, 0);
//                Date parse = dateFormat.parse(year + "-" + month + "-" + c.get(Calendar.DAY_OF_MONTH));
//                date = dateFormat.format(parse);
//            } else if (month == 1) {
//                year--;
//                date = year + "-12-31";
//            }
//            return date;
//        }
//        //time中的日期不是该年该月的一号，直接往前减一天
//        Date date = dateFormat.parse(time);
//        calendar.setTime(date);
//        calendar.add(Calendar.DATE, -1);
//        return dateFormat.format(calendar.getTime());
//    }
//}

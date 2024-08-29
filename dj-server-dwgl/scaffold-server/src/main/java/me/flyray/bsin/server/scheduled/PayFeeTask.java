package me.flyray.bsin.server.scheduled;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.server.config.WxConfig;
import me.flyray.bsin.server.domain.TzPayFee;
import me.flyray.bsin.server.domain.TzPayFeeDetail;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.mapper.TzPayFeeDetailMapper;
import me.flyray.bsin.server.mapper.TzPayFeeMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 每月初自动生成每个支部党员的党费数据
 */
@Component
@Slf4j
public class PayFeeTask {
    @Autowired
    private TzPayFeeMapper tzPayFeeMapper;
    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;
    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;
    @Autowired
    private WxConfig wxConfig;

    /**
     * 定时生成党费数据
     */
//    @Scheduled(cron = "* * 2 1 * ? ")  //每月1日早上2点执行
//    @Scheduled(cron="0 0 1 1 1 ?") //每年1月的1号的1:00:00执行1次
    @DSTransactional
    @XxlJob("genPayFeeMonthData")
    public void genPayFeeMonthData() {
        log.info("开始执行生成党员党费数据>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //查询每个党支部的党员数据
        List<Map<String, Object>> partyMemberList = jcxfPartyMemberMapper.queryDeptPartyMemberList("8009", "true");//查询每个支部的党员数据
        Map<String, List<Map<String, Object>>> deptIdListMap = partyMemberList.stream().collect(Collectors.groupingBy(item -> item.get("dept_id").toString()));


        log.info("查询出来的党员数量：{}", partyMemberList.size());
        log.info("查询出来的部门数量：{}", deptIdListMap.size());

        try {
            //获取当前年月
            String payYear = DateUtil.format(new Date(), "yyyy");
            Date currentDate = new Date();
            BigDecimal actuallyPay = new BigDecimal("0.00");
            TzPayFeeDetail tzPayFeeDetail = null;
            TzPayFee tzPayFee = null;

            List<TzPayFeeDetail> detailArrayList = new ArrayList<TzPayFeeDetail>();

            //循环每个月
            for (int i = 1; i <= 12; i++) {
                String payMonth = payYear + "-" + String.format("%02d", i);
                for (String deptId : deptIdListMap.keySet()) {
                    List<Map<String, Object>> memberList = deptIdListMap.get(deptId);
                    tzPayFee = new TzPayFee();
                    tzPayFee.setPayMonth(payMonth);
                    tzPayFee.setDeptId(Long.parseLong(deptId));
                    tzPayFee.setDeptName(partyMemberList.stream().filter(item -> Objects.equals(item.get("dept_id").toString(), deptId)).findFirst().get().get("dept_name").toString());
                    tzPayFee.setActuallyPay(actuallyPay);
                    tzPayFee.setStatus(0);
                    tzPayFee.setDelFlag(0);
                    tzPayFee.setCreateTime(currentDate);
                    tzPayFeeMapper.insert(tzPayFee);

                    for (Map<String, Object> map : memberList) {
                        tzPayFeeDetail = new TzPayFeeDetail();
                        tzPayFeeDetail.setName(map.get("real_name").toString());
                        tzPayFeeDetail.setPayFeeId(tzPayFee.getId());
                        tzPayFeeDetail.setPayMonth(payMonth);
                        tzPayFeeDetail.setShouldPay(actuallyPay);
                        tzPayFeeDetail.setActuallyPay(actuallyPay);
                        tzPayFeeDetail.setDeptId(tzPayFee.getDeptId());
                        tzPayFeeDetail.setDeptName(tzPayFee.getDeptName());
                        tzPayFeeDetail.setStatus(0);
                        tzPayFeeDetail.setIsInsteadPay(0);
                        tzPayFeeDetail.setCreateTime(currentDate);
                        tzPayFeeDetail.setDelFlag(0);
                        tzPayFeeDetail.setPartyMemberId(Long.parseLong(map.get("party_member_id").toString()));
                        detailArrayList.add(tzPayFeeDetail);
                    }
                }
            }


            List<List<TzPayFeeDetail>> lists = splitList(detailArrayList, 1500);
            for (int i = 0; i < lists.size(); i++) {
                List<TzPayFeeDetail> tzPayFeeDetails = lists.get(i);
                tzPayFeeDetailMapper.saveBatchList(tzPayFeeDetails);
            }
        } catch (Exception e) {
            log.info("生成党员党费数据异常了" + e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }


    public List<List<TzPayFeeDetail>> splitList(List<TzPayFeeDetail> list, int chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min((i + 1) * chunkSize, list.size())))
                .collect(Collectors.toList());
    }


    /**
     * 提醒交纳党费
     */
    @XxlJob("remindPayPartyCost")
    public void remindPayPartyCost() {
        log.info("提醒交纳党费>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        try {
            String payMonth = DateUtil.format(new Date(), "yyyy-MM");
            //查询未缴纳党费的人员，发送模板消息
            List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.queryNoPayMemberList("8009", payMonth);
            log.info("查询出未交纳党员数量：{}", memberList.size());
            for (JcxfPartyMember jcxfPartyMember : memberList) {
                JSONObject data = new JSONObject();
                data.put("first", wxConfig.getValue("尊敬的用户，您的党费还未缴纳，请尽快缴纳党费"));
                data.put("keyword1", wxConfig.getValue(jcxfPartyMember.getRealName()));
                data.put("keyword2", wxConfig.getValue(jcxfPartyMember.getDeptName()));
                data.put("keyword3", wxConfig.getValue(jcxfPartyMember.getPayMonth() + "月份"));
                data.put("keyword4", wxConfig.getValue(String.valueOf(jcxfPartyMember.getShouldPay())) + "元");
                data.put("remark", wxConfig.getValue("点击查看详情"));
                wxConfig.sendWxgMessage(jcxfPartyMember.getOpenid(), "X6d7CdOkzlCPwSTkexZaolIAly0nkQ_zTtR6DiGgYME", data, "https://app.jcjgdj.cn/#/pages/CostPayment/index");
            }
            log.info("发送模板消息成功：{}", memberList.size());
        } catch (Exception e) {
            log.info("月初提醒交纳党费异常>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
            e.printStackTrace();
        }
    }

}

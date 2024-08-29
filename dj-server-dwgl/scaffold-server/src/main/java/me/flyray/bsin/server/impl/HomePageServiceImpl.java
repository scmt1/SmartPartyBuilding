package me.flyray.bsin.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.flyray.bsin.facade.service.HomePageService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.TzPayFeeDetail;
import me.flyray.bsin.server.domain.TzPayFeeDetailLog;
import me.flyray.bsin.server.domain.jcxf.JcxfBranchReelection;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDictionary;
import me.flyray.bsin.server.mapper.TzPayFeeDetailLogMapper;
import me.flyray.bsin.server.mapper.TzPayFeeDetailMapper;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class HomePageServiceImpl implements HomePageService {

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfOrganizationLifeMapper jcxfOrganizationLifeMapper;

    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;

    @Autowired
    private TzPayFeeDetailLogMapper tzPayFeeDetailLogMapper;

    @Autowired
    private JcxfTermDeptMapper jcxfTermDeptMapper;

    @Autowired
    private JcxfDevelopPartyMapper jcxfDevelopPartyMapper;

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Override
    public Map<String, Object> getPartyMemberCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            int count = jcxfPartyMemberMapper.selectCount(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    public Map<String, Object> getDeptCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.like("parent_ids", ","+deptId+",");
            int count = jcxfSysDeptMapper.selectCount(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    /**
     * 三会一课本月数量
     */
    @Override
    public Map<String, Object> getMeetingCountThisMonth(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());


            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1, 00, 00, 00);
            String startTime = simpleDateFormat.format(calendar.getTime());

            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month + 1);
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DATE, -1);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
            String endTime = simpleDateFormat.format(calendar.getTime());

            List<Integer> statusList = new ArrayList<>();
            // statusList.add(2);
            statusList.add(3);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("meeting_type", 1);
            queryWrapper.in("meeting_status", statusList); //会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            queryWrapper.in("dept_id", ids);
            queryWrapper.ge("start_time", simpleDateFormat.parse(startTime));
            queryWrapper.le("start_time", simpleDateFormat.parse(endTime));

            int count = jcxfOrganizationLifeMapper.selectCount(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 组织生活今年次数
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getMeetingCountThisYear(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            List<Integer> statusList = new ArrayList<>();
            // statusList.add(2);
            statusList.add(3);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("meeting_type", 6);
            queryWrapper.in("meeting_status", statusList); //会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            queryWrapper.in("dept_id", ids);
            queryWrapper.ge("start_time", calendar.get(Calendar.YEAR) +"-01-01 00:00:00");
            queryWrapper.le("start_time", calendar.get(Calendar.YEAR) +"-12-31 23:59:59");

            int count = jcxfOrganizationLifeMapper.selectCount(queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 获取本月党费已缴纳金额
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getPayFeeMoneyByMonth(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String month = format.format(calendar.getTime());

            List<Long> listOne = new ArrayList<>(); // 装所有部门id
            if (deptId != null) {
                listOne = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                listOne.add(Long.valueOf(deptId));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("部门id为，请联系管理员"));
            }

            // 查询已缴纳的缴费
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("sum(should_pay) as should_pay");
            queryWrapper.eq("pay_month", month);
            queryWrapper.in("dept_id", listOne);
            queryWrapper.eq("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper.eq("del_flag", 0);
            List<Map<String, Object>> list = tzPayFeeDetailMapper.selectMaps(queryWrapper);

            if (list.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(0));
            }
            if (list != null && list.size() == 1 && list.get(0) != null) {
                BigDecimal amount = (BigDecimal) list.get(0).get("should_pay");
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(amount));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(0));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 性别分布
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getGenderCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            queryWrapper.in("sex", 1); //性别(0:未知,1:男,2:女)
            int count1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", 0);
            queryWrapper2.in("dept_id", ids);
            queryWrapper2.in("sex", 2); //性别(0:未知,1:男,2:女)
            int count2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);

            Map<String, Object> map = new HashMap<>();
            map.put("man", count1);
            map.put("woman", count2);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 流动党员统计
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getFloatingPartyMemberCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            queryWrapper.eq("is_flow", 1); //是否流动党员(0:否,1:是)
            int count1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", 0);
            queryWrapper2.in("dept_id", ids);
            queryWrapper2.eq("is_flow", 0); //是否流动党员(0:否,1:是)
            int count2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);

            Map<String, Object> map = new HashMap<>();
            map.put("floating", count1);
            map.put("notFloating", count2);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 年龄分布
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getAgeDistribution(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("sum(case when ("+ year +" - YEAR(birthday)) < 36 then 1 else 0 end ) as a," +
                    "sum(case when ("+ year +" - YEAR(birthday)) > 35 and ("+ year +" - YEAR(birthday)) < 41 then 1 else 0 end ) as b," +
                    "sum(case when ("+ year +" - YEAR(birthday)) > 40 and ("+ year +" - YEAR(birthday)) < 46 then 1 else 0 end ) as c," +
                    "sum(case when ("+ year +" - YEAR(birthday)) > 45 and ("+ year +" - YEAR(birthday)) < 51 then 1 else 0 end ) as d," +
                    "sum(case when ("+ year +" - YEAR(birthday)) > 50 and ("+ year +" - YEAR(birthday)) < 55 then 1 else 0 end ) as e," +
                    "sum(case when ("+ year +" - YEAR(birthday)) > 54 then 1 else 0 end ) as f");
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            List<Map<String, Object>> list = jcxfPartyMemberMapper.selectMaps(queryWrapper);
            if (list != null && list.size() == 1 && list.get(0) != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list.get(0)));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 党龄分布
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getPartyStandingDistribution(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("sum(case when ("+ year +" - YEAR(party_time)) < 11 then 1 else 0 end ) as a," +
                    "sum(case when ("+ year +" - YEAR(party_time)) > 10 and ("+ year +" - YEAR(party_time)) < 21 then 1 else 0 end ) as b," +
                    "sum(case when ("+ year +" - YEAR(party_time)) > 20 and ("+ year +" - YEAR(party_time)) < 31 then 1 else 0 end ) as c," +
                    "sum(case when ("+ year +" - YEAR(party_time)) > 30 and ("+ year +" - YEAR(party_time)) < 41 then 1 else 0 end ) as d," +
                    "sum(case when ("+ year +" - YEAR(party_time)) > 40 and ("+ year +" - YEAR(party_time)) < 51 then 1 else 0 end ) as e," +
                    "sum(case when ("+ year +" - YEAR(party_time)) > 50 then 1 else 0 end ) as f");
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            List<Map<String, Object>> list = jcxfPartyMemberMapper.selectMaps(queryWrapper);
            if (list != null && list.size() == 1 && list.get(0) != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list.get(0)));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 换届信息统计
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getTermInfo(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper<JcxfBranchReelection> queryWrapper = new QueryWrapper();
            queryWrapper.select("DISTINCT dept_id");
            queryWrapper.eq("del_flag", false);
            queryWrapper.in("dept_id", ids);
            List<JcxfBranchReelection> list = jcxfTermDeptMapper.selectList(queryWrapper);
            List<Long> deptIds = list.stream().map(item -> item.getDeptId()).collect(Collectors.toList());


            if(deptIds.size() > 0) {
                 //分类统计各类型部门的数量
                // 611 党委 911 临时党委 621 党总支部 921 临时党总支部 631 党支部 931 临时党支部 632 联合党支部 932 临时联合党支部
                List<String> typeList = new ArrayList<>();
                typeList.add("611");
                typeList.add("911");
                typeList.add("621");
                typeList.add("921");
                typeList.add("631");
                typeList.add("931");
                typeList.add("632");
                typeList.add("932");

                QueryWrapper q = new QueryWrapper();
                q.eq("del_flag", false);
                q.select("sum(case when type = 611 or type = 911 then 1 else 0 end ) as dw," +
                        "sum(case when type = 621 or type = 921 then 1 else 0 end ) as dzzb," +
                        "sum(case when type = 631 or type = 931 then 1 else 0 end ) as dzb," +
                        "sum(case when type = 632 or type = 932 then 1 else 0 end ) as lhdzb");
                q.in("type", typeList);
                q.in("id", deptIds);
                List<Map<String, Object>> numList = jcxfSysDeptMapper.selectMaps(q);

                QueryWrapper q2 = new QueryWrapper();
                q2.eq("del_flag", false);
                q2.select("sum(case when type = 611 or type = 911 then 1 else 0 end ) as dw," +
                        "sum(case when type = 621 or type = 921 then 1 else 0 end ) as dzzb," +
                        "sum(case when type = 631 or type = 931 then 1 else 0 end ) as dzb," +
                        "sum(case when type = 632 or type = 932 then 1 else 0 end ) as lhdzb");
                q2.in("type", typeList);
                ids.removeAll(deptIds);
                List<Map<String, Object>> numList2 = null;
                if(ids.size() > 0){
                    q2.in("id", ids);
                    numList2 = jcxfSysDeptMapper.selectMaps(q2);
                }

                Map<String, Object> result = new HashMap<>();
                result.put("term", numList != null && numList.size() == 1 && numList.get(0) != null? numList.get(0): null);
                result.put("notTerm", numList2 != null && numList2.size() == 1 && numList2.get(0) != null? numList2.get(0): null);

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 获取党费缴纳近一年每月的数据
     */
    @Override
    public Map<String, Object> getPayFeeNearlyYearCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            // 时间范围
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            String endTime = format.format(calendar.getTime());

            calendar.set(calendar.get(Calendar.YEAR) -1, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
            String startTime = format.format(calendar.getTime());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("pay_month as time," +
                    "sum(case when `status`= '1' then 1 else 0 end ) as payNum," +
                    "count(id) as allNum");
            queryWrapper.ge("pay_month", startTime);
            queryWrapper.le("pay_month", endTime);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            queryWrapper.groupBy("pay_month");

            List<Map<String, Object>> list = tzPayFeeDetailMapper.selectMaps(queryWrapper);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 近一年的三会一课每月开展数量
     */
    @Override
    public Map<String, Object> getMeetingNearlyYearCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            // 时间范围
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
            String endTime = format.format(calendar.getTime());

            calendar.set(calendar.get(Calendar.YEAR) -1, calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE), 0, 0, 0);
            String startTime = format.format(calendar.getTime());

            List<Integer> statusList = new ArrayList<>();
            // statusList.add(2);
            statusList.add(3);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("DATE_FORMAT(start_time, '%Y-%m') as time," +
                    "count(id) as num");
            queryWrapper.eq("meeting_type", 1);
            queryWrapper.in("meeting_status", statusList); // 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            queryWrapper.ge("DATE_FORMAT(start_time, '%Y-%m')", startTime);
            queryWrapper.le("DATE_FORMAT(start_time, '%Y-%m')", endTime);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            queryWrapper.groupBy("DATE_FORMAT(start_time, '%Y-%m')");

            List<Map<String, Object>> list = jcxfOrganizationLifeMapper.selectMaps(queryWrapper);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberYuBeiCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("person_type", 4); // 发展纪实阶段（1.递交入党申请书，2.列为入党积极分子，3.确定为发展对象，4.讨论吸收为预备党员，5.转为正式党员
            queryWrapper.in("dept_id", ids);
            queryWrapper.isNull("party_member_id");
            int count = jcxfDevelopPartyMapper.selectCount(queryWrapper);

            QueryWrapper q = new QueryWrapper();
            q.eq("del_flag", 0);
            q.eq("person_type", "2");
            q.in("dept_id", ids);
            int count2 = jcxfPartyMemberMapper.selectCount(q);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count + count2));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyWorkerCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("is_party_worker", "1"); // 是否是党务工作者（0否1是）
            queryWrapper.in("dept_id", ids);
            int count1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", false);
            queryWrapper2.in("dept_id", ids);
            int count2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);

            Map<String, Object> map = new HashMap<>();
            map.put("isPartyWorker", count1);
            map.put("notPartyWorker", count2 - count1);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyLostCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag" ,false);
            queryWrapper.eq("dept_id", ids);
            queryWrapper.eq("is_lost", "1"); // 是否失联党员(0:否,1:是)
            int count1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", false);
            queryWrapper2.in("dept_id", ids);
            int count2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);

            Map<String, Object> map = new HashMap<>();
            map.put("isLost", count1);
            map.put("notLost", count2 - count1);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPositionDistribution(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.select("detail");
            queryWrapper1.eq("del_flag", false);
            queryWrapper1.eq("code", "position");
            List<JcxfSysDictionary> dictionaryList = jcxfSysDictionaryMapper.selectList(queryWrapper1);
            List<String> positionList = new ArrayList<>();
            for (JcxfSysDictionary dictionary: dictionaryList) {
                positionList.add(dictionary.getDetail());
            }


            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("position,count(id) as num");
            queryWrapper.eq("del_flag", false);
            queryWrapper.in("position", positionList);
            queryWrapper.in("dept_id", ids);
            queryWrapper.groupBy("position");
            List<Map<String, Object>> list = jcxfPartyMemberMapper.selectMaps(queryWrapper);

            QueryWrapper<JcxfPartyMember> q = new QueryWrapper();
            q.eq("del_flag", false);
            q.in("dept_id", ids);
            q.and(i -> i.notIn("position", positionList).or(i2 -> i2.isNull("position")));
            int count = jcxfPartyMemberMapper.selectCount(q);

            Map<String, Object> map = new HashMap<>();
            map.put("no", count);
            map.put("position", list != null && list.size() > 0? list:null);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPayFeeTotal(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("GROUP_CONCAT(id) as ids");
            queryWrapper.in("dept_id", ids);
            queryWrapper.in("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper.eq("del_flag", 0);
            List<Map<String, Object>> list = tzPayFeeDetailMapper.selectMaps(queryWrapper);
            if (list != null && list.size() == 1 && list.get(0) != null) {
                List<String> detailIds = Arrays.asList(list.get(0).get("ids").toString().split(","));

                QueryWrapper q = new QueryWrapper();
                q.select("sum(should_pay) as should_pay");
                q.eq("del_flag", 0);
                q.eq("order_status", 2);
                q.in("pay_fee_detail_id", detailIds);
                TzPayFeeDetailLog log = tzPayFeeDetailLogMapper.selectOne(q);

                if (log != null) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(log.getShouldPay()));
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(0));
                }
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

}

package me.flyray.bsin.server.impl.jcxf;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import me.flyray.bsin.server.domain.bsin.BsinSysTenant;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.mapper.BsinSysTenantMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfPayFeeService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDeptChildGroup;
import me.flyray.bsin.server.mapper.TzPayFeeDetailLogMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.mapper.TzPayFeeDetailMapper;
import me.flyray.bsin.server.mapper.TzPayFeeMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.payUtils.JeepayHelper;
import me.flyray.bsin.server.utils.payUtils.JeepayResult;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:23
 * @description：
 * @modified By：
 */

public class JcxfPayFeeServiceImpl implements JcxfPayFeeService {

    @Autowired
    private TzPayFeeMapper tzPayFeeMapper;
    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzPayFeeDetailLogMapper tzPayFeeDetailLogMapper;
    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;
    @Autowired
    private BsinSysTenantMapper bsinSysTenantMapper;

    @Override
    public Map<String, Object> queryFeeTotalByYear2(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String deptId = (String) map.get("deptId");
            String year = (String) map.get("year");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            // 查询所有下一级部门列表
            /*QueryWrapper q = new QueryWrapper();
            q.select("id, name");
            q.eq("del_flag", 0);
            q.eq("parent_id", deptId);
            List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(q);
            if (deptList.size() == 0) {
                deptList.add(jcxfSysDeptMapper.selectById(deptId));
            }*/
            List<JcxfSysDeptChildGroup> deptList = jcxfSysDeptMapper.getChildDeptMap(Integer.parseInt(deptId),pageVo.getPageNumber(),pageVo.getPageSize());

            String[] arr = {year + "-01", year + "-02", year + "-03", year + "-04", year + "-05", year + "-06", year + "-07", year + "-08", year + "-09", year + "-10", year + "-11", year + "-12"};
            String[] arr2 = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"};

            // 合计map
            Map<String, Object> totalMap = new HashMap<>();
            totalMap.put("deptName", "合计");

            // 记录每一个部门当年每月的缴纳
            List<Map<String, Object>> resultMap = new ArrayList<>();
            if(deptList.size()  == 0){//没有下级部门
                JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectById(deptId);
                JcxfSysDeptChildGroup jcxfSysDeptChildGroup = new JcxfSysDeptChildGroup();
                jcxfSysDeptChildGroup.setId(Integer.parseInt(deptId));
                jcxfSysDeptChildGroup.setName(jcxfSysDept.getName());
                List<Integer> deptIds = new ArrayList<>();
                jcxfSysDeptChildGroup.setChildIds(deptIds);
                deptList.add(jcxfSysDeptChildGroup);
            }
            for (JcxfSysDeptChildGroup dept: deptList) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("deptName", dept.getName());
                dataMap.put("deptId", dept.getId());

                // List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(dept.getId()));
                List<Integer> deptIds = dept.getChildIds();
                deptIds.add(dept.getId());

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.select("pay_month,sum(should_pay) as should_pay");
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("status", 1);
                queryWrapper.in("dept_id", deptIds);
                queryWrapper.likeRight("pay_month", year);
                queryWrapper.groupBy("pay_month");
                List<TzPayFeeDetail> list = tzPayFeeDetailMapper.selectList(queryWrapper);
                Map<String, TzPayFeeDetail> logMap = new HashMap<>();
                for (TzPayFeeDetail payFeeDetail: list) {
                    logMap.put(payFeeDetail.getPayMonth(), payFeeDetail);
                }

                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.select("pay_month,sum(should_pay) as should_pay");
                queryWrapper1.eq("del_flag", 0);
                queryWrapper1.in("dept_id", deptIds);
                queryWrapper1.likeRight("pay_month", year);
                queryWrapper1.groupBy("pay_month");
                List<TzPayFeeDetail> detailList = tzPayFeeDetailMapper.selectList(queryWrapper1);
                Map<String, TzPayFeeDetail> detailMap = new HashMap<>();
                for (TzPayFeeDetail detail: detailList) {
                    detailMap.put(detail.getPayMonth(), detail);
                }

                for (int i = 0; i < arr.length; i++) {
                    if (totalMap.get(arr2[i]) == null) {
                        JSONObject json = new JSONObject();
                        json.put("actuallyPay", new BigDecimal("0"));
                        json.put("shouldPay", new BigDecimal("0"));
                        totalMap.put(arr2[i], json);
                    }

                    if (logMap.get(arr[i]) != null) {
                        if (dataMap.get(arr2[i]) == null) {
                            JSONObject json = new JSONObject();
                            json.put("actuallyPay", logMap.get(arr[i]).getShouldPay());
                            dataMap.put(arr2[i], json);
                        } else {
                            JSONObject thisJson = (JSONObject) dataMap.get(arr2[i]);
                            thisJson.put("shouldPay", logMap.get(arr[i]).getShouldPay());
                            dataMap.put(arr2[i], thisJson);
                        }

                        // 相加
                        JSONObject totalJson = (JSONObject) totalMap.get(arr2[i]);
                        BigDecimal decimal = (BigDecimal) totalJson.get("actuallyPay");
                        totalJson.put("actuallyPay", decimal.add(logMap.get(arr[i]).getShouldPay()));
                        totalMap.put(arr2[i], totalJson);
                    }

                    if (detailMap.get(arr[i]) != null) {
                        if (dataMap.get(arr2[i]) == null) {
                            JSONObject json = new JSONObject();
                            json.put("shouldPay", detailMap.get(arr[i]).getShouldPay());
                            dataMap.put(arr2[i], json);
                        } else {
                            JSONObject thisJson = (JSONObject) dataMap.get(arr2[i]);
                            thisJson.put("shouldPay", detailMap.get(arr[i]).getShouldPay());
                            dataMap.put(arr2[i], thisJson);
                        }


                        // 相加
                        JSONObject totalJson = (JSONObject) totalMap.get(arr2[i]);
                        BigDecimal decimal = (BigDecimal) totalJson.get("shouldPay");
                        totalJson.put("shouldPay", decimal.add(detailMap.get(arr[i]).getShouldPay()));
                        totalMap.put(arr2[i], totalJson);
                    }

                    if (dataMap.get(arr2[i]) == null) {
                        JSONObject json = new JSONObject();
                        json.put("actuallyPay", new BigDecimal("0"));
                        json.put("shouldPay", new BigDecimal("0"));
                        dataMap.put(arr2[i], json);
                    }
                }
                resultMap.add(dataMap);
            }

            resultMap.add(0, totalMap);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(resultMap));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryFeeTotalByYear(Map<String, Object> requestMap) {
        Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");
        TzPayFee tzPayFee = JSON.parseObject(JSON.toJSONString(resultMap.get("tzSysDept")), TzPayFee.class);
        Map<Integer, List<Long>> mapDeptIds = new HashMap<>();

        try {
            if (tzPayFee != null && tzPayFee.getDeptId() != null) {

                QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<JcxfSysDept>();
                queryWrapper.select("id");
                queryWrapper.eq("parent_id", tzPayFee.getDeptId());
                queryWrapper.eq("del_flag", 0);
                List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(queryWrapper);//根据部门id查询下面的第一层子集
                List<Long> listOne = new ArrayList<>(); // 装父本级id
                Integer index = 1;
                listOne.add(tzPayFee.getDeptId());
                mapDeptIds.put(0, listOne);
                for (JcxfSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();

                    List<Long> deptids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(id));
                    deptids.add(Long.valueOf(id));

                    /*QueryWrapper<TzSysDept> wrapper = new QueryWrapper<>();
                    wrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + id + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), id)));
                    wrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0).eq("tz_sys_dept.tenant_id",tenantId));
                    List<TzSysDept> depts = jcxfSysDeptMapper.selectList(wrapper);//根据第一层级，查询该层级下面的所有子集

                    if (depts != null && depts.size() > 0) {
                        for (TzSysDept dept : depts) {
                            deptids.add(dept.getId());//分别把所有每一层的所有子集装起来
                        }
                    }*/
                    mapDeptIds.put(index, deptids);//再用mapDeptIds把带有每一层所有子集的list装起来
                    index++;
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为，请联系管理员");
            }

            String year = tzPayFee.getYear();
            tzPayFee.setDelFlag(0);
            String[] arr = {year + "-01", year + "-02", year + "-03", year + "-04", year + "-05", year + "-06", year + "-07", year + "-08", year + "-09", year + "-10", year + "-11", year + "-12"};
            Map<String, BigDecimal> map = null;
            List<Map<String, Object>> mapList = new ArrayList<>();

            for (Map.Entry<Integer, List<Long>> entry : mapDeptIds.entrySet()) {
                Map<String, Object> maps = new HashMap<>();
                BigDecimal one = new BigDecimal("0.00");
                BigDecimal two = new BigDecimal("0.00");
                BigDecimal three = new BigDecimal("0.00");
                BigDecimal four = new BigDecimal("0.00");
                BigDecimal five = new BigDecimal("0.00");
                BigDecimal six = new BigDecimal("0.00");
                BigDecimal seven = new BigDecimal("0.00");
                BigDecimal eight = new BigDecimal("0.00");
                BigDecimal nine = new BigDecimal("0.00");
                BigDecimal ten = new BigDecimal("0.00");
                BigDecimal eleven = new BigDecimal("0.00");
                BigDecimal twelve = new BigDecimal("0.00");
                List<Long> value = entry.getValue();
                if (value != null && value.size() > 0) {
                    JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(value.get(0));
                    if (tzSysDept != null) {
                        String name = tzSysDept.getName();
                        maps.put("deptName", name);
                    }
                }

                map = tzPayFeeMapper.queryFeeTotalByYears(entry.getValue(), arr);
                if (map != null && map.size() > 0) {
                    one = one.add(map.get("one") != null ? map.get("one") : new BigDecimal("0.00"));
                    two = two.add(map.get("two") != null ? map.get("two") : new BigDecimal("0.00"));
                    three = three.add(map.get("three") != null ? map.get("three") : new BigDecimal("0.00"));
                    four = four.add(map.get("four") != null ? map.get("four") : new BigDecimal("0.00"));
                    five = five.add(map.get("five") != null ? map.get("five") : new BigDecimal("0.00"));
                    six = six.add(map.get("six") != null ? map.get("six") : new BigDecimal("0.00"));
                    seven = seven.add(map.get("seven") != null ? map.get("seven") : new BigDecimal("0.00"));
                    eight = eight.add(map.get("eight") != null ? map.get("eight") : new BigDecimal("0.00"));
                    nine = nine.add(map.get("nine") != null ? map.get("nine") : new BigDecimal("0.00"));
                    ten = ten.add(map.get("ten") != null ? map.get("ten") : new BigDecimal("0.00"));
                    eleven = eleven.add(map.get("eleven") != null ? map.get("eleven") : new BigDecimal("0.00"));
                    twelve = twelve.add(map.get("twelve") != null ? map.get("twelve") : new BigDecimal("0.00"));
                }

                /*for (Integer deptId : entry.getValue()) {
                    map = tzPayFeeMapper.queryFeeTotalByYear(deptId, arr, entry.getValue());
                    if (map != null && map.size() > 0) {
                        one = one.add(map.get("one") != null ? map.get("one") : new BigDecimal("0.00"));
                        two = two.add(map.get("two") != null ? map.get("two") : new BigDecimal("0.00"));
                        three = three.add(map.get("three") != null ? map.get("three") : new BigDecimal("0.00"));
                        four = four.add(map.get("four") != null ? map.get("four") : new BigDecimal("0.00"));
                        five = five.add(map.get("five") != null ? map.get("five") : new BigDecimal("0.00"));
                        six = six.add(map.get("six") != null ? map.get("six") : new BigDecimal("0.00"));
                        seven = seven.add(map.get("seven") != null ? map.get("seven") : new BigDecimal("0.00"));
                        eight = eight.add(map.get("eight") != null ? map.get("eight") : new BigDecimal("0.00"));
                        nine = nine.add(map.get("nine") != null ? map.get("nine") : new BigDecimal("0.00"));
                        ten = ten.add(map.get("ten") != null ? map.get("ten") : new BigDecimal("0.00"));
                        eleven = eleven.add(map.get("eleven") != null ? map.get("eleven") : new BigDecimal("0.00"));
                        twelve = twelve.add(map.get("twelve") != null ? map.get("twelve") : new BigDecimal("0.00"));
                    }
                }*/

                maps.put("one", one);
                maps.put("two", two);
                maps.put("three", three);
                maps.put("four", four);
                maps.put("five", five);
                maps.put("six", six);
                maps.put("seven", seven);
                maps.put("eight", eight);
                maps.put("nine", nine);
                maps.put("ten", ten);
                maps.put("eleven", eleven);
                maps.put("twelve", twelve);
                maps.put("total", one.add(two).add(three).add(four).add(five).add(six).add(seven).add(eight).add(nine).add(ten).add(eleven).add(twelve));

                maps.put("thisMonth", Calendar.MONTH + 2);
                mapList.add(maps);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(mapList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryTzPayFeeList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");
            TzPayFee tzPayFee = JSON.parseObject(JSON.toJSONString(resultMap.get("tzPayFee")), TzPayFee.class);
            String startPayMonth = (String) resultMap.get("startPayMonth");
            String endPayMonth = (String) resultMap.get("endPayMonth");
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(resultMap.get("pageVo")), PageVo.class);
            // String searchMoth = (String) resultMap.get("searchMoth");

            tzPayFee.setDelFlag(0);

            List<Long> listAll = new ArrayList<>();
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            /*if (StringUtils.isNotBlank(searchMoth)) {
                tzPayFee.setPayMonth(searchMoth);
            }*/
            //根据党组织id查询该组织下辖的所有组织的id
            if (tzPayFee.getDeptId() != null) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(tzPayFee.getDeptId());
                listAll.add(tzPayFee.getDeptId());
            }
            try {
                IPage<TzPayFee> result = getTzPayFeeByPage(tzPayFee, startPayMonth, endPayMonth, searchVo, pageVo, listAll);
//                for (int i = 0; i < result.getRecords().size(); i++) {
//                    // 获取所有缴费信息id
//                    QueryWrapper q = new QueryWrapper();
//                    q.select("id, should_pay");
//                    q.eq("pay_fee_id", result.getRecords().get(i).getId());
//                    q.eq("pay_month", result.getRecords().get(i).getPayMonth());
//                    q.eq("del_flag", 0);
//                    List<TzPayFeeDetail> detailList = tzPayFeeDetailMapper.selectList(q);
//
//                    BigDecimal shouldPay = new BigDecimal("0.00");
//                    if (detailList !=null && detailList.size() > 0) {
//                        List<Long> ids = new ArrayList();
//                        for (TzPayFeeDetail detail : detailList) {
//                            ids.add(detail.getId());
//                            shouldPay = shouldPay.add(detail.getShouldPay());
//                        }
//                        result.getRecords().get(i).setShouldPay(shouldPay);
//
//                        QueryWrapper queryWrapper = new QueryWrapper();
//                        // queryWrapper.select("sum(amount) as amount");
//                        queryWrapper.select("sum(should_pay) as should_pay");
//                        queryWrapper.in("pay_fee_detail_id", ids);
//                        queryWrapper.eq("order_status", 2);
//                        queryWrapper.eq("del_flag", 0);
//                        TzPayFeeDetailLog log = tzPayFeeDetailLogMapper.selectOne(queryWrapper);
//                        if (log != null) {
//                            // BigDecimal actuallyPay = new BigDecimal(String.valueOf(log.getAmount())).divide(new BigDecimal("100"));
//                            BigDecimal actuallyPay = new BigDecimal(String.valueOf(log.getShouldPay()));
//                            result.getRecords().get(i).setActuallyPay(actuallyPay);
//                        }
//                    } else {
//                        result.getRecords().get(i).setActuallyPay(new BigDecimal("0.00"));
//                    }
//
//                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> downloadTzPayFeeList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");
            TzPayFee tzPayFee = JSON.parseObject(JSON.toJSONString(resultMap.get("tzPayFee")), TzPayFee.class);
            String startPayMonth = (String) resultMap.get("startPayMonth");
            String endPayMonth = (String) resultMap.get("endPayMonth");
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);

            tzPayFee.setDelFlag(0);

            List<Long> listAll = new ArrayList<>();
            //根据党组织id查询该组织下辖的所有组织的id
            if (tzPayFee.getDeptId() != null) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(tzPayFee.getDeptId());
                listAll.add(tzPayFee.getDeptId());
            }
            try {
                List<TzPayFee> result = queryTzPayFeeList(tzPayFee, startPayMonth, endPayMonth, searchVo, listAll);
                for (int i = 0; i < result.size(); i++) {
                    // 获取所有缴费信息id
                    QueryWrapper q = new QueryWrapper();
                    q.select("id, should_pay");
                    q.eq("pay_fee_id", result.get(i).getId());
                    q.eq("pay_month", result.get(i).getPayMonth());
                    q.eq("del_flag", 0);
                    List<TzPayFeeDetail> detailList = tzPayFeeDetailMapper.selectList(q);

                    BigDecimal shouldPay = new BigDecimal("0.00");
                    if (detailList !=null && detailList.size() > 0) {
                        List<Long> ids = new ArrayList();
                        for (TzPayFeeDetail detail : detailList) {
                            ids.add(detail.getId());
                            shouldPay = shouldPay.add(detail.getShouldPay());
                        }
                        result.get(i).setShouldPay(shouldPay);

                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.select("sum(should_pay) as should_pay");
                        queryWrapper.in("pay_fee_detail_id", ids);
                        queryWrapper.eq("order_status", 2);
                        queryWrapper.eq("del_flag", 0);
                        TzPayFeeDetailLog log = tzPayFeeDetailLogMapper.selectOne(queryWrapper);
                        if (log != null) {
                            BigDecimal actuallyPay = new BigDecimal(String.valueOf(log.getShouldPay()));
                            result.get(i).setActuallyPay(actuallyPay);
                        }
                    } else {
                        result.get(i).setActuallyPay(new BigDecimal("0.00"));
                    }

                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
            } catch (Exception e) {
                e.printStackTrace();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> copyDataByMonth(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String copyMonth = (String) requestMap.get("copyMonth");
            String payMonth = (String) requestMap.get("payMonth");

            //先通过copyMonth查找记录表，看原记录是否存在
            QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
            queryWrapper.and(i -> i.eq("tz_pay_fee.dept_id", deptId));
            queryWrapper.and(i -> i.like("tz_pay_fee.pay_month", copyMonth));
            queryWrapper.and(i -> i.eq("tz_pay_fee.del_flag", 0));
            TzPayFee one = tzPayFeeMapper.selectOne(queryWrapper);
            if (one == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"所选月份暂无记录，无法复制");
            }
            //获取主表id作为详情表的外键id来查询
            Long payFeeId = one.getId();

            //查询当前是否已经存在数据，如果存在则不允许重复添加
            QueryWrapper<TzPayFee> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.and(i -> i.eq("tz_pay_fee.dept_id", deptId));
            queryWrapper2.and(i -> i.like("tz_pay_fee.pay_month", payMonth));
            queryWrapper2.and(i -> i.eq("tz_pay_fee.del_flag", 0));
            TzPayFee tzPayFee = tzPayFeeMapper.selectOne(queryWrapper2);
            if (tzPayFee != null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"缴纳年月存在数据，不能重复导入!");
            }

            //修改并保存数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            one.setPayMonth(payMonth);
            one.setStatus(0);
            one.setActuallyPay(new BigDecimal("0.00"));
            one.setDelFlag(0);
            one.setCreateTime(new Date());
            one.setId(null);
            int save = tzPayFeeMapper.insert(one);
            if (save > 0) {
                //查询详情表数据并保存为缴纳月数据
                QueryWrapper<TzPayFeeDetail> payFeeDetailQueryWrapper = new QueryWrapper<>();
                payFeeDetailQueryWrapper.and(i -> i.eq("tz_pay_fee_detail.pay_fee_id", payFeeId));
                payFeeDetailQueryWrapper.and(i -> i.like("tz_pay_fee_detail.pay_month", copyMonth));
                payFeeDetailQueryWrapper.and(i -> i.eq("tz_pay_fee_detail.del_flag", 0));
                List<TzPayFeeDetail> tzPayFeeDetails = tzPayFeeDetailMapper.selectList(payFeeDetailQueryWrapper);

                if (tzPayFeeDetails != null && tzPayFeeDetails.size() > 0) {

                    Boolean flag = true;
                    for (TzPayFeeDetail tzPayFeeDetail : tzPayFeeDetails) {
                        tzPayFeeDetail.setId(null);
                        tzPayFeeDetail.setPayMonth(payMonth);
                        tzPayFeeDetail.setPayFeeId(one.getId());
                        tzPayFeeDetail.setCreateTime(new Date());
                        tzPayFeeDetail.setDelFlag(0);
                        tzPayFeeDetail.setActuallyPay(new BigDecimal("0.00"));
                        tzPayFeeDetail.setStatus(0);
                        int res = tzPayFeeDetailMapper.insert(tzPayFeeDetail);
                        if (res < 1) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("复制成功"));
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"复制异常");
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"复制数据为空");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"复制失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"复制异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> deleteById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            // 查询是否有已经支付的缴费
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper.eq("pay_fee_id", id);
            queryWrapper.eq("del_flag", "0");
            int count = tzPayFeeDetailMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在已支付缴费");
            }

            //修改tz_pay_fee表里的del_flag状态，即逻辑删除
            UpdateWrapper<TzPayFee> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_pay_fee.del_flag", 1).in("tz_pay_fee.id", id);
            tzPayFeeMapper.update(null, updateWrapper);

            //查询所有的订单日志
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.select("id");
            queryWrapper1.eq("pay_fee_id", id);
            List<TzPayFeeDetail> tzPayFeeDetailList = tzPayFeeDetailMapper.selectList(queryWrapper1);
            List<Long> detailIds = new ArrayList<>();
            for (TzPayFeeDetail tzPayFeeDetail : tzPayFeeDetailList) {
                detailIds.add(tzPayFeeDetail.getId());
            }

            //删除成功后再根据之前拿到的详情记录id集合，去修改对应的详情记录
            UpdateWrapper<TzPayFeeDetail> detailUpdateWrapper = new UpdateWrapper<>();
            detailUpdateWrapper.set("tz_pay_fee_detail.del_flag", 1).eq("tz_pay_fee_detail.pay_fee_id", id);
            tzPayFeeDetailMapper.update(null, detailUpdateWrapper);

            //删除订单日志
            if (detailIds != null && detailIds.size() > 0) {
                UpdateWrapper logUpdateWrapper = new UpdateWrapper();
                logUpdateWrapper.in("pay_fee_detail_id", detailIds);
                logUpdateWrapper.set("del_flag", 1);
                tzPayFeeDetailLogMapper.update(null, logUpdateWrapper);
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> getCopyInfoByTime(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String copyMonth = (String) requestMap.get("copyMonth");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("pay_month", copyMonth);
            TzPayFee tzPayFee = tzPayFeeMapper.selectOne(queryWrapper);
            if (tzPayFee == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到该时间的信息");
            }

            QueryWrapper q2 = new QueryWrapper();
            q2.eq("pay_fee_id", tzPayFee.getId());
            q2.eq("del_flag", 0);
            List<TzPayFeeDetail> tzPayFeeDetailList = tzPayFeeDetailMapper.selectList(q2);

            Map<String, Object> map = new HashMap<>();
            map.put("tzPayFee", tzPayFee);
            map.put("tzPayFeeDetailList", tzPayFeeDetailList);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    @Override
    public Map<String, Object> syncPayResultById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            QueryWrapper q = new QueryWrapper();
            q.eq("pay_fee_id", id);
            q.eq("del_flag", 0);
            List<TzPayFeeDetail> detailList = tzPayFeeDetailMapper.selectList(q);
            for (TzPayFeeDetail detail : detailList) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("pay_fee_detail_id", detail.getId());
                queryWrapper.eq("del_flag", 0);

                List<TzPayFeeDetailLog> logList = tzPayFeeDetailLogMapper.selectList(queryWrapper);
                for (TzPayFeeDetailLog log : logList) {
                    QueryWrapper<JcxfPartyMember> dyQueryWrapper = new QueryWrapper<>();
                    dyQueryWrapper.eq("party_member.id", log.getPartyMemberId()).eq("party_member.del_flag", 0);
                    JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectOne(dyQueryWrapper);
                    BsinSysTenant sysTenant = null;
                    if(jcxfPartyMember != null){
                        if(StringUtils.isNotBlank(jcxfPartyMember.getTenantId())){
                            sysTenant = bsinSysTenantMapper.getSysTenantInfo(jcxfPartyMember.getTenantId());
                        }else{
                            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "您暂无组织信息无法支付");
                        }
                    }
                    JeepayResult jeepayResult = JeepayHelper.payQuery(log.getOrderNo(),sysTenant);
                    // 如果查询到订单信息
                    if (jeepayResult != null && jeepayResult.getCode() != null && jeepayResult.getCode().intValue() == 0) {
                        // 更新订单信息
                        UpdateWrapper<TzPayFeeDetailLog> updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("order_no", log.getOrderNo());
                        updateWrapper.set("order_status", jeepayResult.getData().getState());
                        updateWrapper.set("amount", jeepayResult.getData().getAmount());
                        updateWrapper.set("pay_result", jeepayResult.toString());
                        updateWrapper.set("pay_result_time", new Date());
                        if (jeepayResult.getData().getState() == 2) {
                            Date successTime = new Date(jeepayResult.getData().getSuccessTime());
                            // 修改党费信息状态
                            UpdateWrapper updateWrapper1 = new UpdateWrapper();
                            updateWrapper1.eq("id", log.getPayFeeDetailId());
                            updateWrapper1.set("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
                            updateWrapper1.set("pay_time", successTime);
                            tzPayFeeDetailMapper.update(null, updateWrapper1);

                            updateWrapper.set("success_time", successTime);
                        } else {
                            updateWrapper.set("success_time", null);
                        }
                        tzPayFeeDetailLogMapper.update(null, updateWrapper);
                    } else {
                        UpdateWrapper<TzPayFeeDetailLog> updateWrapper = new UpdateWrapper();
                        updateWrapper.eq("order_no", log.getOrderNo());
                        updateWrapper.set("order_status", jeepayResult.getCode());
                        updateWrapper.set("amount", null);
                        updateWrapper.set("pay_result", jeepayResult.getMsg());
                        updateWrapper.set("pay_result_time", new Date());
                        updateWrapper.set("success_time", null);
                        tzPayFeeDetailLogMapper.update(null, updateWrapper);
                    }
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"同步异常");
        }
    }

    @Override
    public Map<String, Object> getDeptCount(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String deptId = (String) map.get("deptId");
        List<JcxfSysDeptChildGroup> deptList = jcxfSysDeptMapper.getChildDeptMap(Integer.parseInt(deptId),null,null);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(deptList.size()));
    }

    public IPage<TzPayFee> queryTzPayFeeListByPage(TzPayFee tzPayFee, String startPayMonth, String endPayMonth, SearchVo searchVo, PageVo pageVo, List<Long> listAll) {
        int page = 1;
        int limit = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
        }
        Page<TzPayFee> pageData = new Page<>(page, limit);
        QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
        if (tzPayFee != null) {
            queryWrapper = LikeAllField(tzPayFee, searchVo);
        }
        //根据所属组织id集合查询所属的部门缴费记录
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("tz_pay_fee.dept_id", listAll));
        }

        if (StringUtils.isNotBlank(startPayMonth)) {
            queryWrapper.ge("tz_pay_fee.pay_month", startPayMonth);
        }
        if (StringUtils.isNotBlank(endPayMonth)) {
            queryWrapper.le("tz_pay_fee.pay_month", endPayMonth);
        }

        IPage<TzPayFee> result = tzPayFeeMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    public IPage<TzPayFee> getTzPayFeeByPage(TzPayFee tzPayFee, String startPayMonth, String endPayMonth, SearchVo searchVo, PageVo pageVo, List<Long> listAll) {
        int page = 1;
        int limit = 10;
        if (pageVo != null) {
            if (pageVo.getPageNumber() != 0) {
                page = pageVo.getPageNumber();
            }
            if (pageVo.getPageSize() != 0) {
                limit = pageVo.getPageSize();
            }
        }
        Page<TzPayFee> pageData = new Page<>(page, limit);
        QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
        if (tzPayFee != null) {
            queryWrapper = LikeAllField(tzPayFee, searchVo);
        }
        //根据所属组织id集合查询所属的部门缴费记录
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("tz_pay_fee.dept_id", listAll));
        }

        if (StringUtils.isNotBlank(startPayMonth)) {
            queryWrapper.ge("tz_pay_fee.pay_month", startPayMonth);
        }
        if (StringUtils.isNotBlank(endPayMonth)) {
            queryWrapper.le("tz_pay_fee.pay_month", endPayMonth);
        }
        queryWrapper.groupBy("tz_pay_fee.id");
        IPage<TzPayFee> result = tzPayFeeMapper.queryPayFeePage(pageData, queryWrapper);
        return result;
    }

    public List<TzPayFee> queryTzPayFeeList(TzPayFee tzPayFee, String startPayMonth, String endPayMonth, SearchVo searchVo, List<Long> listAll) {
        QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
        if (tzPayFee != null) {
            queryWrapper = LikeAllField(tzPayFee, searchVo);
        }
        //根据所属组织id集合查询所属的部门缴费记录
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("tz_pay_fee.dept_id", listAll));
        }

        if (StringUtils.isNotBlank(startPayMonth)) {
            queryWrapper.ge("pay_month", startPayMonth);
        }
        if (StringUtils.isNotBlank(endPayMonth)) {
            queryWrapper.le("pay_month", endPayMonth);
        }

        List<TzPayFee> result = tzPayFeeMapper.selectList(queryWrapper);
        return result;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzPayFee 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzPayFee> LikeAllField(TzPayFee tzPayFee, SearchVo searchVo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(tzPayFee.getPayMonth())) {
            queryWrapper.and(i -> i.eq("tz_pay_fee.pay_month", tzPayFee.getPayMonth()));
        }
        if (tzPayFee.getShouldPay() != null) {
            queryWrapper.and(i -> i.eq("tz_pay_fee.should_pay", tzPayFee.getShouldPay()));
        }
        if (tzPayFee.getActuallyPay() != null) {
            queryWrapper.and(i -> i.like("tz_pay_fee.actually_pay", tzPayFee.getActuallyPay()));
        }
        if (tzPayFee.getStatus() != null) {
            queryWrapper.and(i -> i.eq("tz_pay_fee.status", tzPayFee.getStatus()));
        }
        if (tzPayFee.getCreateTime() != null) {
            queryWrapper.and(i -> i.like("tz_pay_fee.create_time", tzPayFee.getCreateTime()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TzPayFee::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.and(i -> i.eq("tz_pay_fee.del_flag", 0));
        return queryWrapper;
    }
}


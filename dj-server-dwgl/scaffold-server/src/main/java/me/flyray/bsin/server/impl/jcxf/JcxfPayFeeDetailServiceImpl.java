package me.flyray.bsin.server.impl.jcxf;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.model.PayOrderCreateReqModel;
import com.jeequan.jeepay.request.PayOrderCreateRequest;
import com.jeequan.jeepay.response.PayOrderCreateResponse;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.facade.service.JcxfPayFeeDetailService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.common.exception.YamiShopBindException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.bsin.BsinSysTenant;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.jcxf.PayFeeImportEntity;
import me.flyray.bsin.server.domain.jcxf.RepayFeeEntity;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import me.flyray.bsin.server.domain.vo.PayFeeDetailsVo;
import me.flyray.bsin.server.mapper.BsinSysTenantMapper;
import me.flyray.bsin.server.mapper.TzPayFeeDetailLogMapper;
import me.flyray.bsin.server.mapper.TzPayFeeDetailMapper;
import me.flyray.bsin.server.mapper.TzPayFeeMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.scheduled.PayFeeTask;
import me.flyray.bsin.server.utils.AESUtil2;
import me.flyray.bsin.server.utils.AmountUtil;
import me.flyray.bsin.server.utils.RedisUtils;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.payUtils.JeepayHelper;
import me.flyray.bsin.server.utils.payUtils.JeepayProperties;
import me.flyray.bsin.server.utils.payUtils.JeepayResult;
import me.flyray.bsin.server.utils.payUtils.PayOrderQueryResModel;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class JcxfPayFeeDetailServiceImpl implements JcxfPayFeeDetailService {

    public static String basePath;

    @Value("${localFile.saveUrl}")
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzPayFeeMapper tzPayFeeMapper;

    @Autowired
    private TzPayFeeDetailLogMapper tzPayFeeDetailLogMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;
    @Autowired
    private BsinSysTenantMapper bsinSysTenantMapper;

    @Autowired(required = true)
    private RedisUtils redisUtils;

    @Autowired
    private JeepayProperties staticJeepayProperties;

    @Override
    public Map<String, Object> queryTzPayFeeDetailList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");

            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(resultMap.get("tzPayFeeDetail")), TzPayFeeDetail.class);
            // String searchMoth = (String) resultMap.get("searchMoth");
            String isBackTax = (String) resultMap.get("isBackTax");
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(resultMap.get("pageVo")), PageVo.class);

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(tzPayFeeDetail.getDeptId());
            tzPayFeeDetail.setDeptIds(deptIds);
            tzPayFeeDetail.setDelFlag(0);

            IPage<TzPayFeeDetail> result = queryTzPayFeeDetailListByPage(tzPayFeeDetail, isBackTax, searchVo, pageVo);
            Set<Long> partyIds = new HashSet<>();
            for (TzPayFeeDetail detail : result.getRecords()) {
                partyIds.add(detail.getPartyMemberId());
                if (detail.getInsteadPayPartyMemberId() != null) {
                    partyIds.add(detail.getInsteadPayPartyMemberId());
                }
            }
            QueryWrapper q = new QueryWrapper();
            q.in("id", partyIds);
            List<JcxfPartyMember> memberList = new ArrayList<>();
            if (partyIds.size() > 0) {
                memberList = jcxfPartyMemberMapper.selectList(q);
            }
            for (TzPayFeeDetail detail : result.getRecords()) {
                for (JcxfPartyMember member : memberList) {
                    if (member.getId().equals(detail.getPartyMemberId()) || member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                        if (member.getId().equals(detail.getPartyMemberId())) {
                            detail.setName(member.getRealName());
                        }
                        if (member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                            detail.setInsteadPayPartyMemberName(member.getRealName());
                        }
                        break;
                    }
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> queryAllTzPayFeeDetailList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");

            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(resultMap.get("tzPayFeeDetail")), TzPayFeeDetail.class);
            String deptId = (String) resultMap.get("deptId");

            String startPayMonth = (String) resultMap.get("startPayMonth");
            String endPayMonth = (String) resultMap.get("endPayMonth");
            String startPayTime = (String) resultMap.get("startPayTime");
            String endPayTime = (String) resultMap.get("endPayTime");

            String isBackTax = (String) resultMap.get("isBackTax");

            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(resultMap.get("pageVo")), PageVo.class);

            tzPayFeeDetail.setDelFlag(0);

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            deptIds.add(Long.valueOf(deptId));

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
            tzPayFeeDetail.setDelFlag(0);
            Page<TzPayFeeDetail> pageData = new Page<>(page, limit);
            MPJQueryWrapper<TzPayFeeDetail> queryWrapper = new MPJQueryWrapper<>();
            if (tzPayFeeDetail != null) {
                queryWrapper = LikeAllField(tzPayFeeDetail, searchVo);
            }

            if ("1".equals(isBackTax)) {
                queryWrapper.apply("t.pay_month < date_format(t.pay_time,'%Y-%m')");
            } else if ("0".equals(isBackTax)) {
                queryWrapper.apply("t.pay_month = date_format(t.pay_time,'%Y-%m')");
            }

            if (StringUtils.isNotBlank(startPayMonth)) {
                queryWrapper.ge("t.pay_month", startPayMonth);
            }
            if (StringUtils.isNotBlank(endPayMonth)) {
                queryWrapper.le("t.pay_month", endPayMonth);
            }
            if (StringUtils.isNotBlank(startPayTime)) {
                queryWrapper.ge("t.pay_time", startPayTime);
            }
            if (StringUtils.isNotBlank(endPayTime)) {
                queryWrapper.le("t.pay_time", endPayTime);
            }


            queryWrapper.in("t.dept_id", deptIds);
            if ("1".equals(tzPayFeeDetail.getStatus())) {
                queryWrapper.orderByDesc("t.pay_time");
            } else {
                queryWrapper.orderByDesc("t.pay_month");
            }

            queryWrapper.selectAll(TzPayFeeDetail.class);

            queryWrapper.select("sum(g.should_pay) as actuallyPay, count(g.id) as payNum");
            queryWrapper.leftJoin("tz_pay_fee_detail_log g on g.order_status = 2 and g.del_flag = 0 and g.pay_fee_detail_id = t.id");
            queryWrapper.groupBy("t.id");

            IPage<TzPayFeeDetail> result = tzPayFeeDetailMapper.selectJoinPage(pageData, TzPayFeeDetail.class, queryWrapper);

            List<Long> partyIds = new ArrayList<>();
            for (TzPayFeeDetail detail : result.getRecords()) {
                partyIds.add(detail.getPartyMemberId());
                if (detail.getInsteadPayPartyMemberId() != null) {
                    partyIds.add(detail.getInsteadPayPartyMemberId());
                }
            }
            QueryWrapper q = new QueryWrapper();
            q.select("id, real_name");
            q.in("id", partyIds);
            List<JcxfPartyMember> memberList = new ArrayList<>();
            if (partyIds.size() > 0) {
                memberList = jcxfPartyMemberMapper.selectList(q);
            }
            for (TzPayFeeDetail detail : result.getRecords()) {
                for (JcxfPartyMember member : memberList) {
                    if (member.getId().equals(detail.getPartyMemberId()) || member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                        if (member.getId().equals(detail.getPartyMemberId())) {
                            detail.setName(member.getRealName());
                        }
                        if (member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                            detail.setInsteadPayPartyMemberName(member.getRealName());
                        }
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> importPayFeeVerify(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            List<Object> list = (List<Object>) map.get("partyMemberList");
            String deptId = (String) map.get("deptId");

            List<Long> ids = new ArrayList<>();
            for (Object object : list) {
                JcxfPartyMember jcxfPartyMember = JSON.parseObject(JSON.toJSONString(object), JcxfPartyMember.class);
                if (ids.indexOf(jcxfPartyMember.getId()) < 0) {
                    ids.add(jcxfPartyMember.getId());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党员id：" + jcxfPartyMember.getId() + "，存在重复数据");
                }
            }

            // 记录未匹配到的党员信息
            List<JcxfPartyMember> deptPartyMemberList = new ArrayList<>();
            List<JcxfPartyMember> importPartyMemberList = new ArrayList<>();

            // 有效数据
            Set<Object> objectList = new HashSet<>();

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("dept_id", deptId);
            List<JcxfPartyMember> jcxfPartyMemberList = jcxfPartyMemberMapper.selectList(queryWrapper);
            for (JcxfPartyMember partyMember : jcxfPartyMemberList) {
                Boolean flag = false;
                for (Object object : list) {
                    JcxfPartyMember jcxfPartyMember = JSON.parseObject(JSON.toJSONString(object), JcxfPartyMember.class);
                    if (jcxfPartyMember.getId().equals(partyMember.getId())) {
                        flag = true;
                        objectList.add(object);
                        break;
                    }
                }

                if (!flag) {
                    deptPartyMemberList.add(partyMember);
                }
            }

            for (Object object : list) {
                JcxfPartyMember jcxfPartyMember = JSON.parseObject(JSON.toJSONString(object), JcxfPartyMember.class);
                Boolean flag = false;
                for (JcxfPartyMember partyMember : jcxfPartyMemberList) {
                    if (jcxfPartyMember.getId().equals(partyMember.getId())) {
                        flag = true;
                        objectList.add(object);
                        break;
                    }
                }

                if (!flag) {
                    importPartyMemberList.add(jcxfPartyMember);
                }
            }

            BigDecimal total = new BigDecimal("0");
            for (Object object : objectList) {
                // 应付金额只入不舍
                JSONObject json = JSON.parseObject(JSON.toJSONString(object));
                BigDecimal value1 = new BigDecimal(json.get("shouldPay").toString());
                BigDecimal value2 = new BigDecimal("1");
                total = total.add(value1.divide(value2, 2, BigDecimal.ROUND_UP));
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalPay", total);
            result.put("lack", deptPartyMemberList); // 缺失数据
            result.put("surplus", importPartyMemberList); // 多余数据
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "校验异常");
        }
    }

    @Override
    @DSTransactional
    public Map<String, Object> importPayFee(Map<String, Object> requestMap) throws Exception {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        List<Object> list = (List<Object>) map.get("payFeeDto");

        if (list.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "导入失败，数据为空或有效数据为空！");
        }

        String deptId = (String) map.get("deptId");
        String payMonth = (String) map.get("payMonth");

        JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);

        // 这一步表示如果之前有数据，就把之前的信息先删除掉，然后再传入新数据，即为覆盖

        // 删除未缴纳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        QueryWrapper<TzPayFeeDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.eq("tz_pay_fee_detail.dept_id", deptId));
        queryWrapper.eq("status", "0"); // 缴纳状态（0未缴纳，1已缴纳）
        queryWrapper.and(i -> i.eq("tz_pay_fee_detail.pay_month", payMonth));
        tzPayFeeDetailMapper.delete(queryWrapper);

            /*List<TzPayFeeDetail> list1 = tzPayFeeDetailMapper.selectList(queryWrapper);
            // 记录需要更新信息的党员id
            List<Long> partyIds = new ArrayList<>();
            for (TzPayFeeDetail detail: list1) {
                partyIds.add(detail.getPartyMemberId());
            }*/

        // 查询已缴纳
        QueryWrapper<TzPayFeeDetail> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.and(i -> i.eq("tz_pay_fee_detail.dept_id", deptId));
        queryWrapper2.eq("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
        queryWrapper2.and(i -> i.eq("tz_pay_fee_detail.pay_month", payMonth));
        List<TzPayFeeDetail> list2 = tzPayFeeDetailMapper.selectList(queryWrapper2);
        // 记录需要保留信息的党员id
        List<Long> partyIds2 = new ArrayList<>();
        for (TzPayFeeDetail detail : list2) {
            partyIds2.add(detail.getPartyMemberId());
        }

        BigDecimal payTotal = new BigDecimal("0.00");
        List<TzPayFeeDetail> payFeeDetailList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            PayFeeDto fee = JSON.parseObject(JSON.toJSONString(list.get(i)), PayFeeDto.class);

            TzPayFeeDetail tzPayFeeDetail = new TzPayFeeDetail();
            tzPayFeeDetail.setDelFlag(0);
            tzPayFeeDetail.setPartyMemberId(Long.valueOf(fee.getPartyMemberId()));
            tzPayFeeDetail.setIdCardLast(fee.getIdcard());
            tzPayFeeDetail.setName(fee.getRealName());
            String payFee = fee.getPayFee();
            if (StringUtils.isBlank(payFee)) {
                payFee = "0.00";
            }
            // 应付金额只入不舍
            BigDecimal value1 = new BigDecimal(payFee);
            BigDecimal value2 = new BigDecimal("1");
            tzPayFeeDetail.setShouldPay(value1.divide(value2, 2, BigDecimal.ROUND_UP));


            tzPayFeeDetail.setActuallyPay(new BigDecimal("0.00"));
            tzPayFeeDetail.setPayMonth(payMonth);
            tzPayFeeDetail.setStatus(0);
            tzPayFeeDetail.setIsInsteadPay(0);
            tzPayFeeDetail.setDeptName(dept.getName());
            tzPayFeeDetail.setDeptId(Long.valueOf(deptId));
            tzPayFeeDetail.setCreateTime(new Date());

            payFeeDetailList.add(tzPayFeeDetail);

            payTotal = payTotal.add(tzPayFeeDetail.getShouldPay());
        }

        // 查询当前部门该月份是否已经存在缴费数据
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("dept_id", deptId);
        queryWrapper1.eq("pay_month", payMonth);
        queryWrapper1.eq("del_flag", "0");
        TzPayFee tzPayFee = tzPayFeeMapper.selectOne(queryWrapper1);
        if (tzPayFee == null) {
            tzPayFee = new TzPayFee();
            tzPayFee.setPayMonth(payMonth);
            tzPayFee.setDeptId(Long.valueOf(deptId));
            //tzPayFee.setShouldPay(payTotal);
            tzPayFee.setActuallyPay(new BigDecimal("0.00"));
            tzPayFee.setDeptName(dept.getName());
            tzPayFee.setDelFlag(0);
            tzPayFee.setStatus(0);
            tzPayFee.setCreateTime(new Date());
            tzPayFeeMapper.insert(tzPayFee);
        } else {
            tzPayFee.setPayMonth(payMonth);
            tzPayFee.setDeptId(Long.valueOf(deptId));
            //tzPayFee.setShouldPay(payTotal);
            tzPayFee.setActuallyPay(new BigDecimal("0.00"));
            tzPayFee.setDeptName(dept.getName());
            tzPayFee.setDelFlag(0);
            tzPayFee.setStatus(0);
            tzPayFee.setCreateTime(new Date());
            tzPayFeeMapper.updateById(tzPayFee);
        }

        boolean flag = true;
        for (TzPayFeeDetail payFeeDetail : payFeeDetailList) {
            if (partyIds2 != null && partyIds2.size() > 0) {
                // 不变更或重新写入已缴费信息
                if (partyIds2.indexOf(payFeeDetail.getPartyMemberId()) < 0) {
                    payFeeDetail.setPayFeeId(tzPayFee.getId());
                    int res = tzPayFeeDetailMapper.insert(payFeeDetail);
                    if (res < 1) {
                        flag = false;
                        break;
                    }
                }
            } else {
                payFeeDetail.setPayFeeId(tzPayFee.getId());
                int res = tzPayFeeDetailMapper.insert(payFeeDetail);
                if (res < 1) {
                    flag = false;
                    break;
                }
            }
        }

        if (flag) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new Exception("导入发生错误");
        }
    }

    @Override
    public Map<String, Object> getPayFeeDetailPageByPartyMember(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String partyMemberId = (String) map.get("partyMemberId");
            String status = (String) map.get("status");
            String isInsteadPay = (String) map.get("isInsteadPay");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            String orderBy = (String) map.get("orderBy");

            QueryWrapper queryWrapper = new QueryWrapper();
            if (StringUtils.isBlank(isInsteadPay) || "0".equals(isInsteadPay)) {
                queryWrapper.eq("party_member_id", partyMemberId);
            } else if ("1".equals(isInsteadPay)) {
                queryWrapper.eq("instead_pay_party_member_id", partyMemberId);
            }

            if (StringUtils.isNotBlank(isInsteadPay)) {
                queryWrapper.eq("is_instead_pay", isInsteadPay); // 是否代缴 0否 1是
            }
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            String currentYearMonth = formatter.format(currentDate);

            queryWrapper.le("pay_month", currentYearMonth);
            queryWrapper.gt("should_pay", 0);

            queryWrapper.eq("status", status);// 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper.eq("del_flag", 0);
            if (StringUtils.isNotBlank(orderBy)) {
                queryWrapper.orderByDesc(orderBy);
            }

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
            Page<TzPayFeeDetail> pageData = new Page<>(page, limit);
            IPage<TzPayFeeDetail> result = tzPayFeeDetailMapper.selectPage(pageData, queryWrapper);
            for (TzPayFeeDetail detail : result.getRecords()) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.select("sum(should_pay) as should_pay");
                queryWrapper1.eq("pay_fee_detail_id", detail.getId());
                queryWrapper1.eq("order_status", 2);
                queryWrapper1.eq("del_flag", 0);
                TzPayFeeDetailLog log = tzPayFeeDetailLogMapper.selectOne(queryWrapper1);
                if (log != null) {
                    BigDecimal actuallyPay = new BigDecimal(String.valueOf(log.getShouldPay()));
                    detail.setActuallyPay(actuallyPay);
                }

                if (detail.getInsteadPayPartyMemberId() != null) {
                    JcxfPartyMember member = jcxfPartyMemberMapper.selectById(detail.getInsteadPayPartyMemberId());
                    if (member != null) {
                        detail.setInsteadPayPartyMemberName(member.getRealName());
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPayFeeDetailListByPartyMember(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String partyMemberId = (String) map.get("partyMemberId");
            String status = (String) map.get("status");
            QueryWrapper<TzPayFeeDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member_id", partyMemberId);
            queryWrapper.eq("status", status);// 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper.eq("del_flag", 0);
            queryWrapper.orderByDesc("pay_month");

            List<TzPayFeeDetail> result = tzPayFeeDetailMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPayFeeDetailListByDept(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String partyMemberId = (String) requestMap.get("partyMemberId");

            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
            String currentYearMonth = formatter.format(currentDate);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("status", 0);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.gt("should_pay", 0);
            queryWrapper.le("pay_month", currentYearMonth);
            queryWrapper.ne("party_member_id", partyMemberId);
            queryWrapper.orderByDesc("pay_month");

            List<TzPayFeeDetail> tzPayFeeDetailList = tzPayFeeDetailMapper.selectList(queryWrapper);
            if (tzPayFeeDetailList.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (TzPayFeeDetail detail : tzPayFeeDetailList) {
                    ids.add(detail.getPartyMemberId());
                }
                QueryWrapper q = new QueryWrapper();
                q.select("id, real_name");
                q.in("id", ids);
                q.eq("del_flag", 0);
                List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.selectList(q);
                for (TzPayFeeDetail detail : tzPayFeeDetailList) {
                    for (JcxfPartyMember member : memberList) {
                        if (detail.getPartyMemberId().equals(member.getId())) {
                            detail.setName(member.getRealName());
                            break;
                        }
                    }
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzPayFeeDetailList));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }


    public IPage<TzPayFeeDetail> queryTzPayFeeDetailListByPage(TzPayFeeDetail tzPayFeeDetail, String isBackTax, SearchVo searchVo, PageVo pageVo) {
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
        tzPayFeeDetail.setDelFlag(0);
        Page<TzPayFeeDetail> pageData = new Page<>(page, limit);
        MPJQueryWrapper<TzPayFeeDetail> queryWrapper = new MPJQueryWrapper<>();
        if (tzPayFeeDetail != null) {
            queryWrapper = LikeAllField(tzPayFeeDetail, searchVo);
        }

        queryWrapper.selectAll(TzPayFeeDetail.class);

        queryWrapper.select("sum(g.should_pay) as actuallyPay, count(g.id) as payNum");
        queryWrapper.leftJoin("tz_pay_fee_detail_log g on g.order_status = 2 and g.del_flag = 0 and g.pay_fee_detail_id = t.id");
        queryWrapper.groupBy("t.id");

        if ("1".equals(isBackTax)) {
            queryWrapper.apply("t.pay_month < date_format(t.pay_time,'%Y-%m')");
        } else if ("0".equals(isBackTax)) {
            queryWrapper.apply("t.pay_month = date_format(t.pay_time,'%Y-%m')");
        }

        IPage<TzPayFeeDetail> result;
        if (StringUtils.isNotBlank(tzPayFeeDetail.getPersonType())) {
            result = tzPayFeeDetailMapper.selectByType(pageData, queryWrapper, tzPayFeeDetail.getPersonType());
        } else {
            result = tzPayFeeDetailMapper.selectJoinPage(pageData, TzPayFeeDetail.class, queryWrapper);
        }
        return result;
    }

    public List<TzPayFeeDetail> getTzPayFeeDetailList(TzPayFeeDetail tzPayFeeDetail, String isBackTax, SearchVo searchVo) {
        tzPayFeeDetail.setDelFlag(0);
        MPJQueryWrapper<TzPayFeeDetail> queryWrapper = new MPJQueryWrapper<>();
        if (tzPayFeeDetail != null) {
            queryWrapper = LikeAllField(tzPayFeeDetail, searchVo);
        }

        queryWrapper.selectAll(TzPayFeeDetail.class);

        queryWrapper.select("sum(g.should_pay) as actuallyPay, count(g.id) as payNum");
        queryWrapper.leftJoin("tz_pay_fee_detail_log g on g.order_status = 2 and g.del_flag = 0 and g.pay_fee_detail_id = t.id");
        queryWrapper.groupBy("t.id");

        if ("1".equals(isBackTax)) {
            queryWrapper.apply("t.pay_month < date_format(t.pay_time,'%Y-%m')");
        } else if ("0".equals(isBackTax)) {
            queryWrapper.apply("t.pay_month = date_format(t.pay_time,'%Y-%m')");
        }

        List<TzPayFeeDetail> result;
        if (StringUtils.isNotBlank(tzPayFeeDetail.getPersonType())) {
            result = tzPayFeeDetailMapper.selectByType(queryWrapper, tzPayFeeDetail.getPersonType());
        } else {
            result = tzPayFeeDetailMapper.selectJoinList(TzPayFeeDetail.class, queryWrapper);
        }
        return result;
    }

    @Override
    public Map<String, Object> payByOneself(Map<String, Object> requestMap) throws Exception {
        Map<Object, Object> objectMap = (Map<Object, Object>) requestMap.get("data");
        String partyMemberId = (String) objectMap.get("partyMemberId");
        String payType = (String) objectMap.get("payType");

        QueryWrapper<JcxfPartyMember> dyQueryWrapper = new QueryWrapper<>();
        dyQueryWrapper.eq("party_member.id", partyMemberId).eq("party_member.del_flag", 0);
        JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectOne(dyQueryWrapper);
        BsinSysTenant sysTenant = null;
        if (jcxfPartyMember != null) {
            if (StringUtils.isNotBlank(jcxfPartyMember.getTenantId())) {
                sysTenant = bsinSysTenantMapper.getSysTenantInfo(jcxfPartyMember.getTenantId());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "您暂无组织信息无法支付");
            }
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "暂无查询到您的信息");
        }
        List<Object> list = (List<Object>) objectMap.get("payFeeDetails");
        if (list == null || list.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "数据为空，支付失败");
        }

        List<Long> ids = new ArrayList<>();
        Boolean isPayInfo = false;

        for (Object o : list) {
            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(o), TzPayFeeDetail.class);
            // 判断数据的准确性
            if (tzPayFeeDetail.getId() != null) {
                TzPayFeeDetail nowFeeDetail = tzPayFeeDetailMapper.selectById(tzPayFeeDetail.getId());
                if (nowFeeDetail == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未获取到缴费信息，请刷新重试");
                }

                // 订单状态 0订单生成 1支付中 2支付成功 3支付失败 4已撤销 5已退款 6订单关闭
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("pay_fee_detail_id", tzPayFeeDetail.getId());
                List<TzPayFeeDetailLog> logList = tzPayFeeDetailLogMapper.selectList(queryWrapper);

                if (logList != null && logList.size() > 0) {
                    isPayInfo = getPayResult(logList, isPayInfo);
                }

                // 数据存在、未删除状态、党员id、月份、金额要能对得上
                if (nowFeeDetail != null && nowFeeDetail.getDelFlag().intValue() == 0 && nowFeeDetail.getPartyMemberId().intValue() == tzPayFeeDetail.getPartyMemberId().intValue()
                        && nowFeeDetail.getPayMonth().equals(tzPayFeeDetail.getPayMonth()) && nowFeeDetail.getShouldPay().compareTo(tzPayFeeDetail.getShouldPay()) == 0) {
                    ids.add(tzPayFeeDetail.getId());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党费数据变更，请刷新页面");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党费数据变更，请刷新页面");
            }
        }

        // 如果订单已支付，并驳回支付请求
        if (isPayInfo) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在已缴纳或正在缴纳费用，请等待2分钟后刷新重试");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber = random.nextInt(1000000); // 生成一个0到999999之间的随机数

        String orderNumber = timestamp + String.format("%06d", randomNumber);

        if (ids != null && ids.size() > 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("order_no", orderNumber);
            updateWrapper.in("id", ids);
            tzPayFeeDetailMapper.update(null, updateWrapper);
        }

        if (!lockPay(ids, partyMemberId)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在正在支付的缴费");
        }

        BigDecimal amount = new BigDecimal("0");
        for (Long id : ids) {
            TzPayFeeDetail detail = tzPayFeeDetailMapper.selectById(id);

            TzPayFeeDetailLog log = new TzPayFeeDetailLog();
            log.setPayFeeDetailId(id);
            log.setOrderNo(orderNumber);
            log.setCreateTime(new Date());
            log.setDelFlag(0);
            log.setPartyMemberId(Long.valueOf(partyMemberId));
            log.setDeptId(detail.getDeptId());
            log.setPayMonth(detail.getPayMonth());
            log.setShouldPay(detail.getShouldPay());
            amount = amount.add(detail.getShouldPay());
            tzPayFeeDetailLogMapper.insert(log);
        }
        if (!Objects.equals("wx_jsapi", payType)) {
            String jeepayUrl = JeepayHelper.getJeepayUrl(orderNumber, jcxfPartyMember.getRealName() + "党费缴纳：" + amount, amount.toString(), "", "", "", sysTenant);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jeepayUrl));
        } else {
            if(StringUtils.isEmpty(jcxfPartyMember.getOpenid())) {
                //如果openid是空的。还是走托管小程序
                String jeepayUrl = JeepayHelper.getJeepayUrl(orderNumber, jcxfPartyMember.getRealName() + "党费缴纳：" + amount, amount.toString(), "", "", null, sysTenant);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jeepayUrl));
            }
            JSONObject jsonObject = JeepayHelper.getJeepayUrlWeiXin(jcxfPartyMember.getOpenid(), orderNumber, jcxfPartyMember.getRealName() + "党费缴纳：" + amount, amount.toString(), "", sysTenant);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(666666,jsonObject));
        }
    }

    @Override
    public Map<String, Object> payForOther(Map<String, Object> requestMap) throws Exception {
        Map<Object, Object> objectMap = (Map<Object, Object>) requestMap.get("data");
        // 代缴信息，支付的时候待过去，待支付成功后，记录代缴
        String insteadPayPartyMemberId = (String) objectMap.get("insteadPayPartyMemberId");
        List<Object> list = (List<Object>) objectMap.get("payFeeDetails");
        String payType = (String) objectMap.get("payType");

        if (list == null || list.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "数据为空，支付失败");
        }
        QueryWrapper<JcxfPartyMember> dyQueryWrapper = new QueryWrapper<>();
        dyQueryWrapper.eq("party_member.id", insteadPayPartyMemberId).eq("party_member.del_flag", 0);
        JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectOne(dyQueryWrapper);
        BsinSysTenant sysTenant = null;
        if (jcxfPartyMember != null) {
            if (StringUtils.isNotBlank(jcxfPartyMember.getTenantId())) {
                sysTenant = bsinSysTenantMapper.getSysTenantInfo(jcxfPartyMember.getTenantId());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "您暂无组织信息无法支付");
            }
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "暂无查询到您的信息");
        }

        List<Long> ids = new ArrayList<>();
        Boolean isPayInfo = false;

        for (Object o : list) {
            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(o), TzPayFeeDetail.class);
            // 判断数据的准确性
            if (tzPayFeeDetail.getId() != null) {
                TzPayFeeDetail nowFeeDetail = tzPayFeeDetailMapper.selectById(tzPayFeeDetail.getId());
                if (nowFeeDetail == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未获取到缴费信息，请刷新重试");
                }

                // 订单状态 0订单生成 1支付中 2支付成功 3支付失败 4已撤销 5已退款 6订单关闭
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("pay_fee_detail_id", tzPayFeeDetail.getId());
                List<TzPayFeeDetailLog> logList = tzPayFeeDetailLogMapper.selectList(queryWrapper);

                if (logList != null && logList.size() > 0) {
                    isPayInfo = getPayResult(logList, isPayInfo);
                }

                // 数据存在、未删除状态、党员id、月份、金额要能对得上
                if (nowFeeDetail != null && nowFeeDetail.getDelFlag().intValue() == 0 && nowFeeDetail.getPartyMemberId().intValue() == tzPayFeeDetail.getPartyMemberId().intValue()
                        && nowFeeDetail.getPayMonth().equals(tzPayFeeDetail.getPayMonth()) && nowFeeDetail.getShouldPay().compareTo(tzPayFeeDetail.getShouldPay()) == 0) {
                    ids.add(tzPayFeeDetail.getId());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党费数据变更，请刷新页面");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党费数据变更，请刷新页面");
            }
        }

        // 如果订单已支付，并驳回支付请求
        if (isPayInfo) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在已缴纳或正在缴纳费用，请等待2分钟后刷新重试");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber = random.nextInt(1000000); // 生成一个0到999999之间的随机数
        String orderNumber = timestamp + String.format("%06d", randomNumber);

        if (ids != null && ids.size() > 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.set("order_no", orderNumber);
            updateWrapper.in("id", ids);
            tzPayFeeDetailMapper.update(null, updateWrapper);
        }

        if (!lockPay(ids, insteadPayPartyMemberId)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在正在支付的缴费");
        }

        BigDecimal amount = new BigDecimal("0");

        for (Long id : ids) {
            TzPayFeeDetail detail = tzPayFeeDetailMapper.selectById(id);

            TzPayFeeDetailLog log = new TzPayFeeDetailLog();
            log.setPayFeeDetailId(id);
            log.setOrderNo(orderNumber);
            log.setCreateTime(new Date());
            log.setDelFlag(0);
            log.setPartyMemberId(Long.parseLong(insteadPayPartyMemberId));
            log.setDeptId(detail.getDeptId());
            log.setPayMonth(detail.getPayMonth());
            log.setShouldPay(detail.getShouldPay());
            amount = amount.add(detail.getShouldPay());
            tzPayFeeDetailLogMapper.insert(log);
        }

        if (!Objects.equals("wx_jsapi", payType)) {
            String jeepayUrl = JeepayHelper.getJeepayUrl(orderNumber, jcxfPartyMember.getRealName() + "党费代缴：" + amount + "," + insteadPayPartyMemberId, amount.toString(), "", "", null, sysTenant);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jeepayUrl));
        } else {
            if(StringUtils.isEmpty(jcxfPartyMember.getOpenid())) {
                //如果openid是空的。还是走托管小程序
                String jeepayUrl = JeepayHelper.getJeepayUrl(orderNumber, jcxfPartyMember.getRealName() + "党费代缴：" + amount + "," + insteadPayPartyMemberId, amount.toString(), "", "", null, sysTenant);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jeepayUrl));
            }
            JSONObject jsonObject = JeepayHelper.getJeepayUrlWeiXin(jcxfPartyMember.getOpenid(), orderNumber, jcxfPartyMember.getRealName() + "党费代缴：" + amount + "," + insteadPayPartyMemberId, amount.toString(), "", sysTenant);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(666666,jsonObject));
        }

    }

    private synchronized Boolean lockPay(List<Long> ids, String memberId) {
        Boolean flag = true;
        List<Long> delIds = new ArrayList<>();
        for (Long id : ids) {
            if (!redisUtils.setIfAbsent("payFee_" + id, memberId, 120, TimeUnit.SECONDS)) {
                if (memberId.equals(redisUtils.get("payFee_" + id))) {
                    redisUtils.set("payFee_" + id, memberId, 120, TimeUnit.SECONDS);
                } else {
                    flag = false;
                    break;
                }
            } else {
                delIds.add(id);
            }
        }
        if (!flag && delIds.size() > 0) {
            for (Long id : delIds) {
                redisUtils.del("payFee_" + id);
            }
        }
        return flag;
    }

    private Boolean getPayResult(List<TzPayFeeDetailLog> logList, Boolean isPayInfo) throws Exception {
        for (TzPayFeeDetailLog log : logList) {
            // 如果没有获取到支付结果，则主动查询一次
            if (log.getOrderStatus() == null || log.getOrderStatus().intValue() == 0 || log.getOrderStatus().intValue() == 1) {
                QueryWrapper<JcxfPartyMember> dyQueryWrapper = new QueryWrapper<>();
                dyQueryWrapper.eq("party_member.id", log.getPartyMemberId()).eq("party_member.del_flag", 0);
                JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectOne(dyQueryWrapper);
                BsinSysTenant sysTenant = null;
                if (jcxfPartyMember != null) {
                    if (StringUtils.isNotBlank(jcxfPartyMember.getTenantId())) {
                        sysTenant = bsinSysTenantMapper.getSysTenantInfo(jcxfPartyMember.getTenantId());
                    } else {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL), "您暂无组织信息无法支付");
                    }
                }
                JeepayResult jeepayResult = JeepayHelper.payQuery(log.getOrderNo(), sysTenant);
                // 如果查询到订单信息
                if (jeepayResult != null && jeepayResult.getCode().intValue() == 0) {
                    // 更新订单信息
                    UpdateWrapper<TzPayFeeDetailLog> updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("order_no", log.getOrderNo());
                    updateWrapper.set("order_status", jeepayResult.getData().getState());
                    updateWrapper.set("amount", jeepayResult.getData().getAmount());
                    updateWrapper.set("pay_result", jeepayResult.toString());
                    updateWrapper.set("pay_result_time", new Date());

                    // 订单状态 0订单生成 1支付中 2支付成功 3支付失败 4已撤销 5已退款 6订单关闭
                    int state = jeepayResult.getData().getState();
                    if (state == 2) {
                        Date successTime = new Date(jeepayResult.getData().getSuccessTime());
                        // 修改党费信息状态
                        UpdateWrapper updateWrapper1 = new UpdateWrapper();
                        updateWrapper1.eq("id", log.getPayFeeDetailId());
                        updateWrapper1.set("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
                        updateWrapper1.set("pay_time", successTime);
                        tzPayFeeDetailMapper.update(null, updateWrapper1);

                        isPayInfo = true;
                        updateWrapper.set("success_time", successTime);
                    } else {
                        updateWrapper.set("success_time", null);
                    }

                    // 存在有效订单，则不继续支付
                    if (state == 0 || state == 1) {
                        isPayInfo = true;
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
            } else if (log.getOrderStatus().intValue() == 2) {
                isPayInfo = true;
                // 修改党费信息状态
                UpdateWrapper<TzPayFeeDetail> updateWrapper1 = new UpdateWrapper();
                updateWrapper1.eq("id", log.getPayFeeDetailId());
                updateWrapper1.set("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
                updateWrapper1.set("pay_time", log.getSuccessTime());
                tzPayFeeDetailMapper.update(null, updateWrapper1);
            }
        }
        return isPayInfo;
    }

    /*@Override
    public Map<String, Object> updatePayStatus(Map<String, Object> requestMap) {
        try {
            Map<Object, Object> objectMap = (Map<Object, Object>) requestMap.get("data");
            List<Object> list = (List<Object>) objectMap.get("payFeeDetails");
            Long payFeeId = null;
            for (Object o : list) {
                TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(o), TzPayFeeDetail.class);
                if (payFeeId == null) {
                    payFeeId = tzPayFeeDetail.getPayFeeId();
                }
                tzPayFeeDetail.setStatus("1");
                tzPayFeeDetail.setPayTime(new Date());
                tzPayFeeDetail.setActuallyPay(tzPayFeeDetail.getShouldPay());
                tzPayFeeDetailMapper.updateById(tzPayFeeDetail);
            }
            QueryWrapper<TzPayFeeDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("tz_pay_fee_detail.pay_fee_id", payFeeId);
            List<TzPayFeeDetail> payFeeDetails = tzPayFeeDetailMapper.selectList(detailQueryWrapper);
            BigDecimal count = BigDecimal.valueOf(0);
            if (payFeeDetails != null && payFeeDetails.size() > 0) {
                for (TzPayFeeDetail payFeeDetail : payFeeDetails) {
                    if (payFeeDetail.getStatus() != null && payFeeDetail.getStatus().equals("1")) {
                        BigDecimal actuallyPay = payFeeDetail.getActuallyPay();
                        count = count.add(actuallyPay);
                    }
                }
            }
            QueryWrapper<TzPayFee> feeQueryWrapper = new QueryWrapper<>();
            feeQueryWrapper.eq("tz_pay_fee.id", payFeeId);
            TzPayFee tzPayFee = tzPayFeeMapper.selectOne(feeQueryWrapper);
            if (tzPayFee != null) {
                tzPayFee.setActuallyPay(count);
            }
            int i = tzPayFeeMapper.updateById(tzPayFee);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.showFailMsg("异常信息：" + e.getMessage()));
        }
    }*/

    @Override
    public Map<String, Object> payCallBack(Map<String, Object> requestMap) {
        try {
            PayOrderQueryResModel model = JSON.parseObject(JSON.toJSONString(requestMap), PayOrderQueryResModel.class);
            if (StringUtils.isEmpty(model.getMchOrderNo())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "订单号为空");
            }

            QueryWrapper<TzPayFeeDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_pay_fee_detail.order_no", model.getMchOrderNo());
            List<TzPayFeeDetail> payFeeDetails = tzPayFeeDetailMapper.selectList(queryWrapper);
            for (TzPayFeeDetail payFeeDetail : payFeeDetails) {
                payFeeDetail.setStatus(1);
                payFeeDetail.setPayTime(new Date(model.getSuccessTime()));

//                BigDecimal actuallyPay = new BigDecimal(String.valueOf(model.getAmount())).divide(new BigDecimal("100"));
//                payFeeDetail.setActuallyPay(actuallyPay);

                // 记录代缴人信息
                if (model.getBody().split(",").length > 1) {
                    String partyId = model.getBody().split(",")[1];
                    JcxfPartyMember member = jcxfPartyMemberMapper.selectById(partyId);
                    if (member != null) {
                        payFeeDetail.setIsInsteadPay(1); //是否代缴
                        payFeeDetail.setInsteadPayPartyMemberName(member.getRealName());
                        payFeeDetail.setInsteadPayPartyMemberId(Long.valueOf(partyId));
                    }
                }
                tzPayFeeDetailMapper.updateById(payFeeDetail);
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("order_no", model.getMchOrderNo());
            updateWrapper.set("pay_result_time", new Date());
            updateWrapper.set("pay_result", model.toString());
            updateWrapper.set("order_status", model.getState());
            updateWrapper.set("success_time", new Date(model.getSuccessTime()));
            updateWrapper.set("amount", model.getAmount());
            tzPayFeeDetailLogMapper.update(null, updateWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Map<String, Object> getPayFeeDetailById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
            }
            TzPayFeeDetail detail = tzPayFeeDetailMapper.selectById(id);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(detail));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取信息异常");
        }
    }

    @Override
    public Map<String, Object> updatePayFeeDetail(Map<String, Object> requestMap) {
        try {
//            String id = (String) requestMap.get("id");
//            String shouldPay = (String) requestMap.get("shouldPay");
//            String remark = (String) requestMap.get("remark");
//            if (StringUtils.isBlank(id) || StringUtils.isBlank(shouldPay)) {
//                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
//            }
//
//            TzPayFeeDetail detail = tzPayFeeDetailMapper.selectById(id);
//            if ("1".equals(detail.getStatus())) { // 缴纳状态（0未缴纳，1已缴纳）
//                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该缴费已支付");
//            }
//
//            UpdateWrapper up = new UpdateWrapper();
//            up.eq("id", id);
//            up.set("should_pay", new BigDecimal(shouldPay));
//            up.set("remark", remark);
//            int count = tzPayFeeDetailMapper.update(null, up);
//            if (count > 0) {
//                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
//            } else {
//                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存出错");
//            }
            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(requestMap.get("tzPayFeeDetail")), TzPayFeeDetail.class);
            int count = tzPayFeeDetailMapper.updateById(tzPayFeeDetail);
            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存异常");
        }
    }

    @Override
    public Map<String, Object> downloadTzPayFeeDetailList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> resultMap = (Map<String, Object>) requestMap.get("data");

            TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(resultMap.get("tzPayFeeDetail")), TzPayFeeDetail.class);
            String isBackTax = (String) resultMap.get("isBackTax");
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(resultMap.get("searchVo")), SearchVo.class);

            tzPayFeeDetail.setDelFlag(0);

            List<TzPayFeeDetail> result = getTzPayFeeDetailList(tzPayFeeDetail, isBackTax, searchVo);

            Set<Long> partyIds = new HashSet<>();
            for (TzPayFeeDetail detail : result) {
                partyIds.add(detail.getPartyMemberId());
                if (detail.getInsteadPayPartyMemberId() != null) {
                    partyIds.add(detail.getInsteadPayPartyMemberId());
                }
            }
            QueryWrapper q = new QueryWrapper();
            q.in("id", partyIds);
            List<JcxfPartyMember> memberList = new ArrayList<>();
            if (partyIds.size() > 0) {
                memberList = jcxfPartyMemberMapper.selectList(q);
            }
            for (TzPayFeeDetail detail : result) {
                for (JcxfPartyMember member : memberList) {
                    if (member.getId().equals(detail.getPartyMemberId()) || member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                        if (member.getId().equals(detail.getPartyMemberId())) {
                            detail.setName(member.getRealName());
                        }
                        if (member.getId().equals(detail.getInsteadPayPartyMemberId())) {
                            detail.setInsteadPayPartyMemberName(member.getRealName());
                        }
                        break;
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPayFeeDetailPersonList(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String payYear = (String) requestMap.get("payYear");
        String name = (String) requestMap.get("name");

        PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("pageVo")), PageVo.class);
        List<Long> deptList = jcxfSysDeptMapper.selectChildrenIdsById(Long.parseLong(deptId));
        deptList.add(Long.parseLong(deptId));
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
        Page pageData = new Page<>(page, limit);

        IPage<Map<String, Object>> list = tzPayFeeDetailMapper.getPayFeeDetailPersonList(deptList, payYear, name, pageData);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
    }

    @Override
    public Map<String, Object> getShouldPayData(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        String paymentBase = (String) requestMap.get("paymentBase");
        JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectById(partyMemberId);
        Map<String, Object> hashMap = summary(paymentBase, jcxfPartyMember.getWorkPosition());
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(hashMap));
    }

    private Map<String, Object> summary(String paymentBase, String workPosition) {
        if (StringUtils.isEmpty(paymentBase)) {
            paymentBase = "0.0";
        }
        BigDecimal num1 = new BigDecimal(paymentBase);
        Double shouldPay = 0.00;
        String radio = "";
        String proportion = "";
        if (Objects.equals("502", workPosition) || Objects.equals("501", workPosition)
                || Objects.equals("503", workPosition) || Objects.equals("504", workPosition)) {
            //退休人员 5000及5000以下 0.5%  5000以上1%
            if (num1.doubleValue() > 5000) {
                radio = "0.01";
                proportion = "1%";
            } else {
                radio = "0.005";
                proportion = "0.5%";
            }
        } else {
            //在职人员 3000及3000以下 0.5% 3000以上至5000 1% 5000至1万 1.5%  1万以上2%
            if (num1.doubleValue() <= 3000) {
                radio = "0.005";
                proportion = "0.5%";
            } else if (num1.doubleValue() > 3000 && num1.doubleValue() <= 5000) {
                radio = "0.01";
                proportion = "1%";
            } else if (num1.doubleValue() > 5000 && num1.doubleValue() <= 10000) {
                radio = "0.015";
                proportion = "1.5%";
            } else {
                radio = "0.02";
                proportion = "2%";
            }
        }
        BigDecimal num2 = new BigDecimal(radio);
        BigDecimal multiply = num1.multiply(num2);
        BigDecimal bigDecimal = multiply.setScale(1, BigDecimal.ROUND_UP);
        shouldPay = bigDecimal.doubleValue();
        if (shouldPay < 0.1) {
            shouldPay = 0.1;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("shouldPay", shouldPay);
        map.put("proportion", proportion);
        return map;
    }


    @Override
    public Map<String, Object> updatePayFeeDetailByBatchMonth(Map<String, Object> requestMap) {
        TzPayFeeDetail tzPayFeeDetail = JSON.parseObject(JSON.toJSONString(requestMap.get("tzPayFeeDetail")), TzPayFeeDetail.class);

        LambdaUpdateWrapper<TzPayFeeDetail> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(TzPayFeeDetail::getPaymentType, tzPayFeeDetail.getPaymentType());
        updateWrapper.set(TzPayFeeDetail::getPaymentBase, tzPayFeeDetail.getPaymentBase());
        updateWrapper.set(TzPayFeeDetail::getPaymentRatio, tzPayFeeDetail.getPaymentRatio());
        updateWrapper.set(TzPayFeeDetail::getPaymentReason, tzPayFeeDetail.getPaymentReason());
        updateWrapper.set(TzPayFeeDetail::getReasonOther, tzPayFeeDetail.getReasonOther());
        updateWrapper.set(TzPayFeeDetail::getConfirmMaterial, tzPayFeeDetail.getConfirmMaterial());
        updateWrapper.set(TzPayFeeDetail::getShouldPay, tzPayFeeDetail.getShouldPay());

        updateWrapper.eq(TzPayFeeDetail::getPartyMemberId, tzPayFeeDetail.getPartyMemberId());
        updateWrapper.ge(TzPayFeeDetail::getPayMonth, tzPayFeeDetail.getPayMonth());
        updateWrapper.eq(TzPayFeeDetail::getStatus, 0);

        tzPayFeeDetailMapper.update(null, updateWrapper);

        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
    }

    @Override
    public Map<String, Object> queryPayFeeDetailByPerson(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        String payMonth = (String) requestMap.get("payMonth");

        LambdaQueryWrapper<TzPayFeeDetail> updateWrapper = new LambdaQueryWrapper<>();
        updateWrapper.eq(TzPayFeeDetail::getPayMonth, payMonth);
        updateWrapper.eq(TzPayFeeDetail::getPartyMemberId, partyMemberId);
        updateWrapper.eq(TzPayFeeDetail::getDelFlag, 0);
        updateWrapper.last("LIMIT 1");
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzPayFeeDetailMapper.selectOne(updateWrapper)));
    }


    @Override
    public Map<String, Object> addPartMemberToPayFee(Map<String, Object> requestMap) {
        String parentDeptId = (String) requestMap.get("deptId");
        List<Map<String, Object>> partyMemberList = jcxfPartyMemberMapper.queryDeptPartyMemberList(parentDeptId, "true");
        Map<String, List<Map<String, Object>>> deptIdListMap = partyMemberList.stream().collect(Collectors.groupingBy(item -> item.get("dept_id").toString()));
        Date currentDate = new Date();
        BigDecimal actuallyPay = new BigDecimal("0.00");
        TzPayFeeDetail tzPayFeeDetail = null;

        for (int i = 1; i <= 12; i++) {
            //获取当前年月
            String payMonth = LocalDate.now().getYear() + "-" + String.format("%02d", i);
            for (String deptId : deptIdListMap.keySet()) {
                LambdaQueryWrapper<TzPayFee> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TzPayFee::getPayMonth, payMonth);
                queryWrapper.eq(TzPayFee::getDeptId, deptId);
                TzPayFee selectOne = tzPayFeeMapper.selectOne(queryWrapper);
                List<Map<String, Object>> memberList = deptIdListMap.get(deptId);
                if (selectOne == null) {
                    selectOne = new TzPayFee();
                    selectOne.setPayMonth(payMonth);
                    selectOne.setDeptId(Long.parseLong(deptId));
                    selectOne.setDeptName(partyMemberList.stream().filter(item -> Objects.equals(item.get("dept_id").toString(), deptId)).findFirst().get().get("dept_name").toString());
                    selectOne.setActuallyPay(actuallyPay);
                    selectOne.setStatus(0);
                    selectOne.setDelFlag(0);
                    selectOne.setCreateTime(currentDate);
                    tzPayFeeMapper.insert(selectOne);
                }
                for (Map<String, Object> map : memberList) {
                    tzPayFeeDetail = new TzPayFeeDetail();
                    tzPayFeeDetail.setName(map.get("real_name").toString());
                    tzPayFeeDetail.setPayFeeId(selectOne.getId());
                    tzPayFeeDetail.setPayMonth(payMonth);
                    tzPayFeeDetail.setShouldPay(actuallyPay);
                    tzPayFeeDetail.setActuallyPay(actuallyPay);
                    tzPayFeeDetail.setDeptId(selectOne.getDeptId());
                    tzPayFeeDetail.setDeptName(selectOne.getDeptName());
                    tzPayFeeDetail.setStatus(0);
                    tzPayFeeDetail.setIsInsteadPay(0);
                    tzPayFeeDetail.setCreateTime(currentDate);
                    tzPayFeeDetail.setDelFlag(0);
                    tzPayFeeDetail.setPartyMemberId(Long.parseLong(map.get("party_member_id").toString()));
                    tzPayFeeDetailMapper.insert(tzPayFeeDetail);
                }
            }
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
    }

    @Override
    public Map<String, Object> queryPartMemberRePayData(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        String startMonth = (String) requestMap.get("startMonth");
        String currentMonth = DateUtil.format(new Date(), "yyyy-MM");

        LambdaQueryWrapper<TzPayFeeDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TzPayFeeDetail::getPartyMemberId, partyMemberId);
        queryWrapper.ge(TzPayFeeDetail::getPayMonth, startMonth);
        queryWrapper.lt(TzPayFeeDetail::getPayMonth, currentMonth);
        queryWrapper.eq(TzPayFeeDetail::getDelFlag, 0);
        List<TzPayFeeDetail> detailList = tzPayFeeDetailMapper.selectList(queryWrapper);

        List<TzPayFeeDetail> collect = detailList.stream().filter(item -> item.getStatus() == 0 && item.getShouldPay().doubleValue() == 0).collect(Collectors.toList());
        if (collect.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党员所有月份的党费标准已存在，无需设置");
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(collect));
    }


    @Override
    public Map<String, Object> updateRePayFeeDetail(Map<String, Object> requestMap) {
        try {
            RepayFeeEntity repayFeeEntity = JSON.parseObject(JSON.toJSONString(requestMap.get("repayObj")), RepayFeeEntity.class);
            for (TzPayFeeDetail tzPayFeeDetail : repayFeeEntity.getRepayList()) {
                tzPayFeeDetailMapper.updateById(tzPayFeeDetail);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存异常");
        }
    }

    @Override
    public Map<String, Object> queryPartMemberDeleteData(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        String deleteMonth = (String) requestMap.get("deleteMonth");

        LambdaQueryWrapper<TzPayFeeDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TzPayFeeDetail::getPartyMemberId, partyMemberId);
        queryWrapper.eq(TzPayFeeDetail::getPayMonth, deleteMonth);
        queryWrapper.eq(TzPayFeeDetail::getStatus, 0);
        queryWrapper.eq(TzPayFeeDetail::getDelFlag, 0);
        queryWrapper.gt(TzPayFeeDetail::getShouldPay, 0);
        queryWrapper.last("LIMIT 1");

        TzPayFeeDetail selectOne = tzPayFeeDetailMapper.selectOne(queryWrapper);
        if (selectOne == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "没有可删除的标准");
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(selectOne));
    }

    @Override
    public Map<String, Object> importPayFeeStandard(Map<String, Object> requestMap) {
        try {
            PayFeeImportEntity payFeeImportEntity = JSON.parseObject(JSON.toJSONString(requestMap.get("reqData")), PayFeeImportEntity.class);
            String deptId = payFeeImportEntity.getDeptId();
            String payMonth = payFeeImportEntity.getPayMonth();
            List<TzPayFeeDetail> payPersonList = payFeeImportEntity.getPayPersonList();


            List<JcxfPartyMemberVo> partyMemberList = jcxfPartyMemberMapper.selectPartyMemberVoByDeptId(deptId);
            Date currentDate = new Date();
            List<Long> deleteIdList = new ArrayList<>();

            //计算选择月份到12月相差多少
            String month = payMonth.substring(5);

            for (TzPayFeeDetail tzPayFeeDetail : payPersonList) {
                JcxfPartyMemberVo memberVo = partyMemberList.stream().filter(item -> Objects.equals(item.getPhone(), AESUtil2.encrypt(tzPayFeeDetail.getPhone()))
                        && Objects.equals(item.getRealName(), tzPayFeeDetail.getName())).findFirst().orElse(null);
                if (memberVo == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "导入的党员：“" + tzPayFeeDetail.getName() + "”信息有误，请检查后重试！");
                }

                deleteIdList.add(memberVo.getId());

                Map<String, Object> hashMap = summary(tzPayFeeDetail.getPaymentBase(), memberVo.getWorkPosition());
                tzPayFeeDetail.setPayMonth(payMonth);
                tzPayFeeDetail.setPartyMemberId(memberVo.getId());
                tzPayFeeDetail.setPaymentType("按工资比例交纳");
                tzPayFeeDetail.setPaymentRatio(hashMap.get("proportion").toString());
                tzPayFeeDetail.setShouldPay(new BigDecimal(hashMap.get("shouldPay").toString()));
                tzPayFeeDetail.setActuallyPay(tzPayFeeDetail.getShouldPay());

            }

            String payYear = payMonth.substring(0, 4);

            ArrayList<TzPayFeeDetail> detailArrayList = new ArrayList<>();
            ArrayList<String> deleteMonthArr = new ArrayList<>();
            for (int i = Integer.parseInt(month); i <= 12; i++) {
                payMonth = payYear + "-" + String.format("%02d", i);
                deleteMonthArr.add(payMonth);

                LambdaQueryWrapper<TzPayFee> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(TzPayFee::getPayMonth, payMonth);
                queryWrapper.eq(TzPayFee::getDeptId, deptId);
                TzPayFee selectOne = tzPayFeeMapper.selectOne(queryWrapper);

                for (TzPayFeeDetail tzPayFeeDetail : payPersonList) {
                    TzPayFeeDetail clone = new TzPayFeeDetail();
                    clone.setPayMonth(payMonth);
                    clone.setName(tzPayFeeDetail.getName());
                    clone.setPaymentBase(tzPayFeeDetail.getPaymentBase());
                    clone.setPartyMemberId(tzPayFeeDetail.getPartyMemberId());
                    clone.setPaymentType("按工资比例交纳");
                    clone.setPaymentRatio(tzPayFeeDetail.getPaymentRatio());
                    clone.setShouldPay(tzPayFeeDetail.getShouldPay());
                    clone.setActuallyPay(tzPayFeeDetail.getShouldPay());
                    clone.setDeptId(selectOne.getDeptId());
                    clone.setDeptName(selectOne.getDeptName());
                    clone.setPayFeeId(selectOne.getId());
                    clone.setStatus(0);
                    clone.setIsInsteadPay(0);
                    clone.setCreateTime(currentDate);
                    clone.setDelFlag(0);
                    detailArrayList.add(clone);
                }
            }

            // 删除之前的党员数据
            if (deleteIdList.size() > 0) {
                LambdaQueryWrapper<TzPayFeeDetail> wrapper = new LambdaQueryWrapper<>();
                wrapper.in(TzPayFeeDetail::getPartyMemberId, deleteIdList);
                wrapper.in(TzPayFeeDetail::getPayMonth, deleteMonthArr);
                wrapper.eq(TzPayFeeDetail::getDeptId, deptId);
                wrapper.eq(TzPayFeeDetail::getStatus, 0); //只删除未缴纳的
                tzPayFeeDetailMapper.delete(wrapper);
            }


            // 查询已缴纳
            LambdaQueryWrapper<TzPayFeeDetail> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(TzPayFeeDetail::getDeptId, deptId);
            queryWrapper2.eq(TzPayFeeDetail::getStatus, 1); // 缴纳状态（0未缴纳，1已缴纳）
            queryWrapper2.in(TzPayFeeDetail::getPayMonth, deleteMonthArr);
            List<TzPayFeeDetail> list2 = tzPayFeeDetailMapper.selectList(queryWrapper2);

//            List<Long> partyMemberIdList = list2.stream().map(item -> item.getPartyMemberId()).collect(Collectors.toList());
//
//            //过滤掉已缴费的党员
//            List<TzPayFeeDetail> collect = detailArrayList.stream().filter(item -> !partyMemberIdList.contains(item.getPartyMemberId())).collect(Collectors.toList());

            //过滤掉已缴费的党员
            for (TzPayFeeDetail tzPayFeeDetail : list2) {
                TzPayFeeDetail find = detailArrayList.stream().filter(item -> Objects.equals(item.getPartyMemberId(), tzPayFeeDetail.getPartyMemberId())
                        && item.getPayMonth().equals(tzPayFeeDetail.getPayMonth())).findFirst().orElse(null);
                if (find != null) {
                    detailArrayList.remove(find);
                }
            }

            List<List<TzPayFeeDetail>> lists = splitList(detailArrayList, 1500);
            for (int i = 0; i < lists.size(); i++) {
                List<TzPayFeeDetail> tzPayFeeDetails = lists.get(i);
                tzPayFeeDetailMapper.saveBatchList(tzPayFeeDetails);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), e.getMessage());
        }
    }

    public List<List<TzPayFeeDetail>> splitList(List<TzPayFeeDetail> list, int chunkSize) {
        return IntStream.range(0, (list.size() + chunkSize - 1) / chunkSize)
                .mapToObj(i -> list.subList(i * chunkSize, Math.min((i + 1) * chunkSize, list.size())))
                .collect(Collectors.toList());
    }


    @Override
    public Map<String, Object> exportPayFeeDetailPersonList(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String payYear = (String) requestMap.get("payYear");
        String name = (String) requestMap.get("name");

        List<Long> deptList = jcxfSysDeptMapper.selectChildrenIdsById(Long.parseLong(deptId));
        deptList.add(Long.parseLong(deptId));
        List<Map<String, Object>> list = tzPayFeeDetailMapper.getPayFeeDetailPersonList(deptList, payYear, name);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
    }


    @Override
    public Map<String, Object> queryPartyMemberListByDeptId(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        List<JcxfPartyMemberVo> partyMemberList = jcxfPartyMemberMapper.selectPartyMemberVoByDeptId(deptId);
        for (JcxfPartyMemberVo jcxfPartyMemberVo : partyMemberList) {
            if (StringUtils.isNotEmpty(jcxfPartyMemberVo.getPhone())) {
                jcxfPartyMemberVo.setPhone(AESUtil2.decrypt(jcxfPartyMemberVo.getPhone()));
            }
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(partyMemberList));
    }

    @Override
    public Map<String, Object> getPayFeeDetailStatistics(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String payMonth = (String) requestMap.get("payMonth");

        List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        ids.add(Long.valueOf(deptId));
        PayFeeDetailsVo payFeeDetailStatistics = tzPayFeeDetailMapper.getPayFeeDetailStatistics(ids, payMonth);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(payFeeDetailStatistics));
    }

    @Override
    public Map<String, Object> getPayFeeStatistics(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String startMonth = (String) requestMap.get("startMonth");
        String endMonth = (String) requestMap.get("endMonth");

        List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        ids.add(Long.valueOf(deptId));
        PayFeeDetailsVo payFeeDetailStatistics = tzPayFeeDetailMapper.getPayFeeStatistics(ids, startMonth, endMonth);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(payFeeDetailStatistics));
    }

    @Override
    public Map<String, Object> getNoDonePartyMember(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String payMonth = (String) requestMap.get("payMonth");
        List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        ids.add(Long.valueOf(deptId));
        List<JcxfPartyMember> noDonePartyMember = tzPayFeeDetailMapper.getNoDonePartyMember(ids, payMonth);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(noDonePartyMember));
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzPayFeeDetail 需要模糊查询的信息
     * @return 返回查询
     */
    public MPJQueryWrapper<TzPayFeeDetail> LikeAllField(TzPayFeeDetail tzPayFeeDetail, SearchVo searchVo) {
        MPJQueryWrapper<TzPayFeeDetail> queryWrapper = new MPJQueryWrapper<>();
        if (tzPayFeeDetail.getId() != null) {
            queryWrapper.and(i -> i.eq("t.id", tzPayFeeDetail.getId()));
        }
        if (StringUtils.isNotBlank(tzPayFeeDetail.getName())) {
            queryWrapper.and(i -> i.like("t.name", tzPayFeeDetail.getName()));
        }
        if (StringUtils.isNotBlank(tzPayFeeDetail.getPayMonth())) {
            queryWrapper.and(i -> i.eq("t.pay_month", tzPayFeeDetail.getPayMonth()));
        }
        if (tzPayFeeDetail.getShouldPay() != null) {
            queryWrapper.and(i -> i.like("t.should_pay", tzPayFeeDetail.getShouldPay()));
        }
        if (tzPayFeeDetail.getActuallyPay() != null) {
            queryWrapper.and(i -> i.like("t.actually_pay", tzPayFeeDetail.getActuallyPay()));
        }
        if (tzPayFeeDetail.getDeptIds() != null && tzPayFeeDetail.getDeptIds().size() > 0) {
            queryWrapper.and(i -> i.in("t.dept_id", tzPayFeeDetail.getDeptIds()));
        } else {
            if (tzPayFeeDetail.getDeptId() != null) {
                queryWrapper.and(i -> i.eq("t.dept_id", tzPayFeeDetail.getDeptId()));
            }
        }
        if (tzPayFeeDetail.getPartyMemberId() != null) {
            queryWrapper.and(i -> i.eq("t.party_member_id", tzPayFeeDetail.getPartyMemberId()));
        }
        if (StringUtils.isNotBlank(tzPayFeeDetail.getDeptName())) {
            queryWrapper.and(i -> i.like("t.dept_name", tzPayFeeDetail.getDeptName()));
        }
        if (tzPayFeeDetail.getStatus() != null) {
            queryWrapper.and(i -> i.eq("t.status", tzPayFeeDetail.getStatus()));
        }
        if (tzPayFeeDetail.getIsInsteadPay() != null) {
            queryWrapper.and(i -> i.eq("t.is_instead_pay", tzPayFeeDetail.getIsInsteadPay()));
        }
        if (tzPayFeeDetail.getPayTime() != null) {
            queryWrapper.and(i -> i.like("t.pay_time", tzPayFeeDetail.getPayTime()));
        }
        if (tzPayFeeDetail.getCreateTime() != null) {
            queryWrapper.and(i -> i.like("t.create_time", tzPayFeeDetail.getCreateTime()));
        }
        if (tzPayFeeDetail.getPayFeeId() != null) {
            queryWrapper.and(i -> i.eq("t.pay_fee_id", tzPayFeeDetail.getPayFeeId()));
        }
        if (tzPayFeeDetail.getDelFlag() != null) {
            queryWrapper.and(i -> i.eq("t.del_flag", tzPayFeeDetail.getDelFlag()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(TzPayFeeDetail::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        return queryWrapper;
    }

}

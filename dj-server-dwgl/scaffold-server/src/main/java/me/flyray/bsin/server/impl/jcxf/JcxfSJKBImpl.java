package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfSJKBService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.jcxf.JcxfOrganizationLifeMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSJKBMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.jcxf.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;

public class JcxfSJKBImpl implements JcxfSJKBService {


    @Autowired
    private JcxfSJKBMapper jcxfSJKBMapper;

    @Autowired
    private JcxfOrganizationLifeMapper jcxfOrganizationLifeMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Override
    public Map<String, Object> partMeeting(Map<String, Object> requestMap) {
        try {
            Long deptId = 1L;
            String pay_year = DateUtils.getYear();
            String pay_month = DateUtils.getMonth();
            String payTimeStart;
            String payTimeEnd;
            int payMonthOne = Integer.parseInt(pay_month);
            String status = requestMap.get("status").toString();
            if ("1".equals(status)) {//当前三个月
                if (pay_month.equals(1)) {//当前月份为1月份
                    payTimeStart = pay_year + "-" + pay_month;
                    payTimeEnd = pay_year + "-" + pay_month;
                } else if (pay_month.equals(2)) {//当前月份为2月份
                    String Two = Integer.toString(payMonthOne - 1);
                    if (Two.length() == 1) {
                        Two = "0" + Two;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + Two;
                    payTimeEnd = pay_year + "-" + one;
                } else {//当前月份大于3月份
                    String there = Integer.toString(payMonthOne - 2);
                    if (there.length() == 1) {
                        there = "0" + there;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + there;
                    payTimeEnd = pay_year + "-" + one;
                }
            } else if ("0".equals(status)) {
                payTimeStart = pay_year + "-" + pay_month;
                payTimeEnd = pay_year + "-" + pay_month;
            } else {
                payTimeStart = pay_year + "-" + "01";
                payTimeEnd = pay_year + "-" + "12";
            }
            List<Map<String, Object>> findsysDepts = jcxfSJKBMapper.partMeeting(payTimeStart, payTimeEnd);
            for (Map<String, Object> findDept : findsysDepts) {
                int kzcount = Integer.parseInt(findDept.get("kzcount").toString());
                int zbcount = Integer.parseInt(findDept.get("zbcount").toString());
                String centage = percent.apply(kzcount, zbcount);
                findDept.put("centage", centage);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(findsysDepts));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> qtpartMeeting(Map<String, Object> requestMap) {
        try {
            Long deptId = 1L;
            String pay_year = DateUtils.getYear();
            String pay_month = DateUtils.getMonth();
            String payTimeStart;
            String payTimeEnd;
            int payMonthOne = Integer.parseInt(pay_month);
            String status = requestMap.get("status").toString();
            if ("1".equals(status)) {//当前三个月
                if (pay_month.equals(1)) {//当前月份为1月份
                    payTimeStart = pay_year + "-" + pay_month;
                    payTimeEnd = pay_year + "-" + pay_month;
                } else if (pay_month.equals(2)) {//当前月份为2月份
                    String Two = Integer.toString(payMonthOne - 1);
                    if (Two.length() == 1) {
                        Two = "0" + Two;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + Two;
                    payTimeEnd = pay_year + "-" + one;
                } else {//当前月份大于3月份
                    String there = Integer.toString(payMonthOne - 2);
                    if (there.length() == 1) {
                        there = "0" + there;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + there;
                    payTimeEnd = pay_year + "-" + one;
                }
            } else if ("0".equals(status)) {
                payTimeStart = pay_year + "-" + pay_month;
                payTimeEnd = pay_year + "-" + pay_month;
            } else {
                payTimeStart = pay_year + "-" + "01";
                payTimeEnd = pay_year + "-" + "12";
            }
            List<Map<String, Object>> findsysDepts = jcxfSJKBMapper.qtpartMeeting(payTimeStart, payTimeEnd);
            for (Map<String, Object> findDept : findsysDepts) {
                int kzcount = Integer.parseInt(findDept.get("kzcount").toString());
                int zbcount = Integer.parseInt(findDept.get("zbcount").toString());
                String centage = percent.apply(kzcount, zbcount);
                findDept.put("centage", centage);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(findsysDepts));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> theoryMetting(Map<String, Object> requestMap) {
        try {
            Long deptId = 1L;
            String pay_year = DateUtils.getYear();
            String pay_month = DateUtils.getMonth();
            String payTimeStart;
            String payTimeEnd;
            int payMonthOne = Integer.parseInt(pay_month);
            String status = requestMap.get("status").toString();
            if ("1".equals(status)) {//当前三个月
                if (pay_month.equals(1)) {//当前月份为1月份
                    payTimeStart = pay_year + "-" + pay_month;
                    payTimeEnd = pay_year + "-" + pay_month;
                } else if (pay_month.equals(2)) {//当前月份为2月份
                    String Two = Integer.toString(payMonthOne - 1);
                    if (Two.length() == 1) {
                        Two = "0" + Two;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + Two;
                    payTimeEnd = pay_year + "-" + one;
                } else {//当前月份大于3月份
                    String there = Integer.toString(payMonthOne - 2);
                    System.out.println(there.length());
                    if (there.length() == 1) {
                        there = "0" + there;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + there;
                    payTimeEnd = pay_year + "-" + one;
                }
            } else if ("0".equals(status)) {
                payTimeStart = pay_year + "-" + pay_month;
                payTimeEnd = pay_year + "-" + pay_month;
            } else {
                payTimeStart = pay_year + "-" + "01";
                payTimeEnd = pay_year + "-" + "12";
            }
            List<Map<String, Object>> findsysDepts = jcxfSJKBMapper.theoryMetting(payTimeStart, payTimeEnd);
            for (Map<String, Object> findDept : findsysDepts) {
                int deptcount = Integer.parseInt(findDept.get("deptcount").toString());
                int doneDeptNum = Integer.parseInt(findDept.get("doneDeptNum").toString());
                String centage = percent.apply(doneDeptNum, deptcount);
                findDept.put("value", centage);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(findsysDepts));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> qttheoryMetting(Map<String, Object> requestMap) {
        try {
            Long deptId = 1L;
            String pay_year = DateUtils.getYear();
            String pay_month = DateUtils.getMonth();
            String payTimeStart;
            String payTimeEnd;
            int payMonthOne = Integer.parseInt(pay_month);
            String status = requestMap.get("status").toString();
            if ("1".equals(status)) {//当前三个月
                if (pay_month.equals(1)) {//当前月份为1月份
                    payTimeStart = pay_year + "-" + pay_month;
                    payTimeEnd = pay_year + "-" + pay_month;
                } else if (pay_month.equals(2)) {//当前月份为2月份
                    String Two = Integer.toString(payMonthOne - 1);
                    if (Two.length() == 1) {
                        Two = "0" + Two;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + Two;
                    payTimeEnd = pay_year + "-" + one;
                } else {//当前月份大于3月份
                    String there = Integer.toString(payMonthOne - 2);
                    if (there.length() == 1) {
                        there = "0" + there;
                    }
                    String one = Integer.toString(payMonthOne);
                    payTimeStart = pay_year + "-" + there;
                    payTimeEnd = pay_year + "-" + one;
                }
            } else if ("0".equals(status)) {
                payTimeStart = pay_year + "-" + pay_month;
                payTimeEnd = pay_year + "-" + pay_month;
            } else {
                payTimeStart = pay_year + "-" + "01";
                payTimeEnd = pay_year + "-" + "12";
            }
            List<Map<String, Object>> findsysDepts = jcxfSJKBMapper.qttheoryMetting(payTimeStart, payTimeEnd);
            for (Map<String, Object> findDept : findsysDepts) {
                int deptcount = Integer.parseInt(findDept.get("deptcount").toString());
                int doneDeptNum = Integer.parseInt(findDept.get("doneDeptNum").toString());
                String centage = percent.apply(doneDeptNum, deptcount);
                findDept.put("value", centage);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(findsysDepts));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getLifeDetailByDept(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String type = (String) requestMap.get("type");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("pageVo")), PageVo.class);

            String startTime = "";
            String endTime = "";

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            if ("thisMonth".equals(type)) {
                int actualMinimum = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
                int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),actualMinimum,00,00,00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),actualMaximum,23,59,59);
                endTime = simpleDateFormat.format(instance.getTime());

            } else if ("threeMonth".equals(type)) {
                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH),23,59,59);
                endTime = simpleDateFormat.format(instance.getTime());

                instance.add(Calendar.MONTH, -2);
                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH),00,00,00);
                startTime = simpleDateFormat.format(instance.getTime());

            } else if ("thisYear".equals(type)) {
                instance.set(instance.get(Calendar.YEAR), 1, 1,00,00,00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), 12, 31,23,59,59);
                endTime = simpleDateFormat.format(instance.getTime());
            }


            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("id,name");
            queryWrapper.eq("del_flag", 0);
            // queryWrapper.in("id", childDeptIds);
            queryWrapper.like("parent_ids", ","+deptId+",");
            queryWrapper.in("type", typeList);


            // 分页查询
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
            Page<JcxfSysDept> pageData = new Page<>(page, limit);
            IPage<JcxfSysDept> depts = jcxfSysDeptMapper.selectPage(pageData, queryWrapper);

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < depts.getRecords().size(); i++) {
                JcxfSysDept tzSysDept = depts.getRecords().get(i);
                int count = 0;

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", tzSysDept.getId());
                jsonObject.put("name", tzSysDept.getName());

                List<Integer> list = new ArrayList<>();
                list.add(tzSysDept.getId());
                /*List<Map<String, Long>> mapList = jcxfOrganizationLifeMapper.getLifeCountToRankQxAndOther(startTime, endTime, list);

                for (Map<String, Long> map: mapList) {
                    Integer num = map.get("num").intValue();
                    if (num > 0) {
                        count++;
                        break;
                    }
                }
*/
                if (count > 0) {
                    jsonObject.put("count", "是");
                } else {
                    jsonObject.put("count", "-");
                }
                jsonObject.put("index", i);

                jsonArray.add(jsonObject);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("total", depts.getTotal());
            map.put("data", jsonArray);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    BiFunction<Integer, Integer, String> percent = new BiFunction<Integer, Integer, String>() { //计算百分比
        @Override
        public String apply(Integer num1, Integer num2) {
            if (num1 == 0 && num2 == 0) {
                return "0%";
            }
            if (num1 <= 0) {
                return "0%";
            }
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            return numberFormat.format((float) num1 / (float) num2 * 100) + "%";
        }
    };
}

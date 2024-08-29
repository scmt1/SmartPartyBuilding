package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.JcxfOrganizationLifeService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.*;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.ShortMessageResult;
import me.flyray.bsin.server.utils.ShortMessageUtil;
import me.flyray.bsin.server.utils.jcxf.Constant;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class JcxfOrganizationLifeServiceImpl extends ServiceImpl<JcxfOrganizationLifeMapper, JcxfOrganizationLife> implements JcxfOrganizationLifeService {
    @Autowired
    private TzMessageMapper tzMessageMapper;
    @Autowired
    private TzMessageDetailMapper tzMessageDetailMapper;
    @Autowired
    private JcxfSysDeptServiceImpl jcxfSysDeptService;
    @Autowired
    private JcxfOrganizationLifeMapper jcxfOrganizationLifeMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;
    @Autowired
    private TzMessageAutoMapper messageAutoMapper;

    @Autowired
    private JcxfPartyMeetingDocumentMapper jcxfPartyMeetingDocumentMapper;

    @Autowired
    private BinSysUserMapper binSysUserMapper;

    @Autowired
    private JcxfPartyMeetingUserMapper jcxfPartyMeetingUserMapper;

    @Autowired
    private JcxfPartyTeamMapper jcxfPartyTeamMapper;

    @Autowired
    private TzMessageAutoRoleMapper tzMessageAutoRoleMapper;

    @Autowired
    private TzMessageAutoContentMapper tzMessageAutoContentMapper;

    /**
     * 分页查询组织生活
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> queryTzOrganizationLifeList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(map.get("tzOrganizationLife")), JcxfOrganizationLife.class);

            IPage<JcxfOrganizationLife> result = queryTzOrganizationLifeListByPage(tzOrganizationLife, searchVo, pageVo);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> appQueryTzOrganizationLifeList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(map.get("tzOrganizationLife")), JcxfOrganizationLife.class);

            IPage<JcxfOrganizationLife> result = queryTzOrganizationLifeListByPage(tzOrganizationLife, searchVo, pageVo);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    /**
     * 删除会议
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> deleteTzOrganizationLife(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");
        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            String userId = (String) requestMap.get("userId");

            UpdateWrapper<JcxfOrganizationLife> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("del_flag", 1).in("id", ids);
            updateWrapper.set("update_by", userId);
            boolean res = update(updateWrapper);
            if (res) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    /**
     * 统计三会一课的数量
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> queryMeetingClass(Map<String, Object> requestMap) {
        try {
            JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfOrganizationLife.class);

            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<JcxfSysDept>();
            List<Integer> listAll = new ArrayList<>();

            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (tzOrganizationLife.getDeptId() != null) {
                queryWrapper.and(i -> i.eq("del_flag", 0));
                if (StringUtils.isNotBlank(tzOrganizationLife.getOrganizationType())) {
                    queryWrapper.and(i -> i.eq("organization_type", tzOrganizationLife.getOrganizationType()));
                }

                if (StringUtils.isNotBlank(tzOrganizationLife.getVeteran())) {
                    queryWrapper.and(i -> i.eq("veteran", tzOrganizationLife.getVeteran()));
                }

                queryWrapper.and(i -> i.eq(("id"), tzOrganizationLife.getDeptId())
                        .or(i2 -> i2.like("parent_ids", "," + tzOrganizationLife.getDeptId() + ",")));
                List<JcxfSysDept> deptList = jcxfSysDeptService.list(queryWrapper);
                if (deptList == null || deptList.size() == 0) {
                    List<Map<String, Integer>> error = null;
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(error));
                }
                for (JcxfSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }

            }
            List<Map<String, Integer>> mapList = Collections.synchronizedList(new ArrayList<>());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            ExecutorService executor = Executors.newFixedThreadPool(10);//做10个线程

            if (tzOrganizationLife.getMeetingType().intValue() == 1) {
                for (int i = 0; i < 10; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.YEAR, -i);
                    Date time = calendar.getTime();
                    String year = sdf.format(time);
                    thread(listAll, mapList, executor, i, year);
                }
            } else if (tzOrganizationLife.getMeetingType().intValue() == 6 || tzOrganizationLife.getMeetingType().intValue() == 3) {
                for (int i = 0; i < 10; i++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.YEAR, -i);
                    Date time = calendar.getTime();
                    String year = sdf.format(time);
                    thread2(listAll, mapList, executor, String.valueOf(tzOrganizationLife.getMeetingType()), year);
                }
            }
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);//设置等待时间最大（即为不设置）
            Map<String, Integer> tmp = null;
            for (int i = 0; i < mapList.size() - 1; i++) {
                for (int j = 0; j < mapList.size() - 1 - i; j++) {
                    if (mapList.get(j).get("year") < mapList.get(j + 1).get("year")) {
                        tmp = mapList.get(j);
                        mapList.set(j, mapList.get(j + 1));
                        mapList.set(j + 1, tmp);
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(mapList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getMeetingCountNearlyThreeMonths(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());

            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 2, 1, 00, 00, 00);
            String startTime = simpleDateFormat.format(calendar.getTime());

            int month = calendar.get(Calendar.MONTH);
            calendar.set(Calendar.MONTH, month + 3);
            calendar.set(Calendar.DATE, 1);
            calendar.add(Calendar.DATE, -1);
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 23, 59, 59);
            String endTime = simpleDateFormat.format(calendar.getTime());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("DATE_FORMAT(start_time, '%Y-%m') as time, count(*) as num");
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.ge("start_time", startTime);
            queryWrapper.le("start_time", endTime);
            queryWrapper.eq("meeting_status", 3);
            queryWrapper.eq("meeting_type", 1);
            queryWrapper.groupBy("DATE_FORMAT(start_time, '%Y-%m')");

            List<Map<String, Object>> list = jcxfOrganizationLifeMapper.selectMaps(queryWrapper);
            if (list != null && list.size() > 0 && list.get(0) != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryMeetingClassType(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 2);
            int thisMonth = calendar.get(calendar.MONTH);


            List<Map<String, Object>> mapList = new ArrayList<>();
            List<String> monthList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                Map<String, Object> decimalMap = null;
                // 判断当前月份是否为1月份
                if (thisMonth == 1) {
                    // 如果是1月份，则跨年
                    year--;
                    thisMonth = 12;
                } else {
                    thisMonth--;
                }
                // 生成当前月份的字符串
                String monthStr = year + "-" + String.format("%02d", thisMonth) + "-01";
                monthList.add(monthStr);
                decimalMap = jcxfOrganizationLifeMapper.queryMeetingClassType(monthStr, deptId);
                mapList.add(decimalMap);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(mapList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryMeetingClassTypeByThisMonth(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            calendar.add(Calendar.MONTH, 2);
            int thisMonth = calendar.get(calendar.MONTH) - 1;


            //List<Map<String, Object>> mapList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            List<String> monthList = new ArrayList<>();
            String monthStr = year + "-" + String.format("%02d", thisMonth) + "-01";

            // Map<String, Object> shouldDecimalMap = jcxfOrganizationLifeMapper.queryShouldMeetingClassTypeByThisMonth(monthStr, deptId);
            Map<String, Object> actuallyDecimalMap = jcxfOrganizationLifeMapper.queryActuallyMeetingClassTypeByThisMonth(monthStr, deptId);
            // map.put("should", shouldDecimalMap);
            map.put("actually", actuallyDecimalMap);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    private void thread(List<Integer> listAll, List<Map<String, Integer>> mapList, ExecutorService executor, int i, String year) throws InterruptedException, ExecutionException {
        executor.submit(() -> {
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("year", Integer.parseInt(year));
            //查询出某一年所有部门的数据
            if (listAll != null && listAll.size() > 0) {

                Map<String, BigDecimal> meetingClass = new HashMap<>();
                meetingClass = jcxfOrganizationLifeMapper.queryMeetingClass(listAll, year);

                if (meetingClass != null && meetingClass.size() > 0) {
                    map.put("january", (Integer.parseInt(meetingClass.get("january").toString()) != 0 ? Integer.parseInt(meetingClass.get("january").toString()) : 0));
                    map.put("february", (Integer.parseInt(meetingClass.get("february").toString()) != 0 ? Integer.parseInt(meetingClass.get("february").toString()) : 0));
                    map.put("march", (Integer.parseInt(meetingClass.get("march").toString()) != 0 ? Integer.parseInt(meetingClass.get("march").toString()) : 0));
                    map.put("april", (Integer.parseInt(meetingClass.get("april").toString()) != 0 ? Integer.parseInt(meetingClass.get("april").toString()) : 0));
                    map.put("may", (Integer.parseInt(meetingClass.get("may").toString()) != 0 ? Integer.parseInt(meetingClass.get("may").toString()) : 0));
                    map.put("june", (Integer.parseInt(meetingClass.get("june").toString()) != 0 ? Integer.parseInt(meetingClass.get("june").toString()) : 0));
                    map.put("july", (Integer.parseInt(meetingClass.get("july").toString()) != 0 ? Integer.parseInt(meetingClass.get("july").toString()) : 0));
                    map.put("august", (Integer.parseInt(meetingClass.get("august").toString()) != 0 ? Integer.parseInt(meetingClass.get("august").toString()) : 0));
                    map.put("september", (Integer.parseInt(meetingClass.get("september").toString()) != 0 ? Integer.parseInt(meetingClass.get("september").toString()) : 0));
                    map.put("october", (Integer.parseInt(meetingClass.get("october").toString()) != 0 ? Integer.parseInt(meetingClass.get("october").toString()) : 0));
                    map.put("november", (Integer.parseInt(meetingClass.get("november").toString()) != 0 ? Integer.parseInt(meetingClass.get("november").toString()) : 0));
                    map.put("december", (Integer.parseInt(meetingClass.get("december").toString()) != 0 ? Integer.parseInt(meetingClass.get("december").toString()) : 0));
                } else {
                    map.put("january", 0);
                    map.put("february", 0);
                    map.put("march", 0);
                    map.put("april", 0);
                    map.put("may", 0);
                    map.put("june", 0);
                    map.put("july", 0);
                    map.put("august", 0);
                    map.put("september", 0);
                    map.put("october", 0);
                    map.put("november", 0);
                    map.put("december", 0);
                }
            }
            mapList.add(map);
        });
    }

    private void thread2(List<Integer> listAll, List<Map<String, Integer>> mapList, ExecutorService executor, String type, String year) throws InterruptedException {
        executor.submit(() -> {
            //Map<String, Integer> map = new HashMap<>();
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("year", Integer.parseInt(year));
            //查询出某一年所有部门的数据
            if (listAll != null && listAll.size() > 0) {
                Integer num = jcxfOrganizationLifeMapper.queryOrganizationLife(listAll, year, type);
                map.put("oneYear", num);
            }
            mapList.add(map);
        });
    }

    @Override
    public Map<String, Object> countMyTotalClassNum(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("id");

        List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        deptIds.add(Long.valueOf(deptId));

        if (deptIds == null || deptIds.size() == 0) {
            Map<Object, Object> map = new HashMap<>();
            // map.put("countMy", 0);
            map.put("countOur", 0);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        }

        QueryWrapper<JcxfOrganizationLife> queryWrapper2 = new QueryWrapper<JcxfOrganizationLife>();

        queryWrapper2.eq("del_flag", 0);
        queryWrapper2.eq("meeting_status", 3); // 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消)
        queryWrapper2.in("dept_id", deptIds);
        Integer countOur = jcxfOrganizationLifeMapper.selectCount(queryWrapper2);


        /*QueryWrapper<JcxfOrganizationLife> queryWrapper = new QueryWrapper<JcxfOrganizationLife>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.eq("meeting_status", 3);
        queryWrapper.eq("dept_id", deptId);
        Integer countMy = jcxfOrganizationLifeMapper.selectCount(queryWrapper);*/

        Map<Object, Object> map = new HashMap<>();
        //map.put("countMy", countMy);
        map.put("countOur", countOur);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

    }

    @Override
    public Map<String, Object> setPlanMeetingParty(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String partyIds = (String) map.get("partyIds");
            String id = (String) map.get("id");
            JcxfOrganizationLife life = jcxfOrganizationLifeMapper.selectById(id);
            if (life == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取会议出错");
            }

            // 保存前，先验证本部门人员
            String[] partyIdList = partyIds !=null? partyIds.split(","): new String[0];
            if (partyIdList.length == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"请选择拟参会人员");
            }

            QueryWrapper q = new QueryWrapper();
            q.select("id");
            q.eq("del_flag", false);
            q.eq("dept_id", life.getDeptId());
            q.in("id", partyIdList);
            List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.selectList(q);
            List<Long> ids = new ArrayList<>();
            for (JcxfPartyMember member: memberList) {
                ids.add(member.getId());
            }

            if (ids.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"请选择拟参会人员");
            }

            String str = ids.toString().replace("[", "").replace("]", "").replaceAll(" ", "");
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("plan_meeting_party_ids", str);

            int res = jcxfOrganizationLifeMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> getOrganizationLifeById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            JcxfOrganizationLife life = jcxfOrganizationLifeMapper.selectById(id);
            if (life != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(life));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPlanMeetingPartyMemberInfo(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            JcxfOrganizationLife life = jcxfOrganizationLifeMapper.selectById(id);
            if (life != null) {
                if (StringUtils.isNotBlank(life.getPlanMeetingPartyIds())) {
                    String[] ids = life.getPlanMeetingPartyIds().split(",");

                    // 重新校验一遍，避免人员调用引起的错误
                    QueryWrapper q = new QueryWrapper();
                    q.select("id,avatar,real_name,phone");
                    q.eq("del_flag", false);
                    q.eq("dept_id", life.getDeptId());
                    q.in("id", ids);
                    List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(q);
                    // List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectBatchIds(Arrays.asList(ids));
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到信息");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPlanMeetingAllNumAndTypeNum(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String meetingType = (String) map.get("meetingType");
            String deptId = (String) map.get("deptId");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);

            List<Integer> statusList = new ArrayList<>();
            // statusList.add(2);
            statusList.add(3);

            QueryWrapper q2 = new QueryWrapper();
            q2.eq("del_flag", 0);
            q2.eq("dept_id", deptId);
            q2.in("meeting_status", statusList);// 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            q2.eq("meeting_type", meetingType);
            q2.ge("start_time", year + "-01-01 00:00:00");
            q2.le("start_time", year + "-12-31 23:59:59");
            int typeNum = jcxfOrganizationLifeMapper.selectCount(q2);

            Map<String, Integer> result = new HashMap<>();
            // result.put("allNum", allNum);
            result.put("num", typeNum);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    /**
     * 根据会议id查询会议图片
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> findImgById(Map<String, Object> requestMap) {
        String meetingId = String.valueOf(requestMap.get("id"));
        if (StringUtils.isBlank(meetingId)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("type", "1"); // 1 会议图片 2、签到图片 3、 活动上传文件 4、会议记录照片
            queryWrapper1.eq("meeting_id", meetingId);
            queryWrapper1.eq("del_flag", false);
            List<JcxfPartyMeetingDocument> documents1 = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper1);

            QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("type", "4"); // 1 会议图片 2、签到图片 3、 活动上传文件 4、会议记录照片
            queryWrapper2.eq("meeting_id", meetingId);
            queryWrapper2.eq("del_flag", false);
            List<JcxfPartyMeetingDocument> documents2 = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper2);

            Map<String, Object> map = new HashMap<>();
            map.put("list1", documents1);
            map.put("list2", documents2);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    /**
     * 取消会议
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> cancelMeeting(Map<String, Object> requestMap) {
        JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfOrganizationLife.class);
        try {
            Long id = tzOrganizationLife.getId();
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员");
            }

            JcxfOrganizationLife oldLife = getById(id);
            if (oldLife == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"数据不存在");
            }

            if (oldLife.getMeetingStatus().equals(Constant.MEETING_4)) {//取消会议
                if (tzOrganizationLife.equals(Constant.MEETING_3)) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"当前会议已结束");
                }
            }
            String userId = (String) requestMap.get("userId");

            UpdateWrapper<JcxfOrganizationLife> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("meeting_status", "4"); // 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            updateWrapper.set("update_by", userId);

            boolean update = update(null, updateWrapper);
            if (update) {
                QueryWrapper up = new QueryWrapper();
                up.eq("meeting_id", id);
                jcxfPartyMeetingUserMapper.delete(up);

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    /**
     * 开始会议
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> startMeeting(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            String userId = (String) requestMap.get("userId");

            UpdateWrapper<JcxfOrganizationLife> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", id);
            updateWrapper.set("meeting_status", 2); // 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            updateWrapper.set("update_by", userId);

            JcxfOrganizationLife byId = getById(id);
            if (byId == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"数据不存在");
            }

            boolean update = update(null, updateWrapper);
            if (update) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    /**
     * 根据id删除
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> deleteFileById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            int res = jcxfPartyMeetingDocumentMapper.deleteById(id);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    /**
     * 保存学习附件
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> saveStudyFile(Map<String, Object> requestMap) {
        try {
            JcxfPartyMeetingDocument document = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfPartyMeetingDocument.class);
            if (document == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"数据为空");
            }
            document.setUploadTime(new Date());
            document.setCreateDate(new Date());
            document.setType("3");
            document.setDelFlag(false);
            int save = jcxfPartyMeetingDocumentMapper.insert(document);
            if (save > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    /**
     * 查询学习附件
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> findStudyFile(Map<String, Object> requestMap) {
        String meetingId = (String) requestMap.get("meetingId");
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("meeting_id", meetingId);
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("type", "3"); // 1 会议图片 2、签到图片 3、 活动上传文件
            List<JcxfPartyMeetingDocument> documentList = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(documentList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    /**
     * 保存图片
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> saveImg(Map<String, Object> requestMap) {
        try {
            List<JcxfPartyMeetingDocument> documentList = JSONObject.parseArray(JSON.toJSONString(requestMap.get("data")), JcxfPartyMeetingDocument.class);

            boolean flag = true;
            if (documentList == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"请上传图片");
            }
            for (JcxfPartyMeetingDocument document : documentList) {
                int res = 0;
                if (document.getId() == null) {
                    document.setDelFlag(false);
                    document.setCreateDate(new Date());
                    res = jcxfPartyMeetingDocumentMapper.insert(document);
                } else {
                    document.setDelFlag(false);
                    document.setUploadTime(new Date());
                    res = jcxfPartyMeetingDocumentMapper.updateById(document);
                }
                if (res < 1) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> updateTzOrganizationLife(Map<String, Object> requestMap) {
        JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfOrganizationLife.class);
        if (StringUtils.isEmpty(tzOrganizationLife.getId().toString())) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            JcxfOrganizationLife oldLife = jcxfOrganizationLifeMapper.selectById(tzOrganizationLife.getId());
            if (oldLife.getMeetingStatus() > 1) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"会议已开始，不能编辑会议");
            }

            tzOrganizationLife.setUpdateDate(new Date());
            boolean res = updateById(tzOrganizationLife);
            if (res) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> updateTzOrganizationLifeRecord(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String id = (String) map.get("id");
            String situationRecord = (String) map.get("situationRecord");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("situation_record", situationRecord);

            Boolean res = update(updateWrapper);
            if (res) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }


    /**
     * 根据主键来获取test数据
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getTzOrganizationLife(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isEmpty(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            Map<String, Object> map = new HashMap<>();
            JcxfOrganizationLife res = getById(id);
            if (res != null) {
                // 主持人姓名
                if (StringUtils.isNotBlank(res.getHost())) {
                    JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectById(res.getHost());
                    if (jcxfPartyMember != null) {
                        res.setHostName(jcxfPartyMember.getRealName());
                        res.setHostAvatar(jcxfPartyMember.getAvatar());
                    }
                }
                if (StringUtils.isNotBlank(res.getRecorder())) {
                    JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectById(res.getRecorder());
                    if (jcxfPartyMember != null) {
                        res.setRecorderName(jcxfPartyMember.getRealName());
                        res.setRecorderAvatar(jcxfPartyMember.getAvatar());
                    }
                }

                map.put("main", res);

                //会议图片查询
                Long meetingId = res.getId();
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("type", "1"); // 1 会议图片 2、签到图片 3、 活动上传文件 4、会议记录照片
                queryWrapper1.eq("meeting_id", meetingId);
                queryWrapper1.eq("del_flag", false);
                List<JcxfPartyMeetingDocument> documents1 = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper1);
                map.put("imageList", documents1);

                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("type", "3"); // 1 会议图片 2、签到图片 3、 活动上传文件 4、会议记录照片
                queryWrapper2.eq("meeting_id", meetingId);
                queryWrapper2.eq("del_flag", false);
                List<JcxfPartyMeetingDocument> documents2 = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper2);
                map.put("fileList", documents2);

                // 实际参会人员信息
                List<JcxfMeetingPojoUser> list = jcxfOrganizationLifeMapper.selectEndMeetingUserByDeptId(meetingId, res.getMeetingType());
                List<JcxfMeetingPojoUser> flowList = jcxfOrganizationLifeMapper.selectEndMeetingFlowUserByDeptId(meetingId);
                List<JcxfMeetingPojoUser> joinList = new ArrayList<JcxfMeetingPojoUser>();
                list.addAll(flowList);
                for (JcxfMeetingPojoUser meetingPojoUser : list) {
                    try {
                        if (meetingPojoUser.getStatus().equals(Constant.MEETING_USER_STATUS_6) || meetingPojoUser.getStatus().equals(Constant.MEETING_USER_STATUS_5)) {
                            joinList.add(meetingPojoUser);
                        }
                    } catch (Exception e) {

                    }
                }
                map.put("joinList", joinList);


                if (StringUtils.isNotBlank(res.getPlanMeetingPartyIds())) {
                    String[] ids = res.getPlanMeetingPartyIds().split(",");
                    if (ids.length > 0) {
                        QueryWrapper queryWrapper = new QueryWrapper();
                        queryWrapper.in("id", ids);
                        map.put("shouldJoinList", jcxfPartyMemberMapper.selectList(queryWrapper));
                    } else {
                        map.put("shouldJoinList", null);
                    }
                } else {
                    map.put("shouldJoinList", null);
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    /**
     * 结束会议
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> endMeeting(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String userId = (String) requestMap.get("userId");

            Long meetingId = Long.valueOf((String) map.get("meetingId"));
            JcxfOrganizationLife partyMeeting = jcxfOrganizationLifeMapper.selectById(meetingId);
            if (partyMeeting.getMeetingStatus() < Constant.MEETING_3) {//当前会议是否是未开始
                partyMeeting.setMeetingStatus(Constant.MEETING_3);
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"结束会议失败，会议状态异常");
            }

            String data = (String) map.get("data");
            //数据解析
            JSONArray allData = JSONArray.parseArray(data);
            HashSet<Long> set = new HashSet<Long>();
            Iterator it = allData.iterator();
            while (it.hasNext()) {
                JSONObject deptInfo = (JSONObject) it.next();
                Long deptId = deptInfo.getLong("deptId");
                //判断是否是当前部门
                //zdj 2021-12-03 根据部门去重
                if (set.contains(deptId))
                    continue;
                set.add(deptId);

                if (partyMeeting.getDeptId().equals(deptId)) {
                    //保存人员参会信息 todo 积分暂时不考虑
                    String[] mainMember = saveMeetingUser(deptInfo, meetingId, deptId);
                    partyMeeting.setHost(mainMember[0]);
                    partyMeeting.setRecorder(mainMember[1]);
                    partyMeeting.setJoinAllCount(Integer.parseInt(mainMember[2]));
                    partyMeeting.setUpdateBy(Long.valueOf(userId));

                    jcxfOrganizationLifeMapper.updateById(partyMeeting);
                } else {
                    //拷贝当前会议到其他部门 >>> 清空会议ID 设置支部ID
                    partyMeeting.setId(null);
                    partyMeeting.setDeptId(deptId);
                    // 结束会议时, 勾选其他支部一并作为结束会议的:
                    // 将一起结束会议的支部会议作为同一批次处理, 以便后续同步更新会议照片和记录
                    // 2020/04/21
                    partyMeeting.setBatchId(meetingId);
                    partyMeeting.setCreateBy(Long.valueOf(userId));
                    jcxfOrganizationLifeMapper.insert(partyMeeting);
                    //获取主键
                    Long newId = partyMeeting.getId();
                    //若存在文档则拷贝
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.eq("meeting_id", meetingId);
                    List<JcxfPartyMeetingDocument> docList = jcxfPartyMeetingDocumentMapper.selectList(queryWrapper);
                    for (JcxfPartyMeetingDocument doc : docList) {
                        doc.setId(null);
                        doc.setMeetingId(newId);
                        jcxfPartyMeetingDocumentMapper.insert(doc);
                    }
                    //保存人员参会信息
                    String[] mainMember = saveMeetingUser(deptInfo, newId, deptId);
                    partyMeeting.setHost(mainMember[0]);
                    partyMeeting.setRecorder(mainMember[1]);
                    partyMeeting.setJoinAllCount(Integer.parseInt(mainMember[2]));
                    jcxfOrganizationLifeMapper.updateById(partyMeeting);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    private String[] saveMeetingUser(JSONObject deptInfo, Long meetingId, Long deptId) {
        Iterator it = deptInfo.getJSONArray("members").iterator();//对应支部人员集合 >> 以前段的8列选项划分开
        String[] noComeMembers = new String[5]; //请假人员的集合 一共5列数据
        String[] mainMember = new String[3];//主持人 和 记录人 用于主表的更新
        while (it.hasNext()) {
            JSONObject currentMember = (JSONObject) it.next();
            Integer status = currentMember.getInteger("status");//当前列 以0开始
            String member = currentMember.getString("member");//当前列对应的成员信息
            if (status == 0) { //处理缺席人员
                noComeMembers[0] = member;
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_1);
            } else if (status == 1) { //处理病假人员
                noComeMembers[1] = member;
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_2);
            } else if (status == 2) { //处理公假人员
                noComeMembers[2] = member;
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_3);
            } else if (status == 3) { //处理事假人员
                noComeMembers[3] = member;
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_4);
            } else if (status == 4) { //处理迟到人员
                noComeMembers[4] = member;
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_5);
            } else if (status == 5) { //处理主持人 - 正常参会人员 最后统一处理
                mainMember[0] = member;
            } else if (status == 6) { //处理记录人 - 正常参会人员 最后统一处理
                mainMember[1] = member;
            } else if (status == 7) { //处理流动党员参会人员  - 正常参会人员 特殊处理
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_6);
            } else if (status == 8) {//第8列为参会人员，新增功能
                saveMeetingUserByStatus(member, meetingId, Constant.MEETING_USER_STATUS_6);
                String[] joinMember = getOtherUser(meetingId, deptId, noComeMembers, member, mainMember);
                mainMember[2] = (Integer.parseInt(joinMember[1]) + (noComeMembers[4] != null ? noComeMembers[4].split(",").length : 0) + (noComeMembers[3] != null ? noComeMembers[3].split(",").length : 0) + (noComeMembers[2] != null ? noComeMembers[2].split(",").length : 0) + (noComeMembers[1] != null ? noComeMembers[1].split(",").length : 0) + (noComeMembers[0] != null ? noComeMembers[0].split(",").length : 0)) + "";//参会人数
            }
        }
        return mainMember;
    }

    //目前后面存入的会更新前面的数据
    private void saveMeetingUserByStatus(String members, Long meetingId, Integer status) {
        if (StringUtils.isNotBlank(members)) { //members != null
            String[] member = members.split(",");
            for (String mId : member) {
                Long partyId = Long.parseLong(mId);
                JcxfPartyMeetingUser partyMeetingUser = new JcxfPartyMeetingUser();
                partyMeetingUser.setMeetingId(meetingId);
                partyMeetingUser.setUserId(partyId);

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("meeting_id", meetingId);
                queryWrapper.eq("user_id", partyId);
                partyMeetingUser = jcxfPartyMeetingUserMapper.selectOne(queryWrapper);
                if (null == partyMeetingUser) {
                    partyMeetingUser = new JcxfPartyMeetingUser();
                    partyMeetingUser.setUserStatus(status);
                    partyMeetingUser.setMeetingId(meetingId);
                    partyMeetingUser.setUserId(partyId);
                    partyMeetingUser.setUserPosition(jcxfPartyMemberMapper.isHasPositionByPartyId(partyId));
                    partyMeetingUser.setDelFlag(false);
                    jcxfPartyMeetingUserMapper.insert(partyMeetingUser);
                } else {
                    partyMeetingUser.setUserPosition(jcxfPartyMemberMapper.isHasPositionByPartyId(partyId));
                    partyMeetingUser.setDelFlag(false);
                    partyMeetingUser.setUserStatus(status);
                    jcxfPartyMeetingUserMapper.updateById(partyMeetingUser);
                }
            }
        }
    }

    private String[] getOtherUser(Long meetingId, Long deptId, String[] noComeMembers, String flowMember, String[] mainMember) {
        JcxfOrganizationLife meeting = this.getByIdAndCustomSelectSql(meetingId);
        List<JcxfPartyMember> list = jcxfOrganizationLifeMapper.selectAllMeetingUserByDeptId(deptId, meetingId, meeting.getMeetingType(), null);//查询支部所有人员 包含了流动党员
        String regNoComeMembers = "";
        for (int i = 0; i < noComeMembers.length; i++) {
            String currentNoComeMembers = noComeMembers[i];
            if (null == currentNoComeMembers) continue;
            String[] currentMember = currentNoComeMembers.split(",");
            for (int j = 0; j < currentMember.length; j++) {
                if (regNoComeMembers.contains(currentMember[j])) continue;//这里可能会有问题
                regNoComeMembers += (currentMember[j] + ",");
            }
        }
        String[] leaveUser = regNoComeMembers.split(",");
        String reMember = "";
        int allCount = 0;
        Iterator<JcxfPartyMember> it = list.iterator();
        while (it.hasNext()) {
            JcxfPartyMember partyMember = it.next();
            String id = partyMember.getId().toString();
            boolean flag = true;//能否添加
            for (String uId : leaveUser) {
                if (id.equals(uId)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                allCount++;
                if ("1".equals(partyMember.getIsFlow())) {//如是流动党员
                    // 不在流动党员参会列表 或者也不在 主持人和记录人中 这种 真实在外流动的人员 需要排除
                    if ((StringUtils.isNotBlank(flowMember) && flowMember.indexOf(id) == -1) || (!mainMember[0].equals(id) && !mainMember[1].equals(id))) {
                        continue;
                    }
                }
                reMember += (id + ",");
            }
        }
        return new String[]{reMember, allCount + ""};
    }

    public JcxfOrganizationLife getByIdAndCustomSelectSql(Long id) {
        if (id == null) {
            return jcxfOrganizationLifeMapper.selectById(id);
        }

        JcxfOrganizationLife life = jcxfOrganizationLifeMapper.selectById(id);

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", life.getTeamId());
        JcxfPartyTeam team = jcxfPartyTeamMapper.selectOne(queryWrapper);
        if (team != null) {
            life.setTeamName(team.getName());
        }

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("del_flag", false);
        queryWrapper1.eq("id", life.getDeptId());
        JcxfSysDept dept = jcxfSysDeptMapper.selectOne(queryWrapper1);
        if (dept != null) {
            life.setDeptName(dept.getName());
        }

        return life;
    }

    @Override
    public Map<String, Object> addTzOrganizationLife(Map<String, Object> requestMap) {
        try {
            JcxfOrganizationLife tzOrganizationLife = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfOrganizationLife.class);

            String userId = (String) requestMap.get("userId");

            if (tzOrganizationLife.getId() != null) {
                JcxfOrganizationLife oldLife = jcxfOrganizationLifeMapper.selectById(tzOrganizationLife.getId());
                if (oldLife.getMeetingStatus() > 1) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"会议已开始，不能编辑会议");
                }

                tzOrganizationLife.setUpdateDate(new Date());
                boolean b = updateById(tzOrganizationLife);
                if (b) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
                }
            }

            tzOrganizationLife.setMeetingCategory(0);
            tzOrganizationLife.setDelFlag(false);
            tzOrganizationLife.setMeetingStatus(Constant.MEETING_1); // 会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))
            tzOrganizationLife.setCreateDate(new Date());
            tzOrganizationLife.setIsSend(0);
            tzOrganizationLife.setCreateBy(Long.valueOf(userId));

            boolean res = save(tzOrganizationLife);
            if (res) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzOrganizationLife));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    public IPage<JcxfOrganizationLife> queryTzOrganizationLifeListByPage(JcxfOrganizationLife tzOrganizationLife, SearchVo searchVo, PageVo pageVo) {
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
        Page<JcxfOrganizationLife> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfOrganizationLife> queryWrapper = new QueryWrapper<>();
        if (tzOrganizationLife != null) {
            queryWrapper = LikeAllField(tzOrganizationLife, searchVo);
        }
        queryWrapper.orderByDesc("create_date");
        IPage<JcxfOrganizationLife> result = jcxfOrganizationLifeMapper.selectPage(pageData, queryWrapper);
        return result;
    }

    @Override
    public Map<String, Object> sendMessageByPartyIds(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String tenantId = (String) requestMap.get("bizTenantId");

            JcxfOrganizationLife tzOrganizationLife = jcxfOrganizationLifeMapper.selectById(id);
            if (StringUtils.isBlank(tzOrganizationLife.getPlanMeetingPartyIds())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无拟参会人员");
            }

            String[] planMeetingPartyIds = StringUtils.isNotBlank(tzOrganizationLife.getPlanMeetingPartyIds())
                    ?tzOrganizationLife.getPlanMeetingPartyIds().split(","): new String[0];
            if (planMeetingPartyIds.length == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到拟参会人员");
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("id", planMeetingPartyIds);
            List<JcxfPartyMember> partyMembers = jcxfPartyMemberMapper.selectList(queryWrapper);
            if (partyMembers == null || partyMembers.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到拟参会人员");
            }

            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.eq("tz_message_auto.type", "3");//3代表三会一课的短信模板，已经写入字典
            TzMessageAuto tzMessageAuto = messageAutoMapper.selectOne(autoQueryWrapper);
            if (tzMessageAuto.getIsOpen() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该业务已停用");
            }

            // 获取本部门信息
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzOrganizationLife.getDeptId());
            List<String> roleDeptIds = new ArrayList<>(Arrays.asList(dept.getParentIds().split(",")));
            roleDeptIds.add(String.valueOf(dept.getId()));

            // 查询有此权限的部门
            QueryWrapper qRole = new QueryWrapper();
            qRole.in("dept_id", roleDeptIds);
            qRole.eq("auto_id", tzMessageAuto.getId());
            qRole.eq("status", "1"); // 用户任务开启
            int count = tzMessageAutoRoleMapper.selectCount(qRole);
            if (count == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"暂无该业务权限或业务已关闭");
            }

            String sendObject = "";
            List<JcxfPartyMember> validPhoneNumbers = new ArrayList<>();
            List<String> unValidPhoneNumbers = new ArrayList<>();
            String phoneRegex = "^(\\+\\d{1,3})?1[3456789]\\d{9}$";
            for (JcxfPartyMember partyMember : partyMembers) {
                String phone = partyMember.getPhone();
                sendObject = sendObject + phone + ",";
                if (phone.matches(phoneRegex)) {
                    validPhoneNumbers.add(partyMember);
                } else {
                    unValidPhoneNumbers.add(phone);
                }
            }

            if (unValidPhoneNumbers.size() > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在无效号码，请检查拟参会人员电话");
            }

            TzMessage tzMessage = new TzMessage();
            tzMessage.setSign(tzMessageAuto.getSign());
            tzMessage.setContent(tzMessageAuto.getTemContent());
            tzMessage.setMissionName("三会一课短信通知：" + tzOrganizationLife.getId());
            if (sendObject.length() > 0) {
                sendObject = sendObject.substring(0, sendObject.length() - 1);
                tzMessage.setSendObject(sendObject);
            }
            tzMessage.setSendType(1);
            tzMessage.setSendTimeType(1);
            tzMessage.setSendTime(new Date());
            tzMessage.setCreateTime(new Date());
            tzMessage.setSendMod(1);

            if (tzMessage.getSendTimeType() == 1) {
                tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
            } else {
                tzMessage.setStatus("3");
            }

            //保存发送的短信记录数据
            int count2 = tzMessageMapper.insert(tzMessage);
            if (count2 == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"发送短信错误");
            }

            int successCount = 0;
            int errorCount = 0;

            List<TzMessageDetail> detailList = new ArrayList<>();
            for (JcxfPartyMember validPhoneNumber : validPhoneNumbers) {

                String temContent = tzMessage.getContent();
                // 如果该部门有自己的设置
                QueryWrapper qk = new QueryWrapper();
                qk.eq("dept_id", validPhoneNumber.getDeptId());
                qk.eq("auto_id", tzMessageAuto.getId());
                TzMessageAutoContent tzMessageAutoContent = tzMessageAutoContentMapper.selectOne(qk);
                if (tzMessageAutoContent != null) {
                    temContent = tzMessageAutoContent.getTemContent();
                }

                ShortMessageResult shortMessageResult = null;
                if (tzMessage.getSendTimeType() == 1) {
                    shortMessageResult = ShortMessageUtil.sendMessageBySign(validPhoneNumber.getPhone(), temContent, tzMessage.getSign());
                } else {
                    shortMessageResult = ShortMessageUtil.sendMessageBySignByTime(validPhoneNumber.getPhone(), temContent, tzMessage.getSign(), tzMessage.getSendTime());
                }

                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    successCount++;
                    status = "1";
                } else {
                    errorCount++;
                    status = "2";
                }
                TzMessageDetail tzMessageDetail = new TzMessageDetail();
                //自定义发送不要模板id
                //tzMessageDetail.setTemplateId(tzMessage.getId().toString());
                tzMessageDetail.setMissionName(tzMessage.getMissionName());
                if (shortMessageResult != null) {
                    tzMessageDetail.setMsgId(shortMessageResult.getMsgId());
                    tzMessageDetail.setFeeCount(shortMessageResult.getContNum());
                }
                tzMessageDetail.setPhone(validPhoneNumber.getPhone());
                tzMessageDetail.setSendStatus(status);
                tzMessageDetail.setSendContent(tzMessage.getSign() + temContent);
                tzMessageDetail.setPostTime(tzMessage.getSendTime());
                tzMessageDetail.setTenantId(tenantId);
                tzMessageDetail.setDeptId(validPhoneNumber.getDeptId());
                tzMessageDetail.setSendType("4");
                tzMessageDetail.setTemplateId(String.valueOf(tzMessageAuto.getId()));

                if (shortMessageResult != null) {
                    tzMessageDetail.setResultCode(shortMessageResult.getCode());
                    tzMessageDetail.setResultMsg(shortMessageResult.getMsg());
                }

                detailList.add(tzMessageDetail);
            }
            //设置短信发送状态并更新数据库
            if (errorCount > 0) {
                tzOrganizationLife.setIsSend(0);
            } else {
                tzOrganizationLife.setIsSend(1);
            }

            // 记录上次发送时间
            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", tzOrganizationLife.getId());
            up.set("last_send_time", new Date());
            jcxfOrganizationLifeMapper.update(null, up);

            tzMessage.setSuccessCount(successCount);
            tzMessage.setErrorCount(errorCount);
            tzMessage.setTenantId(tenantId);

            tzMessageMapper.updateById(tzMessage);
            if (count2 > 0) {
                //保存短信详情
                for (TzMessageDetail detail: detailList) {
                    detail.setMessageId(tzMessage.getId());
                    tzMessageDetailMapper.insert(detail);
                }
            }

            Map<String, Integer> map = new HashMap<>();
            map.put("successCount", successCount);
            map.put("errorCount", errorCount);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> rankByYear(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            if (StringUtils.isEmpty(deptId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空，请联系管理员");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            QueryWrapper<JcxfSysDept> queryWrapper1 = new QueryWrapper<JcxfSysDept>();
            queryWrapper1.and(i -> i.eq("del_flag", 0));
            queryWrapper1.and(i -> i.eq(("id"), deptId)
                    .or(i2 -> i2.like("parent_ids", "," + deptId + ",")));
            List<JcxfSysDept> deptList = jcxfSysDeptService.list(queryWrapper1);

            int allNum = 0;

            List<rankByYearDto> listDtos = new ArrayList<>();
            for (JcxfSysDept tzSysDept : deptList) {
                QueryWrapper q = new QueryWrapper();
                q.eq("del_flag", 0);
                q.eq("dept_id", tzSysDept.getId());
                q.ge("start_time", year + "-01-01 00:00:00");
                int count = jcxfOrganizationLifeMapper.selectCount(q);
                rankByYearDto listDto = new rankByYearDto();
                listDto.setDeptName(tzSysDept.getName());
                listDto.setCount(count);
                listDtos.add(listDto);

                allNum += count;
            }
            Comparator<rankByYearDto> comparator = new Comparator<rankByYearDto>() {
                @Override
                public int compare(rankByYearDto dto1, rankByYearDto dto2) {
                    return dto2.getCount().compareTo(dto1.getCount());
                }
            };
            if (listDtos == null || listDtos.size() == 0) {
                ServerResponseEntity.success(listDtos);
            }
            Collections.sort(listDtos, comparator);
            Integer index = 1;
            for (rankByYearDto listDto : listDtos) {
                listDto.setIndex(index);
                index++;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("allNum", allNum);
            map.put("list", listDtos);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

            //return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(listDtos));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }


    @Override
    public Map<String, Object> getOrganizationLifeRanking(Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");

            List<Integer> qxDeptIds = new ArrayList<>();
            qxDeptIds.add(3615); // 中共泸州市龙马潭区委员会
            qxDeptIds.add(2688); // 中共泸州市江阳区委员会
            qxDeptIds.add(199); // 中共泸州市古蔺县委员会
            qxDeptIds.add(6002); // 中共泸州市纳溪区委员会
            qxDeptIds.add(4423); // 中共泸州市泸县委员会
            qxDeptIds.add(7050); // 中共泸州市叙永县委员会
            qxDeptIds.add(1482); // 中共泸州市合江县委员会

            String startTime = "";
            String endTime = "";

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            // 三会一课每个月应该完成一次
            int shouldOver = 0;

            if ("thisMonth".equals(type)) {
                shouldOver = 1;

                int actualMinimum = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
                int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMinimum, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMaximum, 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

            } else if ("threeMonth".equals(type)) {
                shouldOver = 3;

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

                instance.add(Calendar.MONTH, -2);
                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

            } else if ("thisYear".equals(type)) {
                shouldOver = 12;

                instance.set(instance.get(Calendar.YEAR), 1, 1, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), 12, 31, 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());
            }

            JSONArray jsonArray = new JSONArray();
            List<Integer> allDeptIds = new ArrayList<>();
            allDeptIds.addAll(qxDeptIds);

            // 支部类型
            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);


            CountDownLatch latch = new CountDownLatch(allDeptIds.size());
            ExecutorService executorService = Executors.newFixedThreadPool(7);

            // 记录该部门下的完成数量
            for (int i = 0; i < allDeptIds.size(); i++) {
                RunnableLifeCountToRankQxAndOther p = new RunnableLifeCountToRankQxAndOther(allDeptIds.get(i), typeList, startTime, endTime, shouldOver, true, latch, jsonArray);
                executorService.submit(p);
                //jsonArray.add(future.get());
            }

            //写入部门数据
            executorService.shutdown();//for循环结束后停止ExecutorService
            latch.await();

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jsonArray));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    class RunnableLifeCountToRankQxAndOther implements Runnable {
        private Integer id;
        private List<Integer> typeList;
        private String startTime;

        private String endTime;

        private Integer shouldOver;

        private Boolean isQx;

        private CountDownLatch latch;

        private JSONArray jsonArray;

        RunnableLifeCountToRankQxAndOther(Integer id, List<Integer> typeList, String startTime, String endTime, Integer shouldOver, Boolean isQx, CountDownLatch latch, JSONArray jsonArray) {
            this.id = id;
            this.typeList = typeList;
            this.startTime = startTime;
            this.endTime = endTime;
            this.shouldOver = shouldOver;
            this.isQx = isQx;
            this.latch = latch;
            this.jsonArray = jsonArray;
        }

        @Override
        public void run() {
            try {
                JSONObject json = new JSONObject();
                // 获取当前部门所有非退休支部
                /*QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.select("id");
                queryWrapper.eq("del_flag", false);
                queryWrapper.in("type", typeList);
                queryWrapper.ne("veteran", "1");
                queryWrapper.likeRight("parent_ids", "0,1,"+allDeptIds.get(i)+",");
                List<JcxfSysDept> childDeptList = jcxfSysDeptMapper.selectList(queryWrapper);*/

                List<Integer> childDeptIds = jcxfSysDeptMapper.getChildrenIdsByVeteran(id, "0", typeList);
                /*for (JcxfSysDept dept: childDeptList) {
                    childDeptIds.add(dept.getId());
                }*/
                // 当前组织的支部数量
                json.put("zbcount", childDeptIds.size());


                if (childDeptIds.size() > 0) {
                    Integer count = jcxfOrganizationLifeMapper.getLifeCountToRankQxAndOther(startTime, endTime, childDeptIds, shouldOver);
                    json.put("overCount", count);
                } else {
                    json.put("overCount", 0);
                }

                json.put("id", id);

                // 当前部门信息
                JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(id);
                json.put("name", tzSysDept.getName());
                // 是否区县
                json.put("isQx", isQx);

                jsonArray.add(json);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> getOrganizationLifeRankingOther(Map<String, Object> requestMap) {
        try {
            String type = (String) requestMap.get("type");

            List<Integer> qxDeptIds = new ArrayList<>();
            qxDeptIds.add(3615); // 中共泸州市龙马潭区委员会
            qxDeptIds.add(2688); // 中共泸州市江阳区委员会
            qxDeptIds.add(199); // 中共泸州市古蔺县委员会
            qxDeptIds.add(6002); // 中共泸州市纳溪区委员会
            qxDeptIds.add(4423); // 中共泸州市泸县委员会
            qxDeptIds.add(7050); // 中共泸州市叙永县委员会
            qxDeptIds.add(1482); // 中共泸州市合江县委员会
            qxDeptIds.add(21116);  // 酒城先锋运维党委

            QueryWrapper q1 = new QueryWrapper();
            q1.select("id, name");
            q1.notIn("id", qxDeptIds);
            q1.eq("del_flag", 0);
            q1.eq("parent_id", 1);
            List<JcxfSysDept> tzSysDeptList = jcxfSysDeptMapper.selectList(q1);
            List<Integer> otherDeptIds = new ArrayList<>();
            for (JcxfSysDept dept : tzSysDeptList) {
                otherDeptIds.add(dept.getId());
            }

            String startTime = "";
            String endTime = "";

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            // 三会一课每个月应该完成一次
            int shouldOver = 0;

            if ("thisMonth".equals(type)) {
                shouldOver = 1;

                int actualMinimum = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
                int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMinimum, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMaximum, 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

            } else if ("threeMonth".equals(type)) {
                shouldOver = 3;

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

                instance.add(Calendar.MONTH, -2);
                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

            } else if ("thisYear".equals(type)) {
                shouldOver = 12;

                instance.set(instance.get(Calendar.YEAR), 1, 1, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), 12, 31, 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());
            }

            JSONArray jsonArray = new JSONArray();
            List<Integer> allDeptIds = new ArrayList<>();
            allDeptIds.addAll(otherDeptIds);

            // 支部类型
            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);


            CountDownLatch latch = new CountDownLatch(allDeptIds.size());
            ExecutorService executorService = Executors.newFixedThreadPool(7);

            // 记录该部门下的完成数量
            for (int i = 0; i < allDeptIds.size(); i++) {
                RunnableLifeCountToRankQxAndOther p = new RunnableLifeCountToRankQxAndOther(allDeptIds.get(i), typeList, startTime, endTime, shouldOver, false, latch, jsonArray);
                executorService.submit(p);
                //jsonArray.add(future.get());
            }

            //写入部门数据
            executorService.shutdown();//for循环结束后停止ExecutorService
            latch.await();

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jsonArray));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getLifeCountToRankOfMeetingType(Map<String, Object> requestMap) {
        try {
            String meetingType = (String) requestMap.get("meetingType");

            List<Integer> qxDeptIds = new ArrayList<>();
            qxDeptIds.add(3615); // 中共泸州市龙马潭区委员会
            qxDeptIds.add(2688); // 中共泸州市江阳区委员会
            qxDeptIds.add(199); // 中共泸州市古蔺县委员会
            qxDeptIds.add(6002); // 中共泸州市纳溪区委员会
            qxDeptIds.add(4423); // 中共泸州市泸县委员会
            qxDeptIds.add(7050); // 中共泸州市叙永县委员会
            qxDeptIds.add(1482); // 中共泸州市合江县委员会

            QueryWrapper q1 = new QueryWrapper();
            q1.select("id, name");
            q1.notIn("id", qxDeptIds);
            q1.eq("del_flag", 0);
            q1.eq("parent_id", 1);
            List<JcxfSysDept> tzSysDeptList = jcxfSysDeptMapper.selectList(q1);
            List<Integer> otherDeptIds = new ArrayList<>();
            for (JcxfSysDept dept : tzSysDeptList) {
                otherDeptIds.add(dept.getId());
            }

            String startTime = "";
            String endTime = "";

            // 组织生活会每年组织一次
            int shouldType1 = 1;

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            instance.set(instance.get(Calendar.YEAR), 1, 1, 00, 00, 00);
            startTime = simpleDateFormat.format(instance.getTime());

            instance.set(instance.get(Calendar.YEAR), 12, 31, 23, 59, 59);
            endTime = simpleDateFormat.format(instance.getTime());

            JSONArray jsonArray = new JSONArray();
            List<Integer> allDeptIds = new ArrayList<>();
            allDeptIds.addAll(qxDeptIds);
            allDeptIds.addAll(otherDeptIds);

            // 获取所有有子部门的部门id
            QueryWrapper qn = new QueryWrapper();
            qn.select("DISTINCT parent_ids");
            qn.eq("del_flag", 0);
            List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(qn);
            Set<String> parentIds = new HashSet<>();
            for (JcxfSysDept tzSysDept : deptList) {
                parentIds.addAll(Arrays.asList(tzSysDept.getParentIds().split(",")));
            }
            Set<String> removeIds = new HashSet<>();
            removeIds.add("");
            parentIds.removeAll(removeIds);
            List<Integer> listInteger = parentIds.stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());


            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);

            // 记录该部门下的完成数量
            for (int i = 0; i < allDeptIds.size(); i++) {
                int count = 0;

                JSONObject json = new JSONObject();
                // 获取当前部门所有非退休部门下级
                List<Integer> childDeptIds = jcxfSysDeptMapper.getChildrenIdsByVeteran(allDeptIds.get(i), "0", typeList);
                // 当前组织的支部数量
                json.put("zbcount", childDeptIds.size());
                if (childDeptIds.size() > 0) {
                    // 得到最后一级部门的id集合
                    if (listInteger.size() > 0) {
                        childDeptIds.removeAll(listInteger);
                    }

                    List<Map<String, BigDecimal>> mapList = jcxfOrganizationLifeMapper.getLifeCountToRankOfMeetingType(startTime, endTime, meetingType, childDeptIds);
                    for (Map<String, BigDecimal> map : mapList) {
                        Integer num1 = Integer.parseInt(String.valueOf(map.get("num")));

                        if (num1 >= shouldType1) {
                            count++;
                        }
                    }
                }

                json.put("id", allDeptIds.get(i));

                // 当前部门信息
                JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(allDeptIds.get(i));
                json.put("name", tzSysDept.getName());
                // 是否区县
                if (qxDeptIds.indexOf(allDeptIds.get(i)) > -1) {
                    json.put("isQx", true);
                } else {
                    json.put("isQx", false);
                }

                json.put("overCount", count);
                json.put("leafCount", childDeptIds.size());
                jsonArray.add(json);
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jsonArray));
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

            Integer shouldCount = 0;

            if ("thisMonth".equals(type)) {
                shouldCount = 1;

                int actualMinimum = instance.getActualMinimum(Calendar.DAY_OF_MONTH);
                int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMinimum, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), actualMaximum, 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

            } else if ("threeMonth".equals(type)) {
                shouldCount = 2;

                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
                endTime = simpleDateFormat.format(instance.getTime());

                instance.add(Calendar.MONTH, -2);
                instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH), 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

            } else if ("thisYear".equals(type)) {
                shouldCount = 3;

                instance.set(instance.get(Calendar.YEAR), 1, 1, 00, 00, 00);
                startTime = simpleDateFormat.format(instance.getTime());

                instance.set(instance.get(Calendar.YEAR), 12, 31, 23, 59, 59);
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
            queryWrapper.like("parent_ids", "," + deptId + ",");
            queryWrapper.in("type", typeList);
            queryWrapper.eq("veteran", "0"); // 开展率不展示和计算退休支部


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
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("index", i + 1);
                jsonObject.put("id", tzSysDept.getId());
                jsonObject.put("name", tzSysDept.getName());

                List<Integer> list = new ArrayList<>();
                list.add(tzSysDept.getId());
                Integer count = jcxfOrganizationLifeMapper.getLifeCountToRankQxAndOther(startTime, endTime, list, shouldCount);

                if (count > 0) {
                    jsonObject.put("count", "是");
                } else {
                    jsonObject.put("count", "-");
                }

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

    @Override
    public Map<String, Object> getMeetingDecadeCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String veteran = (String) requestMap.get("veteran");
            String organizationType = (String) requestMap.get("organizationType");
            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsByIdAndOther(Long.valueOf(deptId),veteran,organizationType);
            ids.add(Long.valueOf(deptId));

            // 时间范围
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            String endTime = format.format(calendar.getTime());

            calendar.set(calendar.get(Calendar.YEAR) - 10, 0, 1);
            String startTime = format.format(calendar.getTime());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("DATE_FORMAT(start_time, '%Y-%m') as time," +
                    "count(id) as num");
            queryWrapper.eq("meeting_type", 1); //三会一课
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
    public Map<String, Object> getMeetingDecadeCountGroupByYear(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String veteran = (String) requestMap.get("veteran");
            String organizationType = (String) requestMap.get("organizationType");
            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsByIdAndOther(Long.valueOf(deptId),veteran,organizationType);
            ids.add(Long.valueOf(deptId));

            // 时间范围
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            String endTime = format.format(calendar.getTime());

            calendar.set(calendar.get(Calendar.YEAR) - 10, 0, 1);
            String startTime = format.format(calendar.getTime());

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("DATE_FORMAT(start_time, '%Y') as time," +
                    "count(id) as num");
            queryWrapper.eq("meeting_type", 6); //组织生活会
            queryWrapper.ge("DATE_FORMAT(start_time, '%Y')", startTime);
            queryWrapper.le("DATE_FORMAT(start_time, '%Y')", endTime);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", ids);
            queryWrapper.groupBy("DATE_FORMAT(start_time, '%Y')");

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
    public Map<String, Object> getMeetingCountByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String meetingType = (String) requestMap.get("meetingType");
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            deptIds.add(Long.valueOf(deptId));
            JcxfOrganizationLife jcxfOrganizationLife = jcxfOrganizationLifeMapper.getMeetingCountByDeptId(deptIds, meetingType);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfOrganizationLife));
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzOrganizationLife 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<JcxfOrganizationLife> LikeAllField(JcxfOrganizationLife tzOrganizationLife, SearchVo searchVo) {
        // 时间范围
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        String endTime = format.format(calendar.getTime());

        calendar.set(calendar.get(Calendar.YEAR) - 10, 0, 1);
        String startTime = format.format(calendar.getTime());

        QueryWrapper<JcxfOrganizationLife> queryWrapper = new QueryWrapper<>();
        if (tzOrganizationLife.getId() != null) {
            queryWrapper.and(i -> i.like("id", tzOrganizationLife.getId()));
        }
        if (tzOrganizationLife.getDeptId() != null) {
            queryWrapper.and(i -> i.eq("dept_id", tzOrganizationLife.getDeptId()));
        }
        if (StringUtils.isNotBlank(tzOrganizationLife.getMeetingTopic())) {
            queryWrapper.and(i -> i.like("meeting_topic", tzOrganizationLife.getMeetingTopic()));
        }
        if (tzOrganizationLife.getMeetingType() != null) {
            queryWrapper.and(i -> i.eq("meeting_type", tzOrganizationLife.getMeetingType()));
        }else {
            queryWrapper.in("meeting_type", Arrays.asList(1, 6));
        }
        if (tzOrganizationLife.getStartTime() != null) {
            queryWrapper.and(i -> i.ge("start_time", tzOrganizationLife.getStartTime()));
        }else {
            queryWrapper.ge("DATE_FORMAT(start_time, '%Y')", startTime);
        }
        if (tzOrganizationLife.getEndTime() != null) {
            queryWrapper.and(i -> i.le("end_time", tzOrganizationLife.getEndTime()));
        }else {
            queryWrapper.le("DATE_FORMAT(start_time, '%Y')", endTime);
        }
        if (tzOrganizationLife.getMeetingStatus() != null) {
            queryWrapper.and(i -> i.eq("meeting_status", tzOrganizationLife.getMeetingStatus()));
        }
        if (tzOrganizationLife.getCreateBy() != null) {
            queryWrapper.and(i -> i.like("create_by", tzOrganizationLife.getCreateBy()));
        }
        if (tzOrganizationLife.getUpdateBy() != null) {
            queryWrapper.and(i -> i.like("update_by", tzOrganizationLife.getUpdateBy()));
        }
        if (StringUtils.isNotBlank(tzOrganizationLife.getHost())) {
            queryWrapper.and(i -> i.like("host", tzOrganizationLife.getHost()));
        }
        if (tzOrganizationLife.getCreateDate() != null) {
            queryWrapper.and(i -> i.like("create_date", tzOrganizationLife.getCreateDate()));
        }
        //if(StringUtils.isNotBlank(tzOrganizationLife.getDelFlag())){
        //	queryWrapper.and(i -> i.like("del_flag", tzOrganizationLife.getDelFlag()));
        //}
        if (searchVo != null) {
            if (StringUtils.isNotBlank(searchVo.getStartDate()) && StringUtils.isNotBlank(searchVo.getEndDate())) {
                queryWrapper.lambda().and(i -> i.between(JcxfOrganizationLife::getEndTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.lambda().and(i -> i.eq(JcxfOrganizationLife::getDelFlag, 0));
        return queryWrapper;

    }


}

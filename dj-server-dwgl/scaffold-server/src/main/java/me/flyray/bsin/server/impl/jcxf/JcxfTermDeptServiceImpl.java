package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.flyray.bsin.facade.service.JcxfPartyMemberService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDictionary;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDictionaryMapper;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfTermDeptService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.SearchVo;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.jcxf.JcxfBranchReelection;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfTermDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.jcxf.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JcxfTermDeptServiceImpl implements JcxfTermDeptService {

    @Autowired
    private JcxfTermDeptMapper jcxfTermDeptMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberServiceImpl jcxfPartyMemberService;

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Override
    public Map<String, Object> deleteTzTermDept(Map<String, Object> requestMap) {
        /*String str = (String) requestMap.get("ids");
        String[] ids = str.split(",");*/

        List<String> ids = (List<String>) requestMap.get("ids");

        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
        }
        try {
            UpdateWrapper<JcxfBranchReelection> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("branch_reelection.del_flag", 1).in("branch_reelection.id", ids);
            int res = jcxfTermDeptMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除异常");
        }
    }

    @Override
    public Map<String, Object> queryTzTermDeptList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");

        JcxfBranchReelection tzTermDept = JSON.parseObject(JSON.toJSONString(dataMap.get("tzTermDept")), JcxfBranchReelection.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
        String type = (String) dataMap.get("type");
        try {
            Map<String, Object> map = new HashMap<>();
            // 等开通权限后，通过权限来区分该用户的查询范围，1为查询自己的换届历史信息，2为查询所有用户当前换届信息
            List<Long> listAll = new ArrayList<>();
            tzTermDept.setDelFlag(0);

            IPage<JcxfBranchReelection> result = null;

            if ("1".equals(type) && tzTermDept.getDeptId() != null) {
                result = queryTzTermDeptListByPage(tzTermDept, searchVo, pageVo, listAll, type);
            } else if ("2".equals(type)) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(tzTermDept.getDeptId());
                listAll.add(tzTermDept.getDeptId());

                result = queryTzTermDeptListByPage(tzTermDept, searchVo, pageVo, listAll, type);
                List<JcxfBranchReelection> records = result.getRecords();
                for (JcxfBranchReelection record : records) {
                    QueryWrapper<JcxfSysDept> wrapper = new QueryWrapper<>();
                    wrapper.eq("sys_dept.del_flag", 0);
                    wrapper.eq("sys_dept.id", record.getDeptId());
                    JcxfSysDept one = jcxfSysDeptMapper.selectOne(wrapper);
                    if (one != null) {
                        record.setDeptName(one.getName());
                    }
                }
            }

            map.put("result", result);
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            for (JcxfBranchReelection dept : result.getRecords()) {
                long thisChangeTime = dept.getThisSessionStartTime().getTime();
                long thisFinishTime = fmt.parse(dept.getThisSessionFinishTime()).getTime();
                long now = new Date().getTime();
                if (now < thisFinishTime && thisChangeTime < now) {
                    dept.setIsChange("是");
                } else {
                    dept.setIsChange("否");
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }


    @Override
    public Map<String, Object> queryTzTermDeptByDeptPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");

            JcxfBranchReelection tzTermDept = JSON.parseObject(JSON.toJSONString(dataMap.get("tzTermDept")), JcxfBranchReelection.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

            String teamSession = (String) dataMap.get("teamSession");
            String accomplishBranchReelection = (String) dataMap.get("accomplishBranchReelection");
            String monthNum = (String) dataMap.get("monthNum");


            List<Long> listAll = new ArrayList<>();
            listAll.add(tzTermDept.getDeptId());
            listAll.addAll(jcxfSysDeptMapper.selectChildrenIdsById(tzTermDept.getDeptId()));

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
            List<Long> deptWhereIdList = new ArrayList<>();

            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(teamSession)) { //换届届次
                QueryWrapper q1 = new QueryWrapper();
                q1.select("DISTINCT dept_id");
                q1.eq("del_flag", 0);
                q1.in("dept_id", listAll);
                q1.eq("team_session", teamSession);
                List<JcxfBranchReelection> list = jcxfTermDeptMapper.selectList(q1);
                List<Long> collect = list.stream().map(item -> item.getDeptId()).collect(Collectors.toList());
                deptWhereIdList.addAll(collect);
            }

            QueryWrapper<JcxfBranchReelection> branchReelectionQueryWrapper = new QueryWrapper<>();
            branchReelectionQueryWrapper.eq("del_flag", 0);
            branchReelectionQueryWrapper.in("dept_id", listAll);
            branchReelectionQueryWrapper.select("DISTINCT(dept_id)");
            List<JcxfBranchReelection> selectList = jcxfTermDeptMapper.selectList(branchReelectionQueryWrapper);
            List<Long> yhjDept = selectList.stream().map(item -> item.getDeptId()).collect(Collectors.toList());


            if (StringUtils.isNotBlank(accomplishBranchReelection)) {
                if(Objects.equals("1", accomplishBranchReelection)) {
                    //已换届
                    deptWhereIdList.addAll(yhjDept);
                }
                if(Objects.equals("2", accomplishBranchReelection)) {
                    //未换届
                    listAll.removeAll(yhjDept);
                    deptWhereIdList.addAll(listAll);
                }
                if(Objects.equals("3", accomplishBranchReelection)) {
                    //超过换届时间
                    List<Long> longs = jcxfTermDeptMapper.selectExceedFinishTimeDeptIdList(listAll, DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
                    deptWhereIdList.addAll(longs);
                }
            }

            if (StringUtils.isNotBlank(monthNum)) { //换届剩余月数
                List<Long> longs = jcxfTermDeptMapper.selectSurplusFinishTimeDeptIdList(listAll, monthNum);
                deptWhereIdList.addAll(longs);
            }

            //如果没有条件， 默认当前部门下的所有部门
            if(StringUtils.isBlank(teamSession) && StringUtils.isBlank(accomplishBranchReelection) && StringUtils.isBlank(monthNum)) {
                deptWhereIdList.addAll(listAll);
            }

            Page<JcxfSysDept> pageData = new Page<>(page, limit);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in(deptWhereIdList.size() > 0 ,"id", deptWhereIdList);
            queryWrapper.eq(deptWhereIdList.size() == 0 ,"id", -1);
            IPage<JcxfSysDept> result = jcxfSysDeptMapper.selectPage(pageData, queryWrapper);
            for (JcxfSysDept record : result.getRecords()) {
                Long aLong = yhjDept.stream().filter(item -> Objects.equals(String.valueOf(item), String.valueOf(record.getId()))).findFirst().orElse(null);
                if(aLong != null) {
                    record.setAccomplishBranchReelection("1");
                }else {
                    record.setAccomplishBranchReelection("2");
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public synchronized Map<String, Object> addTzTermDept(Map<String, Object> requestMap) {
        JcxfBranchReelection tzTermDept = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfBranchReelection.class);

        if (tzTermDept.getTeamSession() != null) {
            QueryWrapper q1 = new QueryWrapper<>();
            q1.eq("del_flag", 0);
            q1.eq("dept_id", tzTermDept.getDeptId());

            if (tzTermDept.getId() != null) {
                q1.ne("id", tzTermDept.getId());
            }

            q1.eq("team_session", tzTermDept.getTeamSession());
            int count = jcxfTermDeptMapper.selectCount(q1);
            if (count > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该班子届次已存在");
            }
        }

        //如果id不为空则为修改
        if (tzTermDept.getId() != null) {
            tzTermDept.setUpdateDate(new Date());
            int updateById = jcxfTermDeptMapper.updateById(tzTermDept);
            if (updateById > 0) {
                //同时更新党员的职务
                updatePartyMemberPosition(tzTermDept);

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
            }
        }

        tzTermDept.setDelFlag(0);
        tzTermDept.setCreateDate(new Date());
        int res = jcxfTermDeptMapper.insert(tzTermDept);
        if (res > 0) {
            //同时更新党员的职务
            updatePartyMemberPosition(tzTermDept);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
        }
    }


    private void updatePartyMemberPosition(JcxfBranchReelection tzTermDept) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", "position");
        queryWrapper.eq("del_flag", false);
        queryWrapper.orderByAsc("sort");
        List<JcxfSysDictionary> dictionaryList = jcxfSysDictionaryMapper.selectList(queryWrapper);
        ArrayList<JcxfPartyMember> arrayList = new ArrayList<>();
        if (StringUtils.isNotBlank(tzTermDept.getSecretary())) {
            //书记
            JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
            JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "书记")).findFirst().get();
            jcxfPartyMember.setId(Long.parseLong(tzTermDept.getSecretary()));
            jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
            arrayList.add(jcxfPartyMember);
        }
        if (StringUtils.isNotBlank(tzTermDept.getDeputySecretary())) {
            //副书记
            JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
            JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "副书记")).findFirst().get();
            jcxfPartyMember.setId(Long.parseLong(tzTermDept.getDeputySecretary()));
            jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
            arrayList.add(jcxfPartyMember);
        }
        if (StringUtils.isNotBlank(tzTermDept.getCommitteeMember())) {
            //组织委员
            JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
            JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "组织委员")).findFirst().get();
            jcxfPartyMember.setId(Long.parseLong(tzTermDept.getCommitteeMember()));
            jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
            arrayList.add(jcxfPartyMember);
        }
        if (StringUtils.isNotBlank(tzTermDept.getDiCommitteeMember())) {
            //纪检委员
            JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
            JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "纪检委员")).findFirst().get();
            jcxfPartyMember.setId(Long.parseLong(tzTermDept.getDiCommitteeMember()));
            jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
            arrayList.add(jcxfPartyMember);
        }
        if (StringUtils.isNotBlank(tzTermDept.getPublicityCommitteeMember())) {
            //宣传委员
            JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
            JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "宣传委员")).findFirst().get();
            jcxfPartyMember.setId(Long.parseLong(tzTermDept.getPublicityCommitteeMember()));
            jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
            arrayList.add(jcxfPartyMember);
        }
        if (StringUtils.isNotBlank(tzTermDept.getOtherMembers())) {
            //其他委员
            String[] split = tzTermDept.getOtherMembers().split(",");
            for (String s : split) {
                JcxfPartyMember jcxfPartyMember = new JcxfPartyMember();
                JcxfSysDictionary dictionary = dictionaryList.stream().filter(item -> Objects.equals(item.getDetailName(), "其他委员")).findFirst().get();
                jcxfPartyMember.setId(Long.parseLong(s));
                jcxfPartyMember.setPosition(dictionary != null ? Integer.parseInt(dictionary.getDetail()) : null);
                arrayList.add(jcxfPartyMember);
            }
        }
        if (arrayList.size() > 0) {
            jcxfPartyMemberService.updateBatchById(arrayList);
        }
    }

    @Override
    public Map<String, Object> getTzTermDept(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
        }
        try {
            JcxfBranchReelection res = jcxfTermDeptMapper.selectById(id);
            if (res != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getTzTermStatistics(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");

            List<Long> listAll = new ArrayList<>();
            listAll.add(Long.valueOf(deptId));
            listAll.addAll(jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId))); //查询所有下级

            QueryWrapper<JcxfBranchReelection> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", listAll);
            queryWrapper.select("DISTINCT(dept_id)");
            List<JcxfBranchReelection> selectList = jcxfTermDeptMapper.selectList(queryWrapper);
            List<Long> yhjDept = selectList.stream().map(item -> item.getDeptId()).collect(Collectors.toList());

            Map<String, Object> map = new HashMap<>();
            map.put("count1", yhjDept.size()); //已换届
            map.put("count2", listAll.size() - yhjDept.size()); //未换届

            //查询超过换届时间了的
            map.put("count3", jcxfTermDeptMapper.selectExceedFinishTimeCount(listAll, DateUtils.formatDate(new Date(), "yyyy-MM-dd")));
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    public IPage<JcxfBranchReelection> queryTzTermDeptListByPage(JcxfBranchReelection tzTermDept, SearchVo searchVo, PageVo pageVo, List<Long> listAll, String type) throws ParseException {
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
        Page<JcxfBranchReelection> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfBranchReelection> queryWrapper = new QueryWrapper<>();

        if (tzTermDept != null) {
            queryWrapper = LikeAllField(tzTermDept, searchVo);
        }
        IPage<JcxfBranchReelection> result = null;
        // 1为查询自己的换届历史信息，2为查询当前及所有下级换届信息
        if ("1".equals(type)) {
            if (tzTermDept.getDeptId() != null) {
                queryWrapper.and(i -> i.eq("branch_reelection.dept_id", tzTermDept.getDeptId()));
            }
            if(tzTermDept.getTeamSession() != null) {
                queryWrapper.eq("branch_reelection.team_session", tzTermDept.getTeamSession());
            }
            result = jcxfTermDeptMapper.selectPage(pageData, queryWrapper);
        } else if ("2".equals(type)) {
            queryWrapper.and(i -> i.in("branch_reelection.dept_id", listAll));
            // 如果查询所有用户当前换届信息，则获取最新的换届班次
            result = jcxfTermDeptMapper.selectPage(pageData, queryWrapper);
            List<JcxfBranchReelection> records = result.getRecords();
            List<JcxfBranchReelection> tmp = new ArrayList<>();
            // 根据deptId进行分组
            Map<Long, List<JcxfBranchReelection>> collect = records.stream().collect(Collectors.groupingBy(JcxfBranchReelection::getDeptId));
            // 按照deptId分组后，遍历map获取每一组的班次的最大值
            for (Map.Entry<Long, List<JcxfBranchReelection>> entry : collect.entrySet()) {
                JcxfBranchReelection tzTermDept1 = entry.getValue().stream().max(Comparator.comparing(JcxfBranchReelection::getTeamSession)).get();
                tmp.add(tzTermDept1);
            }
            result.setRecords(tmp);

            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            for (JcxfBranchReelection dept : tmp) {
                long thisChangeTime = dept.getThisSessionStartTime().getTime();
                if (dept.getThisSessionFinishTime().length() < 8) {
                    dept.setThisSessionFinishTime(dept.getThisSessionFinishTime() + "-01");
                }
                long thisFinishTime = fmt.parse(dept.getThisSessionFinishTime()).getTime();
                long now = new Date().getTime();
                if (now < thisFinishTime && thisChangeTime < now) {
                    dept.setIsChange("是");
                } else {
                    dept.setIsChange("否");
                }
            }
        }
        return result;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzTermDept 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<JcxfBranchReelection> LikeAllField(JcxfBranchReelection tzTermDept, SearchVo searchVo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper<JcxfBranchReelection> queryWrapper = new QueryWrapper<>();
        if (tzTermDept.getId() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.id", tzTermDept.getId()));
        }
        if (tzTermDept.getTeamSession() != null) {
            queryWrapper.and(i -> i.eq("branch_reelection.team_session", tzTermDept.getTeamSession()));
        }
        if (tzTermDept.getIsFirst() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.is_first", tzTermDept.getIsFirst()));
        }
        if (tzTermDept.getLastSessionFinishTime() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.Last_session_finish_time", simpleDateFormat.format(tzTermDept.getLastSessionFinishTime())));
        }
        if (tzTermDept.getThisSessionStartTime() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.this_session_start_time", simpleDateFormat.format(tzTermDept.getThisSessionStartTime())));
        }
        if (tzTermDept.getThisSessionFinishTime() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.this_session_finish_time", tzTermDept.getThisSessionFinishTime()));
        }
        if (tzTermDept.getVoteType() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.elect_type", tzTermDept.getVoteType()));
        }
        /*if (tzTermDept.getRecordAddTime() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.record_add_time", tzTermDept.getRecordAddTime()));
        }*/
        /*if (tzTermDept.getLastEditTime() != null) {
            queryWrapper.and(i -> i.like("branch_reelection.last_edit_time", tzTermDept.getLastEditTime()));
        }*/
        /*if (StringUtils.isNotBlank(tzTermDept.getRecorder())) {
            queryWrapper.and(i -> i.like("branch_reelection.recorder", tzTermDept.getRecorder()));
        }*/
        /*if (StringUtils.isNotBlank(tzTermDept.getBak())) {
            queryWrapper.and(i -> i.like("branch_reelection.bak", tzTermDept.getBak()));
        }*/
        /*if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(JcxfBranchReelection::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }*/
        queryWrapper.lambda().and(i -> i.eq(JcxfBranchReelection::getDelFlag, 0));
        queryWrapper.orderByAsc("branch_reelection.create_date");
        return queryWrapper;

    }
}

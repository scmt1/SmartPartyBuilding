package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.vo.JcxfDevelopPartyMemberVo;
import me.flyray.bsin.server.utils.AESUtil2;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfDevelopPartyService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfDevelopParty;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.jcxf.JcxfDevelopPartyMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.jcxf.Cn2SpellUtil;
import me.flyray.bsin.server.utils.jcxf.EncryptUtils;
import me.flyray.bsin.server.utils.PasswordManager;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


public class JcxfDevelopPartyServiceImpl implements JcxfDevelopPartyService {

    @Autowired
    private JcxfDevelopPartyMapper jcxfDevelopPartyMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private PasswordManager passwordManager;

    @Override
    public Map<String, Object> queryDevelopPartyList(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        JcxfDevelopParty developParty = JSON.parseObject(JSON.toJSONString(map.get("developParty")), JcxfDevelopParty.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

        try {
            List<Long> listAll = new ArrayList<>();
            developParty.setDelFlag(0);
            //partyMember.setFlowPlace("not null");
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (developParty.getDeptId() != null) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(developParty.getDeptId());
                listAll.add(developParty.getDeptId());
            }
            //这一步是统计发展党员总数，提交入党申请书人数等等

            IPage<JcxfDevelopParty> result = queryDevelopPartyList(developParty, searchVo, pageVo, listAll);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getDevelopPartyListByPartyMemberId(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        try {
            QueryWrapper<JcxfDevelopParty> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member_id",partyMemberId);
            queryWrapper.eq("del_flag",0);
            queryWrapper.orderByDesc("create_date");
            List<JcxfDevelopParty> result = jcxfDevelopPartyMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getBaseCount(Map<String, Object> requestMap) {
        try {
            Map<String, Object> mapDate = (Map<String, Object>) requestMap.get("data");
            JcxfDevelopParty developParty = JSON.parseObject(JSON.toJSONString(mapDate.get("developParty")), JcxfDevelopParty.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(mapDate.get("searchVo")), SearchVo.class);

            List<Long> listAll = jcxfSysDeptMapper.selectChildrenIdsById(developParty.getDeptId());
            listAll.add(Long.valueOf(developParty.getDeptId()));

            QueryWrapper<JcxfDevelopParty> queryWrapper = LikeAllField(developParty, searchVo);

            queryWrapper.select("person_type as type, count(*) as num");
            queryWrapper.and(i -> i.eq("del_flag", 0));
            queryWrapper.groupBy("person_type");
            if (listAll != null && listAll.size() > 0) {
                queryWrapper.in("dept_id", listAll);
            }
            List<Map<String, Object>> list = jcxfDevelopPartyMapper.selectMaps(queryWrapper);

            Long submitTotal = 0l;
            Long activeTotal = 0l;
            Long developTotal = 0l;
            Long preTotal = 0l;
            Long officialTotal = 0l;

            for (Map<String, Object> map: list) {
                if ((int) map.get("type") == 1) {
                    submitTotal = (Long) map.get("num");
                } else if ((int) map.get("type") == 2) {
                    activeTotal = (Long) map.get("num");
                } else if ((int) map.get("type") == 3) {
                    developTotal = (Long) map.get("num");
                } else if ((int) map.get("type") == 4) {
                    preTotal = (Long) map.get("num");
                } else if ((int) map.get("type") == 5) {
                    officialTotal = (Long) map.get("num");
                }
            }

            Map<String, Long> result = new HashMap<>();
            result.put("submitTotal", submitTotal);
            result.put("activeTotal", activeTotal);
            result.put("developTotal", developTotal);
            result.put("preTotal", preTotal);
            result.put("officialTotal", officialTotal);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }



    @Override
    public Map<String, Object> deleteDevelopParty(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");
        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
        }
        try {
            UpdateWrapper<JcxfDevelopParty> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("del_flag", 1).set("update_date", new Date()).in("id", ids);
            int update = jcxfDevelopPartyMapper.update(null, updateWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> addDevelopPartyMember(Map<String, Object> requestMap) {
        JcxfDevelopParty developParty = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfDevelopParty.class);
        String tenantId = (String) requestMap.get("bizTenantId");
        // 验证电话号码和身份证号是否存在重复
        QueryWrapper<JcxfPartyMember> q1 = new QueryWrapper();
        q1.eq("del_flag", 0);
        if(StringUtils.isNotBlank(developParty.getIdcard()) && !developParty.getIdcard().contains("*")
                && StringUtils.isNotBlank(developParty.getPhone()) && !developParty.getPhone().contains("*")){
            q1.and(i -> i.eq("idcard", AESUtil2.encrypt(developParty.getIdcard()))
                    .or()
                    .eq("phone", AESUtil2.encrypt(developParty.getPhone())));
        }
        if (developParty.getPartyMemberId() != null) {
            q1.ne("id", developParty.getPartyMemberId());
        }
        int count1 = jcxfPartyMemberMapper.selectCount(q1);
        if (count1 > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在身份证或电话相同的党员");
        }

        QueryWrapper<JcxfDevelopParty> q2 = new QueryWrapper();
        q2.eq("del_flag", 0);
        if(StringUtils.isNotBlank(developParty.getIdcard()) && !developParty.getIdcard().contains("*")
                && StringUtils.isNotBlank(developParty.getPhone()) && !developParty.getPhone().contains("*")){
            q2.and(i -> i.eq("idcard", AESUtil2.encrypt(developParty.getIdcard()))
                    .or()
                    .eq("phone", AESUtil2.encrypt(developParty.getPhone())));
        }
        if (developParty.getId() != null) {
            q2.ne("id", developParty.getId());
        }
        int count2 = jcxfDevelopPartyMapper.selectCount(q2);
        if (count2 > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在身份证或电话相同的信息");
        }

        Integer developState = developParty.getPersonType();

        if (developState.intValue() == 2 && developParty.getSubmitDate() == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 3 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 4 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 5 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null || developParty.getPartyTime() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        }
        if (developState.intValue() == 2 || developState.intValue() == 3) {
            if ("1".equals(developParty.getIsTrain())) {
                developParty.setIsReturnWorkers(null);
            }
        }
        developParty.setDelFlag(0);
        if ((developState.intValue() == 4 || developState.intValue() == 5) && developParty.getPartyMemberId() == null) {
            JcxfPartyMember partyMember = new JcxfPartyMember();
            //同步数据到党员表
            JcxfPartyMember sync = sync(developParty, partyMember);
            sync.setTenantId(tenantId);
            int save = jcxfPartyMemberMapper.insert(sync);
            if (save < 1) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作失败");
            } else {
                developParty.setPartyMemberId(sync.getId());
            }
        }
        if (developParty.getId() != null) {
            developParty.setUpdateDate(new Date());
            if(developParty.getIdcard().contains("*")){
                developParty.setIdcard(null);
            }
            if(developParty.getPhone().contains("*")){
                developParty.setPhone(null);
            }
            int res = jcxfDevelopPartyMapper.updateById(developParty);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
            }
        } else {
            developParty.setCreateDate(new Date());
            int res = jcxfDevelopPartyMapper.insert(developParty);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
            }
        }
    }

    @Override
    public Map<String, Object> updateDevelopPartyMember(Map<String, Object> requestMap) {
        JcxfDevelopParty developParty = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfDevelopParty.class);
        String tenantId = (String) requestMap.get("bizTenantId");
        JcxfDevelopPartyMemberVo jcxfDevelopParty = null;
        if(developParty.getId() != null){
            jcxfDevelopParty = jcxfDevelopPartyMapper.getJcxfDevelopPartyById(developParty.getId());
        }
        // 验证电话号码和身份证号是否存在重复
        QueryWrapper<JcxfPartyMember> q1 = new QueryWrapper();
        q1.eq("del_flag", 0);
        if(StringUtils.isNotBlank(developParty.getIdcard()) && !developParty.getIdcard().contains("*")){
            q1.and(i -> i.eq("idcard", AESUtil2.encrypt(developParty.getIdcard())));
        }else{
            JcxfDevelopPartyMemberVo finalJcxfDevelopParty = jcxfDevelopParty;//加密数据
            q1.and(i -> i.eq("idcard", finalJcxfDevelopParty.getIdcard()));
        }
        if(StringUtils.isNotBlank(developParty.getPhone()) && !developParty.getPhone().contains("*")){
            q1.and(i -> i.eq("phone", AESUtil2.encrypt(developParty.getPhone())));
        }else{
            JcxfDevelopPartyMemberVo finalJcxfDevelopParty = jcxfDevelopParty;//加密数据
            q1.and(i -> i.eq("phone", finalJcxfDevelopParty.getPhone()));
        }
        JcxfPartyMember jcxfPartyMember = jcxfPartyMemberMapper.selectOne(q1);

        QueryWrapper<JcxfDevelopParty> q2 = new QueryWrapper();
        q2.eq("del_flag", 0);
        if(StringUtils.isNotBlank(developParty.getIdcard()) && !developParty.getIdcard().contains("*")){
            q2.and(i -> i.eq("idcard", AESUtil2.encrypt(developParty.getIdcard())));
        }else{
            JcxfDevelopPartyMemberVo finalJcxfDevelopParty = jcxfDevelopParty;//加密数据
            q2.and(i -> i.eq("idcard", finalJcxfDevelopParty.getIdcard()));
        }
        if(StringUtils.isNotBlank(developParty.getPhone()) && !developParty.getPhone().contains("*")){
            q2.and(i -> i.eq("phone", AESUtil2.encrypt(developParty.getPhone())));
        }else{
            JcxfDevelopPartyMemberVo finalJcxfDevelopParty = jcxfDevelopParty;//加密数据
            q2.and(i -> i.eq("phone", finalJcxfDevelopParty.getPhone()));
        }
        if (developParty.getId() != null) {
            q2.ne("id", developParty.getId());
        }
        int count2 = jcxfDevelopPartyMapper.selectCount(q2);
        if (count2 > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在身份证或电话相同的信息");
        }
        Integer developState = developParty.getPersonType();

        if (developState.intValue() == 2 && developParty.getSubmitDate() == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 3 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 4 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.intValue() == 5 && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null || developParty.getPartyTime() == null)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"流程顺序不对，请检查发展纪实阶段顺序");
        }
        if (developState.intValue() == 2 || developState.intValue() == 3) {
            if ("1".equals(developParty.getIsTrain())) {
                developParty.setIsReturnWorkers(null);
            }
        }
        developParty.setDelFlag(0);
        if ((developState.intValue() == 4 || developState.intValue() == 5)) {
            JcxfPartyMember partyMember = new JcxfPartyMember();
            //同步数据到党员表
            JcxfPartyMember sync = sync(developParty, partyMember);
            if(jcxfDevelopParty != null){
                sync.setIdcard(AESUtil2.decrypt(jcxfDevelopParty.getIdcard()));
                sync.setPhone(AESUtil2.decrypt(jcxfDevelopParty.getPhone()));
                sync.setUsername(AESUtil2.decrypt(jcxfDevelopParty.getPhone()));
                //默认密码
                if(StringUtils.isNotBlank(sync.getIdcard())){
                    //默认密码
                    String idcard = partyMember.getIdcard();
                    String substring = null;
                    //初始密码默认为身份证号后6位
                    if (idcard != null && idcard != "") {
                        substring = idcard.substring(12);
                    }
                    //密码加密
                    String result = passwordManager.encryptPassword(substring);
                    sync.setPassword(result);
                }
            }
            if(jcxfPartyMemberMapper.selectById(developParty.getPartyMemberId()) == null){
                sync.setTenantId(tenantId);
                int save = jcxfPartyMemberMapper.insert(sync);
                if (save < 1) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"党员新增失败");
                } else {
                    developParty.setPartyMemberId(sync.getId());
                }
            }else{
                sync.setId(developParty.getPartyMemberId());
                jcxfPartyMemberMapper.updateById(sync);
            }
        }
        developParty.setUpdateDate(new Date());
        if(developParty.getIdcard().contains("*")){
            developParty.setIdcard(null);
        }
        if(developParty.getPhone().contains("*")){
            developParty.setPhone(null);
        }
        int res = jcxfDevelopPartyMapper.updateById(developParty);
        if (res > 0) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"修改失败");
        }
    }

    @Override
    public Map<String, Object> getDevelopParty(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            JcxfDevelopParty res = jcxfDevelopPartyMapper.selectById(id);
            if (res != null) {
                // 根据result拿到的deptId去查询对应的所属部门
                if (res.getDeptId() != null) {
                    JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(res.getDeptId());
                    res.setDeptName(tzSysDept.getName());
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    public IPage<JcxfDevelopParty> queryDevelopPartyList(JcxfDevelopParty developParty, SearchVo searchVo, PageVo pageVo, List<Long> listAll) {
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
        Page<JcxfDevelopParty> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfDevelopParty> queryWrapper = new QueryWrapper<>();
        if (developParty != null) {
            queryWrapper = LikeAllField(developParty, searchVo);
        }
        //根据所属组织id集合查询所有党员
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("party_member_develop.dept_id", listAll));
        }
        queryWrapper.orderByDesc("party_member_develop.create_date");

        IPage<JcxfDevelopParty> result = jcxfDevelopPartyMapper.getJcxfDevelopPartyByPage(pageData, queryWrapper);
        return result;
    }

    public JcxfPartyMember sync(JcxfDevelopParty developParty, JcxfPartyMember partyMember) {
        partyMember.setDeptId(developParty.getDeptId());
        partyMember.setRealName(developParty.getRealName());
        if(developParty.getIdcard().contains("*")){
            partyMember.setIdcard(null);
        }else{
            partyMember.setIdcard(developParty.getIdcard());
        }
        if(developParty.getPhone().contains("*")){
            partyMember.setPhone(null);
        }else{
            partyMember.setPhone(developParty.getPhone());
        }
        partyMember.setAddress(developParty.getAddress());
        partyMember.setWorkPosition(developParty.getWorkPosition());
        partyMember.setTransferTime(developParty.getTransferTime());
        partyMember.setPartyTime(developParty.getPartyTime());
        partyMember.setSex(developParty.getSex());
        partyMember.setBirthday(developParty.getBirthday());
        partyMember.setNational(String.valueOf(developParty.getNational()));
        partyMember.setEducation(developParty.getEducation());

        if (developParty.getTransferTime() != null) {
            partyMember.setTransferTime(developParty.getTransferTime());
        }
        if (developParty.getPersonType().intValue() == 4) { //讨论吸收为预备党员
            partyMember.setPersonType(2); // 预备党员
        } else if (developParty.getPersonType().intValue() == 5) {//转为正式党员
            partyMember.setPersonType(1); // 正式党员
        }
        // partyMember.setPosition(7);
        partyMember.setPartyState(1);// 党籍状态 正常
        partyMember.setDeptName(developParty.getDeptName());
        partyMember.setWorkUnit(developParty.getWorkUnit());

        partyMember.setIsFirstSecretary("0");
        partyMember.setIsFlow("0");
        partyMember.setIsLost("0");

        partyMember.setDelFlag(false);
        partyMember.setCreateDate(new Date());
        partyMember.setUsername(developParty.getPhone());

        //默认密码
        if(com.alibaba.nacos.client.utils.StringUtils.isNotBlank(partyMember.getIdcard())){
            //默认密码
            String idcard = partyMember.getIdcard();
            String substring = null;
            //初始密码默认为身份证号后6位
            if (idcard != null && idcard != "") {
                substring = idcard.substring(12);
            }
            //密码加密
            String result = passwordManager.encryptPassword(substring);
            partyMember.setPassword(result);
        }

        return partyMember;
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param developParty 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<JcxfDevelopParty> LikeAllField(JcxfDevelopParty developParty, SearchVo searchVo) {
        QueryWrapper<JcxfDevelopParty> queryWrapper = new QueryWrapper<>();
        if (developParty.getId() != null) {
            queryWrapper.and(i -> i.eq("party_member_develop.id", developParty.getId()));
        }
        if (StringUtils.isNotBlank(developParty.getRealName())) {
            queryWrapper.and(i -> i.like("party_member_develop.real_name", developParty.getRealName()));
        }
        if (StringUtils.isNotBlank(developParty.getIdcard())) {
            queryWrapper.and(i -> i.eq("party_member_develop.idcard", AESUtil2.encrypt(developParty.getIdcard())));
        }
        if (developParty.getSex() != null) {
            queryWrapper.and(i -> i.eq("party_member_develop.sex", developParty.getSex()));
        }
        if (StringUtils.isNotBlank(developParty.getEducation())) {
            queryWrapper.and(i -> i.eq("party_member_develop.education", developParty.getEducation()));
        }
        if (developParty.getNational() != null) {
            queryWrapper.and(i -> i.eq("party_member_develop.national", developParty.getNational()));
        }
        if (StringUtils.isNotBlank(developParty.getPhone())) {
            queryWrapper.and(i -> i.eq("party_member_develop.phone", AESUtil2.encrypt(developParty.getPhone())));
        }
        if (StringUtils.isNotBlank(developParty.getCollagesMajors())) {
            queryWrapper.and(i -> i.like("party_member_develop.collages_majors", developParty.getCollagesMajors()));
        }
        if (developParty.getPersonType() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.person_type", developParty.getPersonType()));
        }
        if (developParty.getSubmitDate() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.submit_date", developParty.getSubmitDate()));
        }
        if (developParty.getActiveDate() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.active_date", developParty.getActiveDate()));
        }
        if (developParty.getDevelopDate() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.develop_date", developParty.getDevelopDate()));
        }
        if (developParty.getPartyTime() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.party_time", developParty.getPartyTime()));
        }
        if (developParty.getTransferTime() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.transfer_time", developParty.getTransferTime()));
        }
        if (StringUtils.isNotBlank(developParty.getNotice())) {
            queryWrapper.and(i -> i.like("party_member_develop.notice", developParty.getNotice()));
        }
        if (developParty.getCreateDate() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.create_date", developParty.getCreateDate()));
        }
        if (developParty.getUpdateDate() != null) {
            queryWrapper.and(i -> i.like("party_member_develop.update_time", developParty.getUpdateDate()));
        }
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.and(i -> i.between("party_member_develop.create_date", searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        queryWrapper.and(i -> i.eq("party_member_develop.del_flag", 0));
        return queryWrapper;

    }

}

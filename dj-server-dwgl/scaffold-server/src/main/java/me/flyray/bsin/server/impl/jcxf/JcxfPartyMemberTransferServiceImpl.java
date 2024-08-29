package me.flyray.bsin.server.impl.jcxf;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.mapper.TzPayFeeDetailMapper;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfPartyMemberTransferService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.*;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.RespBodyHandler;
import me.flyray.bsin.server.utils.jcxf.Constant;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static me.flyray.bsin.server.utils.jcxf.DateUtils.getYear;

public class JcxfPartyMemberTransferServiceImpl implements JcxfPartyMemberTransferService {

    @Autowired
    private JcxfPartyMemberTransferMapper jcxfPartyMemberTransferMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Autowired
    private JcxfPartyTeamMemberMapper jcxfPartyTeamMemberMapper;

    @Autowired
    private JcxfSysUserPartyMapper jcxfSysUserPartyMapper;

    @Autowired
    private JcxfPartyMoneyDuesMapper jcxfPartyMoneyDuesMapper;

    @Autowired
    private JcxfSysAreaMapper jcxfSysAreaMapper;

    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;

    @Override
    public Map<String, Object> queryPartyMemberDeptTransferLogList(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            JcxfPartyMemberTransfer partyMemberDeptTransferLog = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMemberDeptTransferLog")), JcxfPartyMemberTransfer.class);
            JcxfPartyMember partyMember = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMember")), JcxfPartyMember.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

            String deptId = (String) dataMap.get("deptId");

            List<Long> listAll = new ArrayList<>();
            if (StringUtils.isNotBlank(deptId)) {
                //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
                listAll.add(Long.valueOf(deptId));

                IPage<JcxfPartyMemberTransfer> result = queryPartyMemberDeptTransferLogListByPage(partyMemberDeptTransferLog, searchVo, pageVo, listAll, partyMember);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
            }
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }

    }

    @Override
    public synchronized Map<String, Object> addPartyMemberDeptTransfer(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            JcxfPartyMemberTransfer log = JSON.parseObject(JSON.toJSONString(map.get("partyMemberDeptTransferLog")), JcxfPartyMemberTransfer.class);
            String deptId = (String) map.get("deptId");

            //判断当前党员是否已经有待审核转移
            QueryWrapper q = new QueryWrapper();
            q.eq("del_flag", 0);
            q.eq("party_id", log.getPartyId());
            q.eq("transfer_status", 2);
            int count = jcxfPartyMemberTransferMapper.selectCount(q);
            if (count > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"已处于挂起状态，不能再次进行党员信息转移");
            }
            QueryWrapper q2 = new QueryWrapper();
            q2.eq("id", log.getPartyId());
            q2.eq("is_lost", "1");
            q2.eq("del_flag", false);
            int count2 = jcxfPartyMemberMapper.selectCount(q2);
            if (count2 > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该党员是失联党员，不能进行党员信息转移");
            }
            QueryWrapper q3 = new QueryWrapper();
            q3.eq("party_id", log.getPartyId());
            q3.eq("accept_status", "0");
            int count3 = jcxfPartyMemberTransferMapper.selectCount(q3);
            if (count3 > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"已有转移信息处于待处理状态，不能再次进行党员信息转移");
            }

            log.setAcceptStatus("0"); //状态 处理状态(0:待处理,1:接收,2：打回)
            log.setTransferStatus("1"); // 当前状态(1:已处理,2:挂起)
            log.setTransferTime(new Date());
            log.setCreateDate(new Date());
            log.setDelFlag(0);

            int res = jcxfPartyMemberTransferMapper.insert(log);
            if (res > 0) {
                JcxfSysDictionary jcxfSysDictionary = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyTransferType", "市外");
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
                if (log.getTransferType().equals(jcxfSysDictionary.getDetail()) ||
                    !ArrayUtils.contains(Constant.BRANCH_ARRAY, dept.getType().intValue())) {
                    handlePartyMemberTransfer(log.getId());

                    JcxfPartyMemberTransfer transfer = new JcxfPartyMemberTransfer();
                    transfer.setId(log.getId());
                    transfer.setAcceptStatus("1");
                    transfer.setUpdateDate(new Date());
                    transfer.setUpdateBy(log.getCreateBy());
                    transfer.setTransferTime(new Date());
                    jcxfPartyMemberTransferMapper.updateById(transfer);

                    TzPayFeeDetail payFeeDetail = new TzPayFeeDetail();
                    payFeeDetail.setPartyMemberId(log.getPartyId());
                    payFeeDetail.setDelFlag(1);
                    tzPayFeeDetailMapper.updatePayFeeDetailDelFlag(payFeeDetail);
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存出错");

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    public void handlePartyMemberTransfer(Long id) {
        JcxfPartyMemberTransfer partyMemberTransfer = jcxfPartyMemberTransferMapper.selectById(id);
        JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(partyMemberTransfer.getPartyId());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("party_id", partyMemberTransfer.getPartyId());
        List<JcxfPartyTeamMember> partyTeamMembers = jcxfPartyTeamMemberMapper.selectList(queryWrapper);
        if (partyTeamMembers != null && partyTeamMembers.size() > 0) {
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("party_id", partyMemberTransfer.getPartyId());
            jcxfPartyTeamMemberMapper.delete(updateWrapper);
        }

        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("party_id", partyMemberTransfer.getPartyId());
        List<JcxfSysUserParty> sysUserParties = jcxfSysUserPartyMapper.selectList(queryWrapper1);
        if (sysUserParties != null && sysUserParties.size() > 0) {
            QueryWrapper updateWrapper = new QueryWrapper();
            updateWrapper.eq("party_id", Long.valueOf(partyMemberTransfer.getPartyId().longValue()));
            jcxfSysUserPartyMapper.delete(updateWrapper);
        }
        JcxfSysDictionary dictionaryIn = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyTransferType", "市内");
        JcxfSysDictionary dictionaryOut = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyTransferType", "市外");
        if (partyMemberTransfer.getTransferType().equals(dictionaryIn.getDetail())) {
            JcxfSysDictionary dictionary = jcxfSysDictionaryMapper.getByCodeAndDetailName("position", "无");
            partyMember.setPosition(Integer.valueOf((dictionary == null) ? 0 : Integer.valueOf(dictionary.getDetail())));
            partyMember.setDeptId(partyMemberTransfer.getInBranchId());
        } else if (partyMemberTransfer.getTransferType().equals(dictionaryOut.getDetail())) {
            partyMember.setDelFlag(Boolean.valueOf(true));
        }
        //2021-09-09 组织关系转移时更新党费缴纳数据
//        changForMemberTransfer(partyMemberTransfer);
        //end

        partyMember.setUpdateDate(new Date());
        partyMember.setIdcard(null);
        partyMember.setPhone(null);
        jcxfPartyMemberMapper.updateById(partyMember);

        //更新党费的部门
        TzPayFeeDetail payFeeDetail = new TzPayFeeDetail();
        payFeeDetail.setPartyMemberId(partyMember.getId());
        payFeeDetail.setDeptId(partyMember.getDeptId());
        payFeeDetail.setDeptName(jcxfSysDeptMapper.selectById( partyMember.getDeptId()).getName());
        tzPayFeeDetailMapper.updatePartyMemberDeptInfo(payFeeDetail);
    }


    /**
     * 党组织关系转移时更新党费应缴记录
     * @param transfer
     */
    public void changForMemberTransfer(JcxfPartyMemberTransfer transfer){
        //change_type 转移类型(1:转移至市内,2:转移至市外)
        // 以下为酒城先锋部分，但酒城先锋党费缴纳未使用
        /*String transferType = transfer.getTransferType();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("party_id", Long.valueOf(transfer.getPartyId()));
        queryWrapper.eq("dept_id", Long.valueOf(transfer.getOutBranchId()));
        queryWrapper.eq("del_flag", 0);
        queryWrapper.like("pay_month", getYear()+"%");
        List<JcxfPartyMoneyDues> partyMoneyDues = jcxfPartyMoneyDuesMapper.selectList(queryWrapper);

        for(JcxfPartyMoneyDues due:partyMoneyDues) {
            //1:转移至市内
            UpdateWrapper updateWrapper = new UpdateWrapper();
            if("1".equals(transferType))//更新当前年当前月之后的所有数据
                updateWrapper.set("dept_id", transfer.getInBranchId()); //更新组织为转入组织
            else //转移至市外 直接逻辑删除
                updateWrapper.set("del_flag", true); //逻辑删除
            jcxfPartyMoneyDuesMapper.update(null, updateWrapper);
        }*/
    }

    @Override
    public Map<String, Object> delPartyMemberDeptTransfer(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            JcxfPartyMemberTransfer transfer = jcxfPartyMemberTransferMapper.selectById(id);
            if (transfer == null || !transfer.getDelFlag().equals(0)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到转接信息");
            }

            // 处理状态(0:待处理,1:接收,2：打回)
            if (!transfer.getAcceptStatus().equals("0")) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"转接申请已处理，不可撤销");
            }

            int res = jcxfPartyMemberTransferMapper.deleteById(id);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除出错");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberDeptTransferById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            JcxfPartyMemberTransfer log = jcxfPartyMemberTransferMapper.selectById(id);
            if (log != null) {
                log.setPartyMember(jcxfPartyMemberMapper.selectById(log.getPartyId()));
                log.setOldTzSysDept(jcxfSysDeptMapper.selectById(log.getOutBranchId()));
                log.setNewTzSysDept(jcxfSysDeptMapper.selectById(log.getInBranchId()));

                if (log.getAreaId() !=null) {
                    JcxfSysArea area = jcxfSysAreaMapper.selectById(log.getAreaId());
                    if (area != null) {
                        log.setAreaName(area.getMergerName());
                    }
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(log));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取出错");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取异常");
        }
    }

    @Override
    @DS("jcxf")
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateTransferStatusById(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String id = (String) map.get("id");
            String acceptStatus = (String) map.get("acceptStatus");
            String rejectReason = (String) map.get("rejectReason");

            JcxfPartyMemberTransfer transfer = jcxfPartyMemberTransferMapper.selectById(id);
            if (transfer == null || !transfer.getDelFlag().equals(0)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该申请不存在");
            }

            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", id);
            up.set("accept_status", acceptStatus);
            up.set("reject_reason", rejectReason);
            up.set("create_date", new Date());
            up.set("update_date", new Date());
            up.set("transfer_time", new Date());

            int res = jcxfPartyMemberTransferMapper.update(null, up);
            if (res > 0) {
                JcxfSysDictionary jcxfSysDictionary = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyTransferAcceptStatus", "已接收");
                if (jcxfSysDictionary != null && jcxfSysDictionary.getDetail().equals(acceptStatus)) {
                    handlePartyMemberTransfer(Long.valueOf(id));
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"审核异常");
        }
    }

    @Override
    public Map<String, Object> getTransferPageByPartyMemberId(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");

            JcxfPartyMemberTransfer partyMemberDeptTransferLog = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMemberDeptTransferLog")), JcxfPartyMemberTransfer.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

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

            Page<JcxfPartyMemberTransfer> pageData = new Page<>(page, limit);
            QueryWrapper<JcxfPartyMemberTransfer> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_id", partyMemberDeptTransferLog.getPartyId());

            if (StringUtils.isNotEmpty(partyMemberDeptTransferLog.getAcceptStatus())) {
                queryWrapper.eq("accept_status", partyMemberDeptTransferLog.getAcceptStatus());
            }

            queryWrapper.eq("del_flag", 0);
            queryWrapper.orderByDesc("create_date");

            IPage<JcxfPartyMemberTransfer> result = jcxfPartyMemberTransferMapper.selectPage(pageData, queryWrapper);
            for (int i = 0; i < result.getRecords().size(); i++) {
                JcxfPartyMemberTransfer log = result.getRecords().get(i);
                log.setOldTzSysDept(jcxfSysDeptMapper.selectById(log.getOutBranchId()));
                log.setNewTzSysDept(jcxfSysDeptMapper.selectById(log.getInBranchId()));


                if (log.getAreaId() != null) {
                    JcxfSysArea area = jcxfSysAreaMapper.selectById(log.getAreaId());
                    if (area != null) {
                        log.setAreaName(area.getMergerName());
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> cancelTransferById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            JcxfPartyMemberTransfer transfer = jcxfPartyMemberTransferMapper.selectById(id);
            if (transfer == null || !transfer.getDelFlag().equals(0)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到转接信息");
            }

            QueryWrapper q = new QueryWrapper();
            q.eq("id", id);

            int res = jcxfPartyMemberTransferMapper.delete(q);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"取消失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"取消异常");
        }
    }

    public IPage<JcxfPartyMemberTransfer> queryPartyMemberDeptTransferLogListByPage(JcxfPartyMemberTransfer partyMemberDeptTransferLog, SearchVo searchVo, PageVo pageVo, List<Long> listAll, JcxfPartyMember partyMember) {
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

        Page<JcxfPartyMemberTransfer> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfPartyMemberTransfer> queryWrapper = new QueryWrapper<>();

        queryWrapper.and(i -> i.in("party_member_transfer.in_branch_id", listAll).or(i2 -> i2.in("party_member_transfer.out_branch_id", listAll)));

        if (StringUtils.isNotBlank(partyMember.getRealName()) || StringUtils.isNotBlank(partyMember.getIdcard())) {
            QueryWrapper q = new QueryWrapper();
            q.select("party_member.id");
            q.eq("party_member.del_flag", 0);
            // q.in("dept_id", listAll);
            if (StringUtils.isNotBlank(partyMember.getRealName())) {
                q.like("party_member.real_name", partyMember.getRealName());
            }
            if (StringUtils.isNotBlank(partyMember.getIdcard())) {
                q.like("party_member.idcard", partyMember.getIdcard());
            }
            List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.selectList(q);
            if (partyMemberList != null && partyMemberList.size() > 0) {
                List<Long> partyMemberIds = new ArrayList<>();
                for (JcxfPartyMember member: partyMemberList) {
                    partyMemberIds.add(member.getId());
                }
                queryWrapper.in("party_member_transfer.party_id", partyMemberIds);
            } else {
                IPage<JcxfPartyMemberTransfer> result = new Page<>();
                result.setTotal(0);
                result.setRecords(new ArrayList<>());

                return result;
            }
        }

        if (StringUtils.isNotBlank(partyMemberDeptTransferLog.getAcceptStatus())) {
            queryWrapper.eq("party_member_transfer.accept_status", partyMemberDeptTransferLog.getAcceptStatus());
        }

        if (StringUtils.isNotBlank(partyMemberDeptTransferLog.getTransferType())) {
            queryWrapper.eq("party_member_transfer.transfer_type", partyMemberDeptTransferLog.getTransferType());
        }

        queryWrapper.eq("party_member_transfer.del_flag", 0);
        queryWrapper.orderByDesc("party_member_transfer.create_date");

        IPage<JcxfPartyMemberTransfer> result = jcxfPartyMemberTransferMapper.getJcxfPartyMemberTransferByPage(pageData, queryWrapper);
        return result;
    }
}

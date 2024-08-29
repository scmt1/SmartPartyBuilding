package me.flyray.bsin.server.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.flyray.bsin.facade.service.TzTwoBestOneFirstAuditService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.TzPartyHonor;
import me.flyray.bsin.server.domain.TzTwoBestOneFirstAudit;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.TzTwoBestOneFirst;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TzTwoBestOneFirstAuditServiceImpl implements TzTwoBestOneFirstAuditService {

    @Autowired
    private TzTwoBestOneFirstAuditMapper tzTwoBestOneFirstAuditMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzTwoBestOneFirstMapper tzTwoBestOneFirstMapper;

    @Autowired
    private TzPartyHonorMapper tzPartyHonorMapper;

    @Override
    public Map<String, Object> getAuditListByTbofId(Map<String, Object> requestMap) {
        try {
            String tbofId = (String) requestMap.get("tbofId");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("tbof_id", tbofId);
            //queryWrapper.orderByDesc("create_time");

            List<TzTwoBestOneFirstAudit> auditList = tzTwoBestOneFirstAuditMapper.selectList(queryWrapper);
            for (TzTwoBestOneFirstAudit audit: auditList) {
                audit.setTzSysDept(jcxfSysDeptMapper.selectById(audit.getAuditDeptId()));
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(auditList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    @DSTransactional
    public Map<String, Object> addAudit(Map<String, Object> requestMap) throws Exception {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            TzTwoBestOneFirstAudit tzTwoBestOneFirstAudit =  JSON.parseObject(JSON.toJSONString(map.get("tzTwoBestOneFirstAudit")), TzTwoBestOneFirstAudit.class);


            // 修改表单的状态
            TzTwoBestOneFirst tzTwoBestOneFirst = tzTwoBestOneFirstMapper.selectById(tzTwoBestOneFirstAudit.getTbofId());
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(tzTwoBestOneFirst.getCreateDeptId());
            JcxfSysDept tzSysDeptAudit = jcxfSysDeptMapper.selectById(tzTwoBestOneFirstAudit.getAuditDeptId());

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", tzTwoBestOneFirstAudit.getTbofId());

            // 如果是本单位审核
            if (tzTwoBestOneFirst.getCreateDeptId().equals(tzTwoBestOneFirstAudit.getAuditDeptId())) {
                if ("1".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "2"); // 单位通过
                    tzTwoBestOneFirstAudit.setAuditStatus("2");
                } else if ("2".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "3"); // 单位驳回
                    tzTwoBestOneFirstAudit.setAuditStatus("3");
                }
            } else if (tzTwoBestOneFirstAudit.getAuditDeptId().equals(tzSysDept.getParentId())) {
                // 如果是上级审核
                // 审核状态 1、通过  2、驳回
                if ("1".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "4"); // 党委党组通过
                    tzTwoBestOneFirstAudit.setAuditStatus("4");
                } else if ("2".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "5"); // 党委党组驳回
                    tzTwoBestOneFirstAudit.setAuditStatus("5");
                }
            } else if (tzSysDeptAudit.getButtonRole().indexOf("auditTBOF") > -1) {
                if ("1".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "6"); // 党委党组通过
                    tzTwoBestOneFirstAudit.setAuditStatus("6");
                } else if ("2".equals(tzTwoBestOneFirstAudit.getAuditStatus())) {
                    updateWrapper.set("status", "7"); // 党委党组驳回
                    tzTwoBestOneFirstAudit.setAuditStatus("7");
                }
            }
            if(StringUtils.isNotBlank(tzTwoBestOneFirstAudit.getCommendType())){
                updateWrapper.set("commend_type", tzTwoBestOneFirstAudit.getCommendType());
                updateWrapper.set("commend_img", tzTwoBestOneFirstAudit.getCommendImg());
            }
            int count = tzTwoBestOneFirstMapper.update(null, updateWrapper);
            if (count > 0) {
                tzTwoBestOneFirstAudit.setCreateTime(new Date());
                tzTwoBestOneFirstAuditMapper.insert(tzTwoBestOneFirstAudit);
                //审核通过加入荣誉
                if("6".equals(tzTwoBestOneFirstAudit.getAuditStatus())){
                    TzPartyHonor tzPartyHonor = new TzPartyHonor();
                    if("1".equals(tzTwoBestOneFirst.getTableType())){
                        tzPartyHonor.setHonorName("优秀共产党员");
                        tzPartyHonor.setPartyMemberId(tzTwoBestOneFirst.getPartyMemberId());
                        tzPartyHonor.setHonorType("2");
                        tzPartyHonor.setImagePath(tzTwoBestOneFirstAudit.getCommendImg());
                    } else if("2".equals(tzTwoBestOneFirst.getTableType())){
                        tzPartyHonor.setHonorName("优秀党务工作者");
                        tzPartyHonor.setPartyMemberId(tzTwoBestOneFirst.getPartyMemberId());
                        tzPartyHonor.setHonorType("2");
                        tzPartyHonor.setImagePath(tzTwoBestOneFirstAudit.getCommendImg());
                    } else if("3".equals(tzTwoBestOneFirst.getTableType())){
                        tzPartyHonor.setHonorName("先进基层党组织");
                        tzPartyHonor.setHonorType("1");
                        tzPartyHonor.setImagePath(tzTwoBestOneFirstAudit.getCommendImg());
                    }
                    tzPartyHonor.setHonorClass("3");
                    tzPartyHonor.setDeptId(tzTwoBestOneFirst.getCreateDeptId());
                    tzPartyHonor.setDelFlag(0);
                    tzPartyHonor.setCreateTime(new Date());
                    tzPartyHonor.setGetYear(DateUtil.format(tzTwoBestOneFirst.getCreateTime(), "yyyy"));
                    tzPartyHonor.setStatus("2"); //审核状态（1、待审核，2、审核通过，3、已驳回）
                    tzPartyHonor.setAwardName("两优一先");
                    tzPartyHonorMapper.insert(tzPartyHonor);
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new Exception("提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("提交失败");
        }
    }
}

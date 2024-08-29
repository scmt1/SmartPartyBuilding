package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.TzMessageAutoRoleService;
import me.flyray.bsin.facade.service.TzUserTagService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import me.flyray.bsin.server.mapper.TzMessageAutoMapper;
import me.flyray.bsin.server.mapper.TzMessageAutoRoleMapper;
import me.flyray.bsin.server.mapper.TzUserTagMapper;
import me.flyray.bsin.server.mapper.TzUserTagUserMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.AESUtil2;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TzUserTagServiceImpl implements TzUserTagService {

    @Autowired
    private TzUserTagMapper tzUserTagMapper;
    @Autowired
    private TzUserTagUserMapper tzUserTagUserMapper;

    @Override
    @Transactional
    public Map<String, Object> addCustomUserTag(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

        Integer id = (Integer) map.get("id");
        String tagName = (String) map.get("tagName");
        String tenantId = (String) requestMap.get("bizTenantId");
        List<String> userIdList = (List<String>) map.get("userIdList");

        TzUserTag tzUserTag = new TzUserTag();
        tzUserTag.setTagName(tagName);
        tzUserTag.setCreateTime(new Date());
        tzUserTag.setTenantId(tenantId);
        tzUserTag.setUserNum(userIdList.size());
        if (null != id) {
            tzUserTag.setId(id);
            tzUserTagMapper.updateById(tzUserTag);
            LambdaQueryWrapper<TzUserTagUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TzUserTagUser::getUserTagId, id);
            tzUserTagUserMapper.delete(queryWrapper);
        } else {
            tzUserTagMapper.insert(tzUserTag);
        }

        TzUserTagUser tzUserTagUser = null;
        for (String s : userIdList) {
            tzUserTagUser = new TzUserTagUser();
            tzUserTagUser.setUserTagId(tzUserTag.getId());
            tzUserTagUser.setUserId(s);
            tzUserTagUserMapper.insert(tzUserTagUser);
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
    }

    @Override
    public Map<String, Object> deleteCustomUserTag(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        Integer id = (Integer) map.get("id");
        tzUserTagMapper.deleteById(id);

        LambdaQueryWrapper<TzUserTagUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TzUserTagUser::getUserTagId, id);
        tzUserTagUserMapper.delete(queryWrapper);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
    }

    @Override
    public Map<String, Object> queryCustomUserTag(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String tagName = (String) map.get("tagName");
        LambdaQueryWrapper<TzUserTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(tagName), TzUserTag::getTagName, tagName);
        List<TzUserTag> tzUserTags = tzUserTagMapper.selectList(queryWrapper);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUserTags));
    }

    @Override
    public Map<String, Object> getCustomUserTag(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        Integer id = (Integer) map.get("id");
        TzUserTag tzUserTag = tzUserTagMapper.selectById(id);
        List<JcxfPartyMemberVo> jcxfPartyMemberVos = tzUserTagUserMapper.queryTableData(id);
        for (JcxfPartyMemberVo partyMemberVo:jcxfPartyMemberVos) {
            partyMemberVo.setPhone(AESUtil2.decrypt(partyMemberVo.getPhone()));
        }
        tzUserTag.setTableData(jcxfPartyMemberVos);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUserTag));
    }
}

package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzTeamActiveService;
import me.flyray.bsin.facade.service.TzTeamService;
import me.flyray.bsin.facade.service.TzTradeActiveService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.TzTeam;
import me.flyray.bsin.server.domain.TzTradeActive;
import me.flyray.bsin.server.mapper.AttachFileMapper;
import me.flyray.bsin.server.mapper.TzTeamMapper;
import me.flyray.bsin.server.mapper.TzTradeActiveMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzTeamServiceImpl extends ServiceImpl<TzTeamMapper, TzTeam> implements TzTeamService {
    @Autowired
    private TzTeamMapper tzTeamMapper;

    @Autowired
    private AttachFileMapper attachFileMapper;


    @Override
    public Map<String, Object> addTeam(Map<String, Object> requestMap) {
        try {
            TzTeam team = JSON.parseObject(JSON.toJSONString(requestMap.get("team")), TzTeam.class);

            QueryWrapper<TzTeam> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("tz_team.dept_id",team.getDeptId());
            TzTeam tzTeam = tzTeamMapper.selectOne(queryWrapper);

            if(tzTeam==null){
                team.setCreateTime(new Date());
                int insert = tzTeamMapper.insert(team);
                if(insert>0){
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(team));
                }else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }else {
                tzTeam.setName(team.getName()!=null ? team.getName() : tzTeam.getName());
                tzTeam.setMaster(team.getMaster()!=null ? team.getMaster() : tzTeam.getMaster());
                tzTeam.setPhone(team.getPhone()!=null ? team.getPhone() : tzTeam.getPhone());
                tzTeam.setContacts(team.getContacts()!=null ? team.getContacts() : tzTeam.getContacts());
                tzTeam.setContactsPhone(team.getContactsPhone()!=null ? team.getContactsPhone() : tzTeam.getContactsPhone());
                tzTeam.setAddr(team.getAddr()!=null ? team.getAddr() : tzTeam.getAddr());
                tzTeam.setPersonCount(team.getPersonCount()!=null ? team.getPersonCount() : tzTeam.getPersonCount());
                tzTeam.setUpdateTime(new Date());
                int insert = tzTeamMapper.updateById(tzTeam);
                if(insert>0){
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTeam));
                }else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }

    @Override
    public Map<String, Object> queryTeamByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            if(deptId==null){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"部门id为空，请联系管理员");
            }
            QueryWrapper<TzTeam> queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("tz_team.dept_id",deptId);
            TzTeam tzTeam = tzTeamMapper.selectOne(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTeam));
        }catch (Exception e){
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"操作异常");
        }
    }
}

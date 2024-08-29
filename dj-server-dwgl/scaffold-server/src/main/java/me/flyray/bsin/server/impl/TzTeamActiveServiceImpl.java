package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzTeamActiveService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.AttachFileMapper;
import me.flyray.bsin.server.mapper.TzTeamActiveMapper;
import me.flyray.bsin.server.mapper.TzTeamMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
public class TzTeamActiveServiceImpl extends ServiceImpl<TzTeamActiveMapper, TzTeamActive> implements TzTeamActiveService {
    @Autowired
    private TzTeamActiveMapper tzTeamActiveMapper;

    @Autowired
    private AttachFileMapper attachFileMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> addTeamActive(Map<String, Object> requestMap) {
        try {
            TzTeamActive tzTeamActive = JSON.parseObject(JSON.toJSONString(requestMap.get("active")), TzTeamActive.class);

            if (tzTeamActive.getId() == null) {
                tzTeamActive.setDelFlag(0);
                tzTeamActive.setCreateTime(new Date());
                int insert = tzTeamActiveMapper.insert(tzTeamActive);
                if (insert <= 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                tzTeamActive.setUpdateTime(new Date());
                tzTeamActiveMapper.updateById(tzTeamActive);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"添加失败");
        }
    }

    @Override
    public Map<String, Object> queryTeamActiveList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
        TzTeamActive tzTeamActive = JSON.parseObject(JSON.toJSONString(dataMap.get("active")), TzTeamActive.class);
        try {
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

            IPage<TzTeamActive> pageData = new Page<>(page, limit);
            QueryWrapper<TzTeamActive> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("tz_team_active.team_id", tzTeamActive.getTeamId());
            IPage<TzTeamActive> result = tzTeamActiveMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getTeamActiveById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"id为空，请联系管理员");
            }
            TzTeamActive tzTeamActive = tzTeamActiveMapper.selectById(id);
            if (tzTeamActive == null || tzTeamActive.getDelFlag().intValue() != 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到信息");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTeamActive));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> deleteTeamActive(Map<String, Object> requestMap) {
        try {
            List<String> ids = new ArrayList<>();
            for (Object id : (List<?>) requestMap.get("ids")) {
                ids.add(String.valueOf(id));
            }
            if (ids == null || ids.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }

            UpdateWrapper<TzTeamActive> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tz_team_active.id", ids);
            updateWrapper.set("del_flag", 1);
            int update = tzTeamActiveMapper.update(null, updateWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }
}

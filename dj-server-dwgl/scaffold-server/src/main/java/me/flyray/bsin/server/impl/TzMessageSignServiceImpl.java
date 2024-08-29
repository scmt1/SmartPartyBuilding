package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzMessageSignService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzMessageSignMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

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

public class TzMessageSignServiceImpl extends ServiceImpl<TzMessageSignMapper, TzMessageSign> implements TzMessageSignService {

    @Autowired
    private TzMessageSignMapper tzMessageSignMapper;

    @Override
    public Map<String, Object> saveMessageSign(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String tenantId = (String) requestMap.get("bizTenantId");
        TzMessageSign messageSign = JSON.parseObject(JSON.toJSONString(map.get("tzMessageSign")),TzMessageSign.class);
        messageSign.setTenantId(tenantId);
        messageSign.setCreateTime(new Date());
        int insert = tzMessageSignMapper.insert(messageSign);
        if (insert > 0) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> deleteMessageSign(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("id", id);
        updateWrapper.set("del_flag", 1);
        int update = tzMessageSignMapper.update(null, updateWrapper);
        if (update > 0) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }
    }

    @Override
    public Map<String, Object> updateMessageSign(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        TzMessageSign messageSign = JSON.parseObject(JSON.toJSONString(map.get("tzMessageSign")),TzMessageSign.class);
        int res = tzMessageSignMapper.updateById(messageSign);
        if (res > 0) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> getMessageSign(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        TzMessageSign messageSign = tzMessageSignMapper.selectById(id);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(messageSign));
    }

    @Override
    public Map<String, Object> queryMessageSign(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String tenantId = (String) requestMap.get("bizTenantId");
            TzMessageSign messageSign = JSON.parseObject(JSON.toJSONString(map.get("tzMessageSign")),TzMessageSign.class);
            messageSign.setTenantId(tenantId);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("tzMessageSign")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);
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
            Page<TzMessageSign> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessageSign> queryWrapper = new QueryWrapper<>();
            if (messageSign != null) {
                queryWrapper = LikeAllField(messageSign, searchVo);
            }
            IPage<TzMessageSign> result = tzMessageSignMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> queryMessageSignList(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            QueryWrapper<TzMessageSign> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id",tenantId);
            queryWrapper.eq("del_flag",0);
            List<TzMessageSign> tzMessageSigns = tzMessageSignMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageSigns));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzMessageSign 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzMessageSign> LikeAllField(TzMessageSign tzMessageSign, SearchVo searchVo) {
        QueryWrapper<TzMessageSign> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(tzMessageSign.getSign())) {
            queryWrapper.and(i -> i.like("tz_message_sign.sign", tzMessageSign.getSign()));
        }
        if (StringUtils.isNotBlank(tzMessageSign.getTenantId())) {
            queryWrapper.and(i -> i.eq("tz_message_sign.tenant_id", tzMessageSign.getTenantId()));
        }
        queryWrapper.and(i -> i.eq("tz_message_sign.del_flag", 0));
        return queryWrapper;

    }
}

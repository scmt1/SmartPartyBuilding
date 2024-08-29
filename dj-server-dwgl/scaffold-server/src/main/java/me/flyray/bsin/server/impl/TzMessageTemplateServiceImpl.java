package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzMessageTemplateService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzMessageTemplateMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzMessageTemplateServiceImpl extends ServiceImpl<TzMessageTemplateMapper, TzMessageTemplate> implements TzMessageTemplateService {
    @Autowired
    private TzMessageTemplateMapper tzMessageTemplateMapper;

    @Override
    public Map<String, Object> addMessageTemplate(Map<String, Object> requestMap) {
        try {
            TzMessageTemplate tzMessageTemplate = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzMessageTemplate.class);
            tzMessageTemplate.setCreateTime(new Date());
            int insert = tzMessageTemplateMapper.insert(tzMessageTemplate);
            if(insert<0){
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("保存成功"));
        }catch (Exception e){
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }
}

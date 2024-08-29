package me.flyray.bsin.server.impl;

import me.flyray.bsin.facade.service.TDictService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.mapper.TDictMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class TDictServiceImpl implements TDictService {

    @Autowired
    private TDictMapper tDictMapper;

    @Override
    public Map<String, Object> findByType(Map<String, Object> requestMap) {
        String type = (String) requestMap.get("deptId");
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tDictMapper.findByType(type)));
    }
}

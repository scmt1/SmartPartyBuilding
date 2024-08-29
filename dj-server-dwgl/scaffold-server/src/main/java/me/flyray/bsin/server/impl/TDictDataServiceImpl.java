package me.flyray.bsin.server.impl;

import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.facade.service.TDictDataService;
import me.flyray.bsin.server.common.constant.CommonConstant;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.TDict;
import me.flyray.bsin.server.domain.TDictData;
import me.flyray.bsin.server.mapper.TDictDataMapper;
import me.flyray.bsin.server.mapper.TDictMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class TDictDataServiceImpl implements TDictDataService {

    @Autowired
    private TDictMapper tDictMapper;

    @Autowired
    private TDictDataMapper tDictDataMapper;

    @Override
    public Map<String, Object> getByType(Map<String, Object> requestMap) {
        String type = (String) requestMap.get("type");
        TDict dict = tDictMapper.findByType(type);
        if (dict == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"字典类型 " + type + " 不存在");
        }
        List<TDictData> list = tDictDataMapper.findByDictIdAndStatusOrderBySortOrder(dict.getId(), CommonConstant.STATUS_NORMAL);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
    }
}

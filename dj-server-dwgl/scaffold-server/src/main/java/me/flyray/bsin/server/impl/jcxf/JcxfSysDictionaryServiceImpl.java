package me.flyray.bsin.server.impl.jcxf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.flyray.bsin.facade.service.JcxfSysDictionaryService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.jcxf.JcxfResponseDic;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDictionary;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDictionaryMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JcxfSysDictionaryServiceImpl implements JcxfSysDictionaryService {

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Override
    public Map<String, Object> getByCodeAndDetailName(Map<String, Object> requestMap) {
        String code = (String) requestMap.get("code");
        String detailName = (String) requestMap.get("detailName");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        queryWrapper.eq("detailName", detailName);

        return null;
    }

    @Override
    public Map<String, Object> getDictByCode(Map<String, Object> requestMap) {
        try {
            String code = (String) requestMap.get("code");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("code", code);
            queryWrapper.eq("del_flag", false);
            queryWrapper.orderByAsc("sort");
            List<JcxfSysDictionary> dictionaryList = jcxfSysDictionaryMapper.selectList(queryWrapper);

            List<JcxfResponseDic> responseDicList = new ArrayList<>(dictionaryList.size());
            for (JcxfSysDictionary dic: dictionaryList) {
                JcxfResponseDic responseDic = new JcxfResponseDic();
                responseDic.setLabel(dic.getDetailName());
                responseDic.setItemValue(dic.getDetail());
                responseDicList.add(responseDic);
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(responseDicList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }
}

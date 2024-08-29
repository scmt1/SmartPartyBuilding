package me.flyray.bsin.server.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import me.flyray.bsin.facade.service.TzMessageAutoContentService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.TzMessageAutoContent;
import me.flyray.bsin.server.mapper.TzMessageAutoContentMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

public class TzMessageAutoContentServiceImpl implements TzMessageAutoContentService {

    @Autowired
    private TzMessageAutoContentMapper tzMessageAutoContentMapper;

    @Override
    public Map<String, Object> editAutoContent(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String id = (String) map.get("id");
            String autoId = (String) map.get("autoId");
            String deptId = (String) map.get("deptId");
            String temContent = (String) map.get("temContent");

            int count = 0;
            if (StringUtils.isNotEmpty(id)) {
                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", id);
                updateWrapper.set("tem_content", temContent);
                updateWrapper.set("update_date", new Date());
                count = tzMessageAutoContentMapper.update(null, updateWrapper);

            } else {
                TzMessageAutoContent content = new TzMessageAutoContent();
                content.setTemContent(temContent);
                content.setAutoId(Integer.parseInt(autoId));
                content.setDeptId(Long.parseLong(deptId));
                content.setCreateDate(new Date());
                count = tzMessageAutoContentMapper.insert(content);
            }

            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }
}

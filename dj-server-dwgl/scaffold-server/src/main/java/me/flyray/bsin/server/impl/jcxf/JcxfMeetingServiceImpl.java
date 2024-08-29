package me.flyray.bsin.server.impl.jcxf;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.flyray.bsin.facade.service.JcxfMeetingService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMeeting;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMeetingMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class JcxfMeetingServiceImpl implements JcxfMeetingService {

    @Autowired
    private JcxfPartyMeetingMapper jcxfPartyMeetingMapper;

    @Override
    public Map<String, Object> getMeetingByPartyMemberId(Map<String, Object> requestMap) {
        String partyMemberId = (String) requestMap.get("partyMemberId");
        try {
            List<JcxfPartyMeeting> result = jcxfPartyMeetingMapper.getMeetingByPartyMemberId(partyMemberId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }
}

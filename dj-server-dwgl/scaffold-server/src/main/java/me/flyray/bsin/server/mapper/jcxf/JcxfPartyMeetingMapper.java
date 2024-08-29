package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMeeting;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS(value = "jcxf")
public interface JcxfPartyMeetingMapper extends BaseMapper<JcxfPartyMeeting> {

    List<JcxfPartyMeeting> getMeetingByPartyMemberId(@Param("partyMemberId") String partyMemberId);
}

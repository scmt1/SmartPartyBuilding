package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfMeetingPojoUser;
import me.flyray.bsin.server.domain.jcxf.JcxfOrganizationLife;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@DS("jcxf")
public interface JcxfOrganizationLifeMapper extends BaseMapper<JcxfOrganizationLife> {
    Map<String, BigDecimal> queryMeetingClass(@Param(value = "ids") List<Integer> ids, @Param(value = "year") String year);

    List<Map<String, Integer>> queryMeetingClass3(@Param(value = "id") Integer id);

    Map<String, Integer> queryMeetingClass2(@Param(value = "Integer") Integer Integer, @Param(value = "year") String year);

    Integer queryOrganizationLife(@Param(value = "ids") List<Integer> ids, @Param(value = "year") String year,@Param(value = "type") String type);

    Map<String, Object> queryMeetingClassType(@Param(value = "date")String date, @Param(value = "deptId")String deptId);

    Map<String, Object> queryShouldMeetingClassTypeByThisMonth(@Param(value = "date")String date, @Param(value = "deptId")String deptId);

    Map<String, Object> queryActuallyMeetingClassTypeByThisMonth(@Param(value = "date")String date, @Param(value = "deptId")String deptId);


    Integer getLifeCountToRankQxAndOther(@Param(value = "startTime") String startTime,
                                                     @Param(value = "endTime") String endTime,
                                                     @Param(value = "deptIds") List<Integer> deptIds,
                                                     @Param(value = "shouldCount") Integer shouldCount);

    List<Map<String, BigDecimal>> getLifeCountToRankOfMeetingType(@Param(value = "startTime") String startTime,
                                                               @Param(value = "endTime") String endTime,
                                                               @Param(value = "meetingType") String meetingType,
                                                               @Param(value = "deptIds") List<Integer> deptIds);

    List<JcxfPartyMember> selectAllMeetingUserByDeptId(@Param("deptId") Long deptId, @Param("meetingId") Long meetingId, @Param("meetingType") Integer meetingType, @Param("isFlow") String isFlow);

    List<JcxfMeetingPojoUser> selectEndMeetingUserByDeptId(@Param("meetingId") Long meetingId, @Param("meetingType") Integer meetingType);

    List<JcxfMeetingPojoUser> selectEndMeetingFlowUserByDeptId(@Param("meetingId") Long meetingId);

    JcxfOrganizationLife getMeetingCountByDeptId(@Param("deptIds") List<Long> deptIds,@Param("meetingType") String meetingType);
}

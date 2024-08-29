package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzStudyView;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TzStudyViewMapper extends BaseMapper<TzStudyView> {

    List<Integer> getStudyIdsByPartMember(@Param(value = "parentIds") List<String> parentIds,
                                          @Param(value = "deptId") String deptId,
                                          @Param(value = "positionId") String positionId,
                                          @Param(value = "partyMemberId") String partyMemberId,
                                          @Param(value = "columnId") Integer columnId);

    List<Integer> getColumnIdsByPartMember(@Param(value = "parentIds") List<String> parentIds,
                                          @Param(value = "deptId") String deptId,
                                          @Param(value = "positionId") String positionId,
                                          @Param(value = "partyMemberId") String partyMemberId);

}

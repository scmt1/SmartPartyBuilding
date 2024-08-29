package me.flyray.bsin.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.SendRateDto;
import me.flyray.bsin.server.domain.TzMessage;
import me.flyray.bsin.server.domain.TzMessageDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Mapper
public interface TzMessageDetailMapper extends BaseMapper<TzMessageDetail> {
    Integer countByPhone(@Param("tenantId") String tenantId);

    Integer countByPhoneToday(@Param("tenantId") String tenantId);

    List<Map<Integer, String>> recentlySendCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tenantId") String tenantId);

    List<SendRateDto> recentlySendCountBySendStatus(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("tenantId") String tenantId);
}

package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzPartyHonor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS("dj_party")
public interface TzPartyHonorMapper extends BaseMapper<TzPartyHonor> {
    List<TzPartyHonor> deptHonorRank(@Param("deptIds") List<Long> deptIds, @Param("honorClass")Integer honorClass);
}

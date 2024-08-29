package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TDictData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TDictDataMapper extends BaseMapper<TDictData> {

    /**
     * 通过dictId和状态获取
     * @param dictId
     * @param status
     * @return
     */
    List<TDictData> findByDictIdAndStatusOrderBySortOrder(@Param(value = "dictId") Long dictId, @Param(value = "status") Integer status);

    /**
     * 通过dictId删除字典
     * @param dictId
     * @return
     */
    Boolean removeByDictId(Long dictId);

}

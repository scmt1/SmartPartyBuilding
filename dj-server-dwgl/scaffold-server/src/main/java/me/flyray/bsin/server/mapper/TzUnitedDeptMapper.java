package me.flyray.bsin.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzTradeUnion;
import me.flyray.bsin.server.domain.TzUnitedDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Mapper
public interface TzUnitedDeptMapper extends BaseMapper<TzUnitedDept> {

    List<Integer> selectChildrenIdsById(@Param(value = "id") Integer id);

}

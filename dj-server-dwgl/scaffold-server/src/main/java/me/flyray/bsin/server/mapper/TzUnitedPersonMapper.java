package me.flyray.bsin.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.MPJBaseMapper;
import me.flyray.bsin.server.domain.TzUnitedPerson;
import me.flyray.bsin.server.domain.TzXxqg;
import org.apache.ibatis.annotations.Mapper;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Mapper
public interface TzUnitedPersonMapper extends BaseMapper<TzUnitedPerson>, MPJBaseMapper<TzUnitedPerson> {
}

package me.flyray.bsin.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzUserTagUser;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import org.apache.ibatis.annotations.Mapper;

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
public interface TzUserTagUserMapper extends BaseMapper<TzUserTagUser> {
    List<JcxfPartyMemberVo> queryTableData(Integer tagId);
}

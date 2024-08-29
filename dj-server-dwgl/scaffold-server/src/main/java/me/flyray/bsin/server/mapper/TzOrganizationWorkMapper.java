package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.TzOrganizationWork;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:54
 * @description：SysDept数据访问
 */

public interface TzOrganizationWorkMapper extends BaseMapper<TzOrganizationWork> {

    List<TzOrganizationWork> selectPageList(@Param(value = "type") String type);
}

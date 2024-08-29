package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.TzTwoBestOneFirstTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TzTwoBestOneFirstTimeMapper extends BaseMapper<TzTwoBestOneFirstTime> {

    IPage<TzTwoBestOneFirstTime> getTwoBestOneFirstTimeByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<TzTwoBestOneFirstTime> queryWrapper,@Param("deptId") String deptId);
}

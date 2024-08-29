package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.TzTwoBestOneFirst;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DS("dj_party")
public interface TzTwoBestOneFirstMapper extends BaseMapper<TzTwoBestOneFirst> {

    IPage<TzTwoBestOneFirst> getTzTwoBestOneFirstByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<TzTwoBestOneFirst> queryWrapper);
}

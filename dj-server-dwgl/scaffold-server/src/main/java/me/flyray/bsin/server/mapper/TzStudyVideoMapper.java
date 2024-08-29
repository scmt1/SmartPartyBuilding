package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.TzStudyVideo;
import org.apache.ibatis.annotations.Param;

public interface TzStudyVideoMapper extends BaseMapper<TzStudyVideo> {
    IPage<TzStudyVideo> getStudyVideoByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<TzStudyVideo> queryWrapper);
}

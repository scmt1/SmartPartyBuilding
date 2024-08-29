package me.flyray.bsin.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.TzVote;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Mapper
public interface TzVoteMapper extends BaseMapper<TzVote> {

    IPage<TzVote> queryTzVoteByApp(@Param("page") Page pageData, @Param(Constants.WRAPPER) QueryWrapper<TzVote> voteQueryWrapper, @Param("deptId") Long deptId);
}

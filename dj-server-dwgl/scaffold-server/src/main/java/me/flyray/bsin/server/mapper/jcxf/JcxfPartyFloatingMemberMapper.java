package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyFloatingMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS(value = "jcxf")
public interface JcxfPartyFloatingMemberMapper extends BaseMapper<JcxfPartyFloatingMember> {

    IPage<JcxfPartyFloatingMember> getPartyFloatingMemberByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfPartyFloatingMember> queryWrapper);

    List<JcxfPartyFloatingMember> getPartyFloatingListByPartyMemberId(@Param("partyMemberId") String partyMemberId);
}

package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.jcxf.JcxfDevelopParty;
import me.flyray.bsin.server.domain.vo.JcxfDevelopPartyMemberVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@DS(value = "jcxf")
public interface JcxfDevelopPartyMapper extends BaseMapper<JcxfDevelopParty> {

    IPage<JcxfDevelopParty> getJcxfDevelopPartyByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfDevelopParty> queryWrapper);

    JcxfDevelopPartyMemberVo getJcxfDevelopPartyById(@Param("id") Long id);
}

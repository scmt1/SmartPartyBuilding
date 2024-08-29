package me.flyray.bsin.server.mapper.jcxf;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMemberTransfer;
import org.apache.ibatis.annotations.Param;

@DS(value = "jcxf")
public interface JcxfPartyMemberTransferMapper extends BaseMapper<JcxfPartyMemberTransfer> {

    IPage<JcxfPartyMemberTransfer> getJcxfPartyMemberTransferByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfPartyMemberTransfer> queryWrapper);
}

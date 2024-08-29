package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfSysUser;

@DS("jcxf")
public interface JcxfSysUserMapper extends BaseMapper<JcxfSysUser> {
}

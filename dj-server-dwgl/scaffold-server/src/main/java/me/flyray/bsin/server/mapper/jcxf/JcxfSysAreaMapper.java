package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfSysArea;

@DS(value = "jcxf")
public interface JcxfSysAreaMapper extends BaseMapper<JcxfSysArea> {
}

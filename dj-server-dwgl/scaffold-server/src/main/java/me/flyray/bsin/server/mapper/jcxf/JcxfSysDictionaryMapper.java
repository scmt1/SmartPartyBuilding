package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDictionary;
import org.apache.ibatis.annotations.Param;

@DS(value = "jcxf")
public interface JcxfSysDictionaryMapper extends BaseMapper<JcxfSysDictionary> {

    JcxfSysDictionary getByCodeAndDetailName(@Param(value = "code") String code,@Param(value = "detailName")  String detailName);
}

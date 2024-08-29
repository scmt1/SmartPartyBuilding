package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@DS("jcxf")
public interface JcxfSJKBMapper {

    List<Map<String, Object>> partMeeting(@Param("year")String year, @Param("payMonth")String payMonth);

    List<Map<String, Object>> qtpartMeeting(@Param("year")String year,@Param("payMonth")String month);

    List<Map<String, Object>> theoryMetting(@Param("payTimeStart")String payTimeStart,@Param("payTimeEnd")String payTimeEnd);

    List<Map<String, Object>> qttheoryMetting(@Param("payTimeStart")String payTimeStart,@Param("payTimeEnd")String payTimeEnd);

}

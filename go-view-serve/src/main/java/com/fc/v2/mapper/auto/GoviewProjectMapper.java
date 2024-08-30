package com.fc.v2.mapper.auto;

import com.fc.v2.model.auto.GoviewProject;
import com.fc.v2.model.auto.GoviewProjectExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 项目表 GoviewProjectMapper
 * @author fuce_自动生成
 * @email ${email}
 * @date 2022-05-18 21:43:25
 */
public interface GoviewProjectMapper {
      	   	      	      	      	      	      	      	      	      
    long countByExample(GoviewProjectExample example);

    int deleteByExample(GoviewProjectExample example);
		
    int deleteByPrimaryKey(String id);
		
    int insert(GoviewProject record);

    int insertSelective(GoviewProject record);

    List<GoviewProject> selectByExample(GoviewProjectExample example);
		
    GoviewProject selectByPrimaryKey(String id);
		
    int updateByExampleSelective(@Param("record") GoviewProject record, @Param("example") GoviewProjectExample example);

    int updateByExample(@Param("record") GoviewProject record, @Param("example") GoviewProjectExample example); 
		
    int updateByPrimaryKeySelective(GoviewProject record);

    int updateByPrimaryKey(GoviewProject record);
  	  	
}
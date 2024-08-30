package com.fc.v2.mapper.auto;

import com.fc.v2.model.auto.GoviewProjectData;
import com.fc.v2.model.auto.GoviewProjectDataExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 项目数据关联表 GoviewProjectDataMapper
 * @author fuce_自动生成
 * @email ${email}
 * @date 2022-05-20 00:30:49
 */
public interface GoviewProjectDataMapper {
      	   	      	      	      	      	      
    long countByExample(GoviewProjectDataExample example);

    int deleteByExample(GoviewProjectDataExample example);
		
    int deleteByPrimaryKey(String id);
		
    int insert(GoviewProjectData record);

    int insertSelective(GoviewProjectData record);

    List<GoviewProjectData> selectByExample(GoviewProjectDataExample example);
		
    GoviewProjectData selectByPrimaryKey(String id);
		
    int updateByExampleSelective(@Param("record") GoviewProjectData record, @Param("example") GoviewProjectDataExample example);

    int updateByExample(@Param("record") GoviewProjectData record, @Param("example") GoviewProjectDataExample example); 
		
    int updateByPrimaryKeySelective(GoviewProjectData record);

    int updateByPrimaryKey(GoviewProjectData record);
    
    List<GoviewProjectData> selectByExampleWithBLOBs(GoviewProjectDataExample example);
  	  	
}
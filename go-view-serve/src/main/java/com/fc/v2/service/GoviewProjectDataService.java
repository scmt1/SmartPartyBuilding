package com.fc.v2.service;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.hutool.core.util.StrUtil;
import com.fc.v2.common.base.BaseService;
import com.fc.v2.common.support.ConvertUtil;
import com.fc.v2.mapper.auto.GoviewProjectDataMapper;
import com.fc.v2.model.auto.GoviewProjectData;
import com.fc.v2.model.auto.GoviewProjectDataExample;
import com.fc.v2.model.custom.Tablepar;
import com.fc.v2.satoken.SaTokenUtil;
import com.fc.v2.util.SnowflakeIdWorker;

/**
 * 项目数据关联表 GoviewProjectDataService
 * @Title: GoviewProjectDataService.java 
 * @Package com.fc.v2.service 
 * @author fuce_自动生成
 * @email ${email}
 * @date 2022-05-20 00:30:49  
 **/
@Service
public class GoviewProjectDataService implements BaseService<GoviewProjectData, GoviewProjectDataExample>{
	@Autowired
	private GoviewProjectDataMapper goviewProjectDataMapper;
	
      	   	      	      	      	      	      	
	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	 public PageInfo<GoviewProjectData> list(Tablepar tablepar,GoviewProjectData goviewProjectData){
	        GoviewProjectDataExample testExample=new GoviewProjectDataExample();
			//搜索
			if(StrUtil.isNotEmpty(tablepar.getSearchText())) {//小窗体
	        	testExample.createCriteria().andLikeQuery2(tablepar.getSearchText());
	        }else {//大搜索
	        	testExample.createCriteria().andLikeQuery(goviewProjectData);
	        }
			//表格排序
			//if(StrUtil.isNotEmpty(tablepar.getOrderByColumn())) {
	        //	testExample.setOrderByClause(StringUtils.toUnderScoreCase(tablepar.getOrderByColumn()) +" "+tablepar.getIsAsc());
	        //}else{
	        //	testExample.setOrderByClause("id ASC");
	        //}
	        PageHelper.startPage(tablepar.getPage(), tablepar.getLimit());
	        List<GoviewProjectData> list= goviewProjectDataMapper.selectByExample(testExample);
	        PageInfo<GoviewProjectData> pageInfo = new PageInfo<GoviewProjectData>(list);
	        return  pageInfo;
	 }

	@Override
	public int deleteByPrimaryKey(String ids) {
				
			List<String> lista=ConvertUtil.toListStrArray(ids);
			GoviewProjectDataExample example=new GoviewProjectDataExample();
			example.createCriteria().andIdIn(lista);
			return goviewProjectDataMapper.deleteByExample(example);
			
				
	}
	
	
	@Override
	public GoviewProjectData selectByPrimaryKey(String id) {
				
			return goviewProjectDataMapper.selectByPrimaryKey(id);
				
	}

	
	@Override
	public int updateByPrimaryKeySelective(GoviewProjectData record) {
		return goviewProjectDataMapper.updateByPrimaryKeySelective(record);
	}
	
	
	/**
	 * 添加
	 */
	@Override
	public int insertSelective(GoviewProjectData record) {
		//添加雪花主键id
		record.setId(SnowflakeIdWorker.getUUID());
		record.setCreateUserId(SaTokenUtil.getUserId());
		record.setCreateTime(new Date());
		return goviewProjectDataMapper.insertSelective(record);
	}
	
	
	@Override
	public int updateByExampleSelective(GoviewProjectData record, GoviewProjectDataExample example) {
		
		return goviewProjectDataMapper.updateByExampleSelective(record, example);
	}

	
	@Override
	public int updateByExample(GoviewProjectData record, GoviewProjectDataExample example) {
		
		return goviewProjectDataMapper.updateByExample(record, example);
	}

	@Override
	public List<GoviewProjectData> selectByExample(GoviewProjectDataExample example) {
		
		return goviewProjectDataMapper.selectByExample(example);
	}

	
	@Override
	public long countByExample(GoviewProjectDataExample example) {
		
		return goviewProjectDataMapper.countByExample(example);
	}

	
	@Override
	public int deleteByExample(GoviewProjectDataExample example) {
		
		return goviewProjectDataMapper.deleteByExample(example);
	}
	
	/**
	 * 修改权限状态展示或者不展示
	 * @param goviewProjectData
	 * @return
	 */
	public int updateVisible(GoviewProjectData goviewProjectData) {
		return goviewProjectDataMapper.updateByPrimaryKeySelective(goviewProjectData);
	}
	
	
	/**
	 * 根据项目id查询项目数据
	 * @Title: getProjectid
	 * @author fuce
	 * @date 2022年5月24日
	 * @param @param projectId
	 * @param @return 参数
	 * @return GoviewProjectData 返回类型
	 * @throws
	 */
	public GoviewProjectData getProjectid(String projectId) {
		GoviewProjectDataExample example=new GoviewProjectDataExample();
		example.createCriteria().andProjectIdEqualTo(projectId);
		List<GoviewProjectData> list= goviewProjectDataMapper.selectByExampleWithBLOBs(example);
		if(list!=null&&list.size()>0) {
			return list.get(0);
		}
		return null;
	} 

}

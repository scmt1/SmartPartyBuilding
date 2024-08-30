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
import com.fc.v2.mapper.auto.GoviewProjectMapper;
import com.fc.v2.model.auto.GoviewProject;
import com.fc.v2.model.auto.GoviewProjectExample;
import com.fc.v2.model.custom.Tablepar;
import com.fc.v2.satoken.SaTokenUtil;
import com.fc.v2.util.SnowflakeIdWorker;

/**
 * 项目表 GoviewProjectService
 * @Title: GoviewProjectService.java 
 * @Package com.fc.v2.service 
 * @author fuce_自动生成
 * @email ${email}
 * @date 2022-05-18 21:43:25  
 **/
@Service
public class GoviewProjectService implements BaseService<GoviewProject, GoviewProjectExample>{
	@Autowired
	private GoviewProjectMapper goviewProjectMapper;


	/**
	 * 分页查询
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	 public PageInfo<GoviewProject> list(Tablepar tablepar,GoviewProject goviewProject){
	        GoviewProjectExample testExample=new GoviewProjectExample();
			//搜索
			if(StrUtil.isNotEmpty(tablepar.getSearchText())) {//小窗体
	        	testExample.createCriteria().andLikeQuery2(tablepar.getSearchText());
	        }else {//大搜索
	        	testExample.createCriteria().andLikeQuery(goviewProject);
	        }
		 	testExample.setTenantId(goviewProject.getTenantId());
		 	testExample.setOrgId(goviewProject.getOrgId());
			//表格排序
			//if(StrUtil.isNotEmpty(tablepar.getOrderByColumn())) {
	        //	testExample.setOrderByClause(StringUtils.toUnderScoreCase(tablepar.getOrderByColumn()) +" "+tablepar.getIsAsc());
	        //}else{
	        //	testExample.setOrderByClause("id ASC");
	        //}
	        PageHelper.startPage(tablepar.getPage(), tablepar.getLimit());
	        List<GoviewProject> list= goviewProjectMapper.selectByExample(testExample);
	        PageInfo<GoviewProject> pageInfo = new PageInfo<GoviewProject>(list);
	        return  pageInfo;
	 }

	@Override
	public int deleteByPrimaryKey(String ids) {

		List<String> lista=ConvertUtil.toListStrArray(ids);
		GoviewProjectExample example=new GoviewProjectExample();
		example.createCriteria().andIdIn(lista);
		GoviewProject goviewProject=new GoviewProject();
		goviewProject.setIsDelete(1);
		return goviewProjectMapper.updateByExampleSelective(goviewProject, example);


	}


	@Override
	public GoviewProject selectByPrimaryKey(String id) {

			return goviewProjectMapper.selectByPrimaryKey(id);

	}


	@Override
	public int updateByPrimaryKeySelective(GoviewProject record) {
		return goviewProjectMapper.updateByPrimaryKeySelective(record);
	}


	/**
	 * 添加
	 */
	@Override
	public int insertSelective(GoviewProject record) {

		//添加雪花主键id
		record.setId(SnowflakeIdWorker.getUUID());
		record.setCreateTime(new Date());
		record.setCreateUserId(SaTokenUtil.getUserId());
		return goviewProjectMapper.insertSelective(record);
	}


	@Override
	public int updateByExampleSelective(GoviewProject record, GoviewProjectExample example) {

		return goviewProjectMapper.updateByExampleSelective(record, example);
	}


	@Override
	public int updateByExample(GoviewProject record, GoviewProjectExample example) {

		return goviewProjectMapper.updateByExample(record, example);
	}

	@Override
	public List<GoviewProject> selectByExample(GoviewProjectExample example) {

		return goviewProjectMapper.selectByExample(example);
	}


	@Override
	public long countByExample(GoviewProjectExample example) {

		return goviewProjectMapper.countByExample(example);
	}


	@Override
	public int deleteByExample(GoviewProjectExample example) {

		return goviewProjectMapper.deleteByExample(example);
	}

	/**
	 * 修改权限状态展示或者不展示
	 * @param goviewProject
	 * @return
	 */
	public int updateVisible(GoviewProject goviewProject) {
		return goviewProjectMapper.updateByPrimaryKeySelective(goviewProject);
	}


}

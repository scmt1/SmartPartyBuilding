package com.fc.v2.service;

import com.fc.v2.mapper.auto.TenantMapper;
import com.fc.v2.model.auto.SysTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户表 TenantService
 * @Title: TenantService.java 
 * @Package com.fc.v2.service 
 * @author fuce_自动生成
 * @email ${email}
 * @date 2022-05-18 21:43:25  
 **/
@Service
public class TenantService{
	@Autowired
	private TenantMapper tenantMapper;

	/**
	 * 查询
	 * @return
	 */
	public List<SysTenant> list(){
		List<SysTenant> list= tenantMapper.getTenantList();
		return list;
	}
}

package com.fc.v2.controller.gen;

import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.model.custom.Tablepar;
import com.fc.v2.model.auto.GoviewProjectData;
import com.fc.v2.service.GoviewProjectDataService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * 项目数据关联表Controller
 * @ClassName: GoviewProjectDataController
 * @author fuce
 * @date 2022-05-20 00:30:49
 */
@Api(value = "项目数据关联表")
@Controller
@RequestMapping("/GoviewProjectDataController")
@CrossOrigin
public class GoviewProjectDataController extends BaseController{
	
	private String prefix = "gen/goviewProjectData";
	
	@Autowired
	private GoviewProjectDataService goviewProjectDataService;
	
	
	/**
	 * 项目数据关联表页面展示
	 * @param model
	 * @return String
	 * @author fuce
	 */
	@ApiOperation(value = "分页跳转", notes = "分页跳转")
	@GetMapping("/view")
	@SaCheckPermission("gen:goviewProjectData:view")
    public String view(ModelMap model)
    {
        return prefix + "/list";
    }
	
	/**
	 * list集合
	 * @param tablepar
	 * @param searchText
	 * @return
	 */
	//@Log(title = "项目数据关联表", action = "111")
	@ApiOperation(value = "分页跳转", notes = "分页跳转")
	@GetMapping("/list")
	@SaCheckPermission("gen:goviewProjectData:list")
	@ResponseBody
	public ResultTable list(Tablepar tablepar,GoviewProjectData goviewProjectData){
		PageInfo<GoviewProjectData> page=goviewProjectDataService.list(tablepar,goviewProjectData) ; 
		return pageTable(page.getList(),page.getTotal());
	}
	
	/**
     * 新增跳转
     */
	@ApiOperation(value = "新增跳转", notes = "新增跳转")
    @GetMapping("/add")
    public String add(ModelMap modelMap)
    {
        return prefix + "/add";
    }
	
    /**
     * 新增保存
     * @param 
     * @return
     */
	//@Log(title = "项目数据关联表新增", action = "111")
	@ApiOperation(value = "新增", notes = "新增")
	@PostMapping("/add")
	@SaCheckPermission("gen:goviewProjectData:add")
	@ResponseBody
	public AjaxResult add(GoviewProjectData goviewProjectData){
		int b=goviewProjectDataService.insertSelective(goviewProjectData);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	/**
	 * 项目数据关联表删除
	 * @param ids
	 * @return
	 */
	//@Log(title = "项目数据关联表删除", action = "111")
	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/remove")
	@SaCheckPermission("gen:goviewProjectData:remove")
	@ResponseBody
	public AjaxResult remove(String ids){
		int b=goviewProjectDataService.deleteByPrimaryKey(ids);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}
	
	
	/**
	 * 修改跳转
	 * @param id
	 * @param mmap
	 * @return
	 */
	@ApiOperation(value = "修改跳转", notes = "修改跳转")
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, ModelMap map)
    {
        map.put("GoviewProjectData", goviewProjectDataService.selectByPrimaryKey(id));

        return prefix + "/edit";
    }
	
	/**
     * 修改保存
     */
    //@Log(title = "项目数据关联表修改", action = "111")
	@ApiOperation(value = "修改保存", notes = "修改保存")
    @SaCheckPermission("gen:goviewProjectData:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(GoviewProjectData goviewProjectData)
    {
        return toAjax(goviewProjectDataService.updateByPrimaryKeySelective(goviewProjectData));
    }
    
    
    /**
	 * 修改状态
	 * @param record
	 * @return
	 */
    @PutMapping("/updateVisible")
	@ResponseBody
    public AjaxResult updateVisible(@RequestBody GoviewProjectData goviewProjectData){
		int i=goviewProjectDataService.updateVisible(goviewProjectData);
		return toAjax(i);
	}

    
    

	
}

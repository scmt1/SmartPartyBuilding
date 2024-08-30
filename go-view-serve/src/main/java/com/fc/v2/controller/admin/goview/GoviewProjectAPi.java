package com.fc.v2.controller.admin.goview;

import com.fc.v2.common.base.BaseController;
import com.fc.v2.common.conf.V2Config;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.domain.ResultTable;
import com.fc.v2.model.auto.*;
import com.fc.v2.model.custom.GoviewProjectVo;
import com.fc.v2.model.custom.MagicHttp;
import com.fc.v2.model.custom.Tablepar;
import com.fc.v2.service.GoviewProjectDataService;
import com.fc.v2.service.GoviewProjectService;
import com.fc.v2.service.SysFileService;
import com.fc.v2.service.TenantService;
import com.fc.v2.util.BeanUtils;
import com.fc.v2.util.SnowflakeIdWorker;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 项目表Controller
 * @ClassName: GoviewProjectController
 * @author fuce
 * @date 2022-05-18 21:43:25
 */
@Api(value = "前端项目表")
@Controller
@RequestMapping("/api/goview/project")
@CrossOrigin
public class GoviewProjectAPi extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(GoviewProjectAPi.class);

	@Autowired
	private GoviewProjectService goviewProjectService;
	@Autowired
	private GoviewProjectDataService goviewProjectDataService;
	@Autowired
	private SysFileService sysFileService;
	@Autowired
	private V2Config v2Config;
	@Autowired
	private TenantService tenantService;

	/**
	 * list集合
	 * @param tablepar
	 * @param searchText
	 * @return
	 */
	//@Log(title = "项目表", action = "111")
	@ApiOperation(value = "分页跳转", notes = "分页跳转")
	@GetMapping("/list")
	@SaCheckPermission("gen:goviewProject:list")
	@ResponseBody
	public ResultTable list(Tablepar tablepar,GoviewProject goviewProject){
		PageInfo<GoviewProject> page=goviewProjectService.list(tablepar,goviewProject) ;
		ResultTable resultTable=  pageTable(page.getList(),page.getTotal());
		resultTable.setCode(200);
		return resultTable;
	}


    /**
     * 新增保存
     * @param
     * @return
     */
	//@Log(title = "项目表新增", action = "111")
	@ApiOperation(value = "新增", notes = "新增")
	@PostMapping("/create")
	@SaCheckPermission("gen:goviewProject:add")
	@ResponseBody
	public AjaxResult add(@RequestBody GoviewProject goviewProject){
		int b=goviewProjectService.insertSelective(goviewProject);
		if(b>0){
			return AjaxResult.successData(200, goviewProjectService.selectByPrimaryKey(goviewProject.getId())).put("msg", "创建成功");
		}else{
			return error();
		}
	}

	/**
	 * 项目表删除
	 * @param ids
	 * @return
	 */
	//@Log(title = "项目表删除", action = "111")
	@ApiOperation(value = "删除", notes = "删除")
	@DeleteMapping("/delete")
	@SaCheckPermission("gen:goviewProject:remove")
	@ResponseBody
	public AjaxResult remove(String ids){
		int b=goviewProjectService.deleteByPrimaryKey(ids);
		if(b>0){
			return success();
		}else{
			return error();
		}
	}



	/**
     * 修改保存
     */
    //@Log(title = "项目表修改", action = "111")
	@ApiOperation(value = "修改保存", notes = "修改保存")
    @SaCheckPermission("gen:goviewProject:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(@RequestBody GoviewProject goviewProject)
    {
        return toAjax(goviewProjectService.updateByPrimaryKeySelective(goviewProject));
    }


	/**
	 * 项目重命名
	 * @author fuce
	 * @date 2022年5月20日
	 * @param @param goviewProject
	 * @param @return 参数
	 * @return AjaxResult 返回类型
	 * @throws
	 */
    //@Log(title = "项目表修改", action = "111")
	@ApiOperation(value = "项目重命名", notes = "项目重命名")
    @SaCheckPermission("gen:goviewProject:edit")
    @PostMapping("/rename")
    @ResponseBody
    public AjaxResult rename(@RequestBody GoviewProject goviewProject)
    {
		GoviewProject goviewProject2=new GoviewProject();
    	goviewProject2.setId(goviewProject.getId());
    	goviewProject2.setProjectName(goviewProject.getProjectName());
        return toAjax(goviewProjectService.updateByPrimaryKeySelective(goviewProject2));
    }


    //发布/取消项目状态
    @PutMapping("/publish")
	@ResponseBody
    public AjaxResult updateVisible(@RequestBody GoviewProject goviewProject){
    	if(goviewProject.getState()==-1||goviewProject.getState()==1) {
    		GoviewProject goviewProject2=new GoviewProject();
        	goviewProject2.setId(goviewProject.getId());
        	goviewProject2.setState(goviewProject.getState());
    		int i=goviewProjectService.updateVisible(goviewProject2);
    		return toAjax(i);
    	}
    	return error("警告非法字段");
	}


    /**
	 * 获取项目存储数据
	 * @param id 项目id
	 * @param mmap
	 * @return
	 */
	@ApiOperation(value = "获取项目存储数据", notes = "获取项目存储数据")
	@GetMapping("/getData")
	@ResponseBody
    public AjaxResult getData(String projectId, ModelMap map)
    {
		GoviewProject goviewProject= goviewProjectService.selectByPrimaryKey(projectId);

		GoviewProjectData blogText=goviewProjectDataService.getProjectid(projectId);
		if(blogText!=null) {
			GoviewProjectVo goviewProjectVo=new GoviewProjectVo();
			BeanUtils.copyBeanProp(goviewProjectVo, goviewProject);
			goviewProjectVo.setContent(blogText.getDataToStr());
			return AjaxResult.successData(200,goviewProjectVo).put("msg","获取成功");
		}
		return AjaxResult.successData(200, null).put("msg","无数据");

    }
	@ApiOperation(value = "保存项目数据", notes = "保存项目数据")
	@PostMapping("/save/data")
	@ResponseBody
	public AjaxResult saveData(GoviewProjectData data) {

		GoviewProject goviewProject= goviewProjectService.selectByPrimaryKey(data.getProjectId());
		if(goviewProject==null) {
			return AjaxResult.error("没有该项目ID");
		}
		GoviewProjectDataExample dataExample=new GoviewProjectDataExample();
		dataExample.createCriteria().andProjectIdEqualTo(goviewProject.getId());
		List<GoviewProjectData> list= goviewProjectDataService.selectByExample(dataExample);
		int i=0;
		if(list!=null&&list.size()>0) {
			GoviewProjectDataExample dataExample2=new GoviewProjectDataExample();
			dataExample2.createCriteria().andProjectIdEqualTo(goviewProject.getId());
			i=goviewProjectDataService.updateByExampleSelective(data, dataExample2);
		}else {
			i=goviewProjectDataService.insertSelective(data);
		}
		if(i>0) {
			return AjaxResult.success("数据保存成功");
		}
		return AjaxResult.error("获取保存失败");
	}

	/**
	 * 模拟请求
	 * @return
	 */
	@ApiOperation(value = "模拟请求", notes = "模拟请求")
	@PostMapping("/magicHttp")
	@ResponseBody
    public AjaxResult magicHttp(@RequestBody MagicHttp magicHttp){
    	if(magicHttp!=null){
    		try {
    			logger.info("后台接收前端模拟提交数据:"+JSONUtil.toJsonStr(magicHttp));
        		if(magicHttp.getRequestType().toUpperCase().equals("GET")){
        			HttpRequest  httpRequest=HttpUtil.createGet(magicHttp.getUrl());
        			if(magicHttp.getHead()!=null&&magicHttp.getHead().size()>0){
        				httpRequest.addHeaders(magicHttp.getHead());
        			}
        			if(StrUtil.isNotBlank(magicHttp.getCookie())){
        				httpRequest.cookie(magicHttp.getCookie());
        			}
        			if(StrUtil.isNotBlank(magicHttp.getBody())){
        				httpRequest.body(magicHttp.getBody());
        			}
        			if(magicHttp.getTimeout()!=null){
        				httpRequest.timeout(magicHttp.getTimeout());
        			}
        			if(magicHttp.getTimeout()==null){
        				httpRequest.timeout(30000);
        			}

        			String body= httpRequest.setFollowRedirects(true).execute().body();
        			return AjaxResult.successData(200,body);
        		}
        		if(magicHttp.getRequestType().toUpperCase().equals("POST")){

        			HttpRequest  httpRequest=HttpUtil.createPost(magicHttp.getUrl());
        			if(magicHttp.getHead()!=null&&magicHttp.getHead().size()>0){
        				httpRequest.addHeaders(magicHttp.getHead());
        			}
        			if(StrUtil.isNotBlank(magicHttp.getCookie())){
        				httpRequest.cookie(magicHttp.getCookie());
        			}
        			if(StrUtil.isNotBlank(magicHttp.getBody())){
        				httpRequest.body(magicHttp.getBody());
        			}
        			if(magicHttp.getTimeout()!=null){
        				httpRequest.timeout(magicHttp.getTimeout());
        			}
        			if(magicHttp.getTimeout()==null){
        				httpRequest.timeout(30000);
        			}
        	    	if(magicHttp.getForm()!=null&&magicHttp.getForm().size()>0){
        	    		httpRequest.form(magicHttp.getForm());
        	    	}
        	    	String body=httpRequest.setFollowRedirects(true).execute().body();
        			return AjaxResult.successData(200,body);
        		}
			} catch (Exception e) {
				return AjaxResult.successNullData("参数异常"+e.getMessage());
			}

    	}
    	return AjaxResult.successNullData("参数异常为null");
    }



	/**
	 * 上传文件
	 * @param object 文件流对象
	 * @param bucketName 桶名
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/upload")
	@ResponseBody
	public AjaxResult upload(@RequestBody MultipartFile object) throws IOException{
		String fileName = object.getOriginalFilename();
		String suffixName=".png";
		String mediaKey="";
		//文件名字
		String fileSuffixName="";
		if(fileName.lastIndexOf(".")!=-1) {//有后缀
			 suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
			 mediaKey=MD5.create().digestHex(fileName);
			 fileSuffixName=mediaKey+suffixName;
		}else {//无后缀
			//取得唯一id
			 mediaKey = MD5.create().digestHex(fileName+suffixName);
			 fileSuffixName=mediaKey+suffixName;
		}
		SysFile sysFile=sysFileService.selectByExamplefileName(fileSuffixName);

		 File desc = getAbsoluteFile(v2Config.getDefaultBaseDir(),fileSuffixName);
		 object.transferTo(desc);
		if(sysFile!=null){//修改
		}else{
			sysFile=new SysFile(SnowflakeIdWorker.getUUID(),  fileSuffixName, null, object.getSize(), object.getContentType(),"-", "-", new Date(),null, null, null);
			sysFileService.insertSelective(sysFile);
		}

		return AjaxResult.successData(200, sysFile);

	}


	private  final File getAbsoluteFile(String uploadDir, String filename) throws IOException
    {
        File desc = new File(uploadDir+File.separator + filename);

        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists())
        {
            desc.createNewFile();
        }
        return desc;
    }

	/**
	 * list集合
	 * @return
	 */
	@ApiOperation(value = "租户列表", notes = "租户列表")
	@GetMapping("/tenantList")
	@ResponseBody
	public ResultTable tenantList(){
		List<SysTenant> list = tenantService.list();
		return ResultTable.listDataTable(list);
	}
}

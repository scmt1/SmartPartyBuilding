package com.fc.v2.controller.admin.goview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fc.v2.common.conf.V2Config;
import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.mapper.custom.TsysUserDao;
import com.fc.v2.model.auto.TsysUser;
import com.fc.v2.model.auto.TsysUserExample;
import com.fc.v2.satoken.SaTokenUtil;
import com.fc.v2.service.SysUserService;
import com.fc.v2.util.ServletUtils;
import com.fc.v2.util.StringUtils;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/goview/sys")
@CrossOrigin
public class GoViewController {

	private static Logger logger = LoggerFactory.getLogger(GoViewController.class);
	@Autowired
	private TsysUserDao tsysUserDao;

	//系统用户
	@Autowired
	public SysUserService sysUserService;
	@Autowired
	private V2Config v2Config;

	/**
	 * 手机登录
	 * @param user
	 * @param redirectAttributes
	 * @param rememberMe
	 * @param request
	 * @return
	 * @author fuce
	 * @Date 2020年12月7日 上午12:54:28
	 */
	@ApiOperation(value = "手机登录", notes = "手机登录")
	@PostMapping("/login")
	@ResponseBody
	public AjaxResult APIlogin(@RequestBody TsysUser user,boolean rememberMe,HttpServletRequest request) {

		Boolean yz = true;
//		if (V2Config.getRollVerification()) {// 滚动验证
//			yz = true;
//		} else {// 图片验证
//			String scode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//			yz = StringUtils.isNotEmpty(scode) && StringUtils.isNotEmpty(code) && scode.equals(code);
//		}
		// 判断验证码
		if (yz) {
			String userName = user.getUsername();
			// 是否验证通过
			if (!StpUtil.isLogin()) {
				TsysUser queryUser = tsysUserDao.queryUserName(userName);
				// 各种校验
				if (queryUser == null) {
					logger.info("对用户[" + userName + "]进行登录验证..验证未通过,未知账户");
					return AjaxResult.error(500, "未知账户");
				}
				if (!SaSecureUtil.md5(user.getPassword()).equals(queryUser.getPassword())) {
					logger.info("对用户[" + userName + "]进行登录验证..验证未通过,错误的凭证");
					return AjaxResult.error(500, "用户名或密码不正确");
				}

				// 校验通过，开始登录
				StpUtil.login(queryUser.getId(), rememberMe);
				SaTokenUtil.setUser(queryUser);
				StpUtil.getTokenSession().set("ip", ServletUtils.getIP(request));
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("token",StpUtil.getTokenInfo());
				map.put("userinfo", queryUser);
				return AjaxResult.success().put("data",map);
			} else {
				if (StringUtils.isNotNull(SaTokenUtil.getUser())) {
					// 跳转到 get请求的登陆方法

					Map<String, Object> map=new HashMap<String, Object>();
					map.put("token",StpUtil.getTokenInfo());
					map.put("userinfo", SaTokenUtil.getUser());

					return AjaxResult.success().put("data",map);
				} else {
					return AjaxResult.error(500, "未知账户");
				}
			}
		} else {
			return AjaxResult.error(500, "验证码不正确!");
		}

	}

	/**
	 * 注册
	 * @Title: register
	 * @author fuce
	 * @date 2022年5月19日
	 * @param  参数
	 * @return void 返回类型
	 * @throws
	 */
	@PostMapping("/register")
	@ResponseBody
	public AjaxResult register(@RequestBody TsysUser tsysUser) {
		TsysUserExample example=new TsysUserExample();
		example.createCriteria().andUsernameEqualTo(tsysUser.getUsername());
		List<TsysUser> list = sysUserService.selectByExample(example);
		if(list.size()>0) {
			return AjaxResult.success("用户名重复");
		}
		int b=sysUserService.insertUserRoles(tsysUser,"687205030654251008");
		if(b>0){
			return AjaxResult.success("注册成功");
		}else{
			return AjaxResult.error("注册失败");
		}
	}






	/**
	 * 退出登陆
	 *
	 * @return
	 */
	@ApiOperation(value = "退出登陆", notes = "退出登陆")
	@GetMapping("/logout")
	@ResponseBody
	public AjaxResult LoginOut(HttpServletRequest request, HttpServletResponse response) {
		// 在这里执行退出系统前需要清空的数据
		// 注销
		StpUtil.logout();
		return AjaxResult.success().put("msg", "退出成功");
	}

	@ApiOperation(value = "获取oss信息", notes = "获取oss信息")
	@GetMapping("/getOssInfo")
	@ResponseBody
	public AjaxResult OssInfo(HttpServletRequest request) {
		Map<String, String> ossinfo=new HashMap<String, String>();
		ossinfo.put("bucketURL",v2Config.getUploadImgUrl());
		return AjaxResult.successData(200, ossinfo).put("msg", "返回成功");

	}



}

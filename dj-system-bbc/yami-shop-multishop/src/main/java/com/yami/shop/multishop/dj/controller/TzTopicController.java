package com.yami.shop.multishop.dj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qiniu.util.Base64;
import com.yami.shop.bean.constant.CommonConstant;
import com.yami.shop.bean.dto.PartyMemberDto;
import com.yami.shop.bean.model.TDict;
import com.yami.shop.bean.model.TDictData;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzSysDept;
import com.yami.shop.bean.model.dj.TzTopic;
import com.yami.shop.bean.model.dj.TzTopicDetail;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.ImportExeclUtil;
import com.yami.shop.dj.service.ITzTopicService;
import com.yami.shop.multishop.uitls.FileUtil;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import org.apache.catalina.security.SecurityUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 **/
@RestController
@Api(tags = " topic数据接口")
@RequestMapping("/multi/tzTopic")
@Transactional
public class TzTopicController {
    @Autowired
    private ITzTopicService tzTopicService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;

    /**
     * 功能描述：新增topic数据
     *
     * @param tzTopic 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增topic数据")
    @PostMapping("addTzTopic")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addTzTopic(@RequestBody TzTopic tzTopic) {
        try {
            tzTopic.setDelFlag(0);
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            Integer deptId = Integer.valueOf(shopEmployee.getDeptId());
            tzTopic.setDeptId(deptId);
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            String userId = shopEmployee.getUserId();
            if (tzTopic.getId() != null) {
                tzTopic.setStatus(0);
                tzTopic.setUpdateId(userId);
                tzTopic.setUpdateBy(username);
                tzTopic.setUpdateTime(new Date());
                boolean updateById = tzTopicService.updateById(tzTopic);
                if (updateById) {
                    return ServerResponseEntity.success(updateById);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            } else {
                tzTopic.setStatus(0); //状态为已新建
                tzTopic.setCreateId(userId);
                tzTopic.setCreateBy(username);
                tzTopic.setCreateTime(new Date());
                boolean res = tzTopicService.save(tzTopic);
                if (res) {
                    return ServerResponseEntity.success(res);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("保存失败");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }



    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除topic数据")
    @GetMapping("deleteTzTopic")
    public ServerResponseEntity<Object> deleteTzTopic(@RequestParam String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzTopic> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_topic.del_flag", 1).in("tz_topic.id", ids);
            boolean res = tzTopicService.update(updateWrapper);
            //boolean res = tzTopicService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取topic数据")
    @GetMapping("getTzTopic")
    public ServerResponseEntity<Object> getTzTopic(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzTopic res = tzTopicService.getById(id);
            if (res != null) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询topic数据")
    @GetMapping("queryTzTopicList")
    public ServerResponseEntity<Object> queryTzTopicList(TzTopic tzTopic, SearchVo searchVo, PageVo pageVo) {
        Map<String,Object> map= new HashMap<>();
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        Integer type = shopEmployee.getType();
        if (type == 0) {
            tzTopic.setStatus(null);
            map.put("isAdmin",true);
        } else {
            tzTopic.setStatus(2);
            map.put("isAdmin",false);
        }
        try {
            tzTopic.setDelFlag(0);
            IPage<TzTopic> result = tzTopicService.queryTzTopicListByPage(tzTopic, searchVo, pageVo);
            map.put("result",result);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：根据主键来发布专题
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来发布专题")
    @GetMapping("postIt")
    public ServerResponseEntity<Object> postIt(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<TzTopic> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic.id",id);
            queryWrapper.eq("tz_topic.del_flag",0);
            TzTopic tzTopic = tzTopicService.getOne(queryWrapper);
            tzTopic.setStatus(2);
            tzTopic.setPostTime(new Date());
            boolean updateById = tzTopicService.updateById(tzTopic);
            if (updateById) {
                return ServerResponseEntity.success("发布成功");
            } else {
                return ServerResponseEntity.showFailMsg("发布失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }




}

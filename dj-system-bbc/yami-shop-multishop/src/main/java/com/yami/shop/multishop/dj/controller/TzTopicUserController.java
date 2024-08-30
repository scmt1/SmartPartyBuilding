package com.yami.shop.multishop.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.dj.TzTopicDetail;
import com.yami.shop.bean.model.dj.TzTopicScore;
import com.yami.shop.bean.model.dj.TzTopicUser;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzTopicDetailService;
import com.yami.shop.dj.service.ITzTopicScoreService;
import com.yami.shop.dj.service.ITzTopicUserService;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

//import org.apache.catalina.security.SecurityUtil;

/**
 * @author
 **/
@RestController
@Api(tags = " tzTopicUser数据接口")
@RequestMapping("/multi/tzTopicUser")
@Transactional
public class TzTopicUserController {
    @Autowired
    private ITzTopicDetailService tzTopicDetailService;
    @Autowired
    private ITzTopicUserService tzTopicUserService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;


    /**
     * 功能描述：新增topicDeatil数据
     *
     * @param tzTopicUser 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增tzTopicUser数据")
    @PostMapping("addTzTopicUser")
    public ServerResponseEntity<Object> addTzTopicUser(@RequestBody TzTopicUser tzTopicUser,String topicUserId) {
        try {
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();

            tzTopicUser.setCreateBy(username);
            tzTopicUser.setDelFlag(0);
            tzTopicUser.setCreateTime(new Date());
            boolean res = tzTopicUserService.save(tzTopicUser);
            if (res) {
                return ServerResponseEntity.success(tzTopicUser);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
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
    @ApiOperation("根据主键来删除topicDeatil数据")
    @GetMapping("deleteTzTopicDetail")
    public ServerResponseEntity<Object> deleteTzTopicDetail(@RequestParam String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzTopicDetail> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_topic_detail.del_flag", 1).in("tz_topic_detail.id", ids);
            boolean res = tzTopicDetailService.update(updateWrapper);
            //boolean res = tzTopicDetailService.removeByIds(Arrays.asList(ids));
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
     * 功能描述：检查该用户是否已经答题过
     *
     * @param topicId 主键
     * @return 返回获取结果
     */
    @ApiOperation("检查该用户是否已经答题过")
    @GetMapping("checkDoIt")
    public ServerResponseEntity<Object> checkDoIt(@RequestParam(name = "topicId") String topicId) {
        if (StringUtils.isBlank(topicId)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
        try {
            QueryWrapper<TzTopicUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic_user.del_flag",0);
            queryWrapper.eq("tz_topic_user.create_by",username);
            queryWrapper.eq("tz_topic_user.topic_id",topicId);
            List<TzTopicUser> list = tzTopicUserService.list(queryWrapper);
            if (list != null&&list.size()>0) {
                return ServerResponseEntity.success(true);
            } else {
                return ServerResponseEntity.success(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：根据topicId获取TzTopicUser的id
     *
     * @param topicId 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据topicId获取TzTopicUser的id")
    @GetMapping("getInfo")
    public ServerResponseEntity<Object> getInfo(@RequestParam(name = "topicId") String topicId) {
        if (StringUtils.isBlank(topicId)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
        try {
            QueryWrapper<TzTopicUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic_user.del_flag",0);
            queryWrapper.eq("tz_topic_user.create_by",username);
            queryWrapper.eq("tz_topic_user.topic_id",topicId);
            List<TzTopicUser> list = tzTopicUserService.list(queryWrapper);
            if (list != null&&list.size()>0) {
                return ServerResponseEntity.success(list);
            } else {
                return ServerResponseEntity.success("查询失败");
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
    @ApiOperation("分页查询topicDeatil数据")
    @GetMapping("queryTzTopicDetailList")
    public ServerResponseEntity<Object> queryTzTopicDetailList(TzTopicDetail tzTopicDetail, SearchVo searchVo, PageVo pageVo) {
        try {
            IPage<TzTopicDetail> result = tzTopicDetailService.queryTzTopicDetailListByPage(tzTopicDetail, searchVo, pageVo);
            return ServerResponseEntity.success(result);
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
    @ApiOperation("根据topicId查询该专题的所有题目")
    @GetMapping("queryTzTopicDetailTestList")
    public ServerResponseEntity<Object> queryTzTopicDetailTestList(TzTopicDetail tzTopicDetail, SearchVo searchVo, PageVo pageVo) {
        try {
            List<TzTopicDetail> result = tzTopicDetailService.queryTzTopicDetailTestList(tzTopicDetail);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

}

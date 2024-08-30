package com.yami.shop.multishop.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.dj.TzTopicDetail;
import com.yami.shop.bean.model.dj.TzTopicScore;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzTopicDetailService;
import com.yami.shop.dj.service.ITzTopicScoreService;
import com.yami.shop.dj.service.ITzTopicService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.catalina.security.SecurityUtil;

/**
 * @author
 **/
@RestController
@Api(tags = " topicDeatil数据接口")
@RequestMapping("/multi/tzTopicRecord")
@Transactional
public class TzTopicScoreController {
    @Autowired
    private ITzTopicDetailService tzTopicDetailService;
    @Autowired
    private ITzTopicScoreService tzTopicScoreService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;

/*	@Autowired
	private
	 securityUtil;*/

    /**
     * 功能描述：新增topicDeatil数据
     *
     * @param tzTopicScoreList 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增topicDeatil数据")
    @PostMapping("addTzTopicScore")
    public ServerResponseEntity<Object> addTzTopicScore(@RequestBody List<TzTopicScore> tzTopicScoreList, String topicUserId) {
        try {
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();

            for (TzTopicScore tzTopicScore : tzTopicScoreList) {
                tzTopicScore.setCreateBy(username);
                tzTopicScore.setDelFlag(0);
                tzTopicScore.setCreateTime(new Date());
                //tzTopicScore.setTopicUserId(topicUserId);
            }
            boolean res = tzTopicScoreService.saveBatch(tzTopicScoreList);
            if (res) {
                return ServerResponseEntity.success("保存成功");
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
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取topicDeatil数据")
    @GetMapping("getTzTopicDetail")
    public ServerResponseEntity<Object> getTzTopicDetail(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzTopicDetail res = tzTopicDetailService.getById(id);
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
     * 功能描述：根据topicUserId来获取ttz_topic_score数据
     *
     * @param
     * @return 返回获取结果
     */
    @ApiOperation("根据topicUserId来获取tz_topic_score数据")
    @GetMapping("getDoIt")
    public ServerResponseEntity<Object> getDoIt(@RequestParam(name = "topicUserId") String topicUserId, String topicId) {
        if (StringUtils.isBlank(topicUserId)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
        try {
            QueryWrapper<TzTopicScore> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic_score.del_flag", 0);
            queryWrapper.eq("tz_topic_score.topic_user_id", topicUserId);
            queryWrapper.eq("tz_topic_score.create_by", username);
            List<TzTopicScore> list = tzTopicScoreService.list(queryWrapper);

            QueryWrapper<TzTopicDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("tz_topic_detail.del_flag", 0);
            wrapper.eq("tz_topic_detail.topic_id", topicId);
            //wrapper.eq("tz_topic_detail.create_by", username);
            List<TzTopicDetail> tzTopicDetails = tzTopicDetailService.list(wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("TzTopicScore", list);
            map.put("TzTopicDetail", tzTopicDetails);
            return ServerResponseEntity.success(map);

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

package com.yami.shop.multishop.dj.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.TzStudyVideo;
import com.yami.shop.bean.model.dj.TzTeacherInfo;
import com.yami.shop.bean.model.dj.TzVideoColumn;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzStudyVideoService;
import com.yami.shop.dj.service.ITzTeacherInfoService;
import com.yami.shop.dj.service.ITzVideoColumnService;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 **/
@RestController
@Api(tags = " TzTeacherInfo数据接口")
@RequestMapping("/multi/tzTeacherInfo")
public class TzTeacherInfoController {
    @Autowired
    private ITzStudyVideoService tzStudyVideoService;
    @Autowired
    private ITzTeacherInfoService iTzTeacherInfoService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
    @Autowired
    private AttachFileService attachFileService;

    /**
     * 功能描述：新增TzTeacherInfo数据
     *
     * @param  tzTeacherInfo
     * @return 返回新增结果
     */
    @ApiOperation("新增视频")
    @PostMapping("saveTeacherInfo")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> saveTeacherInfo(@RequestBody TzTeacherInfo tzTeacherInfo) {
        try {
            tzTeacherInfo.setDelFlag("0");
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            if (tzTeacherInfo.getId() != null) {
                tzTeacherInfo.setUpdateBy(username);
                tzTeacherInfo.setUpdateTime(new Date());
                boolean updateById = iTzTeacherInfoService.updateById(tzTeacherInfo);
                if (updateById) {
                    return ServerResponseEntity.success(updateById);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }

            } else {
                tzTeacherInfo.setCreateBy(username);
                tzTeacherInfo.setCreateTime(new Date());
                boolean res = iTzTeacherInfoService.save(tzTeacherInfo);
                if (res) {
                    if (res) {
                        return ServerResponseEntity.success(res);
                    } else {
                        return ServerResponseEntity.showFailMsg("保存失败");
                    }
                } else {
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
    @ApiOperation("根据主键来删除tzTeacherInfo数据")
    @PostMapping("deleteTeacherInfo")
    public ServerResponseEntity<Object> deleteTeacherInfo(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzTeacherInfo> queryWrapper = new UpdateWrapper<>();
            queryWrapper.set("tz_teacher_info.del_flag", 1).in("tz_teacher_info.id", ids);
            boolean update = iTzTeacherInfoService.update(queryWrapper);
            if (update) {
                    return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败！！！");
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
    @ApiOperation("根据主键来获取tzTeacherInfo数据")
    @GetMapping("tzTeacherInfo")
    public ServerResponseEntity<Object> tzTeacherInfo(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzTeacherInfo tzTeacherInfo = iTzTeacherInfoService.getById(id);
            if (tzTeacherInfo != null) {
                return ServerResponseEntity.success(tzTeacherInfo);
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
    @ApiOperation("分页查询TzTeacherInfo数据")
    @GetMapping("queryTzTeacherInfoList")
    public ServerResponseEntity<Object> queryTzTeacherInfoList(TzTeacherInfo tzTeacherInfo, SearchVo searchVo, PageVo pageVo) {
        Map<String, Object> map = new HashMap<>();
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        Integer type = shopEmployee.getType();
        try {
            tzTeacherInfo.setDelFlag("0");
            IPage<TzTeacherInfo> result = iTzTeacherInfoService.queryTzTeacherInfoList(tzTeacherInfo, searchVo, pageVo);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 图表数据统计
     * @return
     */
    @ApiOperation("图表数据统计")
    @GetMapping("getDataCalculate")
    public ServerResponseEntity<Object> getDataCalculate() {
        Map<String, Object> map = new HashMap<>();
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        QueryWrapper<TzTeacherInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("tz_teacher_info.del_flag",0);
        queryWrapper1.eq("tz_teacher_info.del_flag",0);
        try {
            Map<String,Object> stringObjectMap = iTzTeacherInfoService.getDataCalculate();
            return ServerResponseEntity.success(stringObjectMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
}

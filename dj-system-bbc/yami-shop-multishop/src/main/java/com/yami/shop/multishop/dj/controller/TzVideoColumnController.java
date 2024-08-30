package com.yami.shop.multishop.dj.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.TzStudyFile;
import com.yami.shop.bean.model.dj.TzTeacherInfo;
import com.yami.shop.bean.model.dj.TzTopic;
import com.yami.shop.bean.model.dj.TzVideoColumn;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzStudyFileService;
import com.yami.shop.dj.service.ITzTeacherInfoService;
import com.yami.shop.dj.service.ITzVideoColumnService;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
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
@Api(tags = " tzStudyFile数据接口")
@RequestMapping("/multi/tzVideoColumn")
@Transactional
public class TzVideoColumnController {
    @Autowired
    private ITzStudyFileService iTzStudyFileService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ITzVideoColumnService iTzVideoColumnService;
    @Autowired
    private ITzTeacherInfoService iTzTeacherInfoService;

    /**
     * @param tzVideoColumn
     * @return
     */
    @ApiOperation("新增学习文件")
    @PostMapping("addVideoColumn")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addVideoColumn(@RequestBody TzVideoColumn tzVideoColumn) {
        try {
            tzVideoColumn.setDelFlag("0");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            if (tzVideoColumn.getId() != null) {
                tzVideoColumn.setUpdateTime(new Date());
                tzVideoColumn.setUpdateBy(username);
                boolean updateById = iTzVideoColumnService.updateById(tzVideoColumn);
                if (updateById) {
                    return ServerResponseEntity.success(updateById);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            } else {
                tzVideoColumn.setCreateBy(username);
                tzVideoColumn.setCreateTime(new Date());
                boolean res = iTzVideoColumnService.save(tzVideoColumn);
                if (res) {
                    return ServerResponseEntity.success(res);
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
     * 功能描述：实现分页查询
     *
     * @return 返回获取结果
     */
    @ApiOperation("查询所有栏目")
    @GetMapping("firstQueryAll")
    public ServerResponseEntity<Object> firstQueryAll(String type) {

        try {
            QueryWrapper<TzVideoColumn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_video_column.type", type);
            List<TzVideoColumn> tzVideoColumns = iTzVideoColumnService.list(queryWrapper);
            return ServerResponseEntity.success(tzVideoColumns);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @return 返回获取结果
     */
    @ApiOperation("查询所有栏目")
    @GetMapping("queryAll")
    public ServerResponseEntity<Object> queryAll(String id, String type) {

        try {
            QueryWrapper<TzVideoColumn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_video_column.parent_id", id);
            queryWrapper.eq("tz_video_column.type", type);
            List<TzVideoColumn> tzVideoColumns = iTzVideoColumnService.list(queryWrapper);
            List<TzVideoColumn> list = new ArrayList<>();
            //list.add(one);
            this.recursionTests(Integer.parseInt(id), list, type);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    private List<TzVideoColumn> recursionTests(Integer parentId, List<TzVideoColumn> list, String type) {
        List<TzVideoColumn> one = iTzVideoColumnService.list(Wrappers.<TzVideoColumn>query().lambda().eq(TzVideoColumn::getParentId, parentId).eq(TzVideoColumn::getType, type));
        if (ObjectUtil.isEmpty(one)) {
            return list;
        } else {
            for (TzVideoColumn tzVideoColumn : one) {
                list.add(tzVideoColumn);
                this.recursionTests(tzVideoColumn.getId(), list, type);
            }
        }
        return list;
    }


    /**
     * 功能描述：根据主键来删除数据
     *
     * @param id 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除deleteTStudyFile数据")
    @GetMapping("deleteVideoColumn")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> deleteVideoColumn(@RequestParam String id, String type) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            List<TzVideoColumn> list = new ArrayList<>();
            this.recursionTests(Integer.parseInt(id), list, type);
            if (list != null && list.size() > 0) {
                return ServerResponseEntity.showFailMsg("子节点不为空，暂不可删除");
            }
            QueryWrapper<TzTeacherInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_teacher_info.column_id", id).eq("tz_teacher_info.del_flag", 0);
            List<TzTeacherInfo> teacherInfoList = iTzTeacherInfoService.list(queryWrapper);
            Integer count = 0;
            boolean remove = true;
            if (teacherInfoList != null && teacherInfoList.size() > 0) {
                count = teacherInfoList.size();
                remove = iTzTeacherInfoService.remove(queryWrapper);
            }
            if (remove == false) {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
            boolean res = iTzVideoColumnService.removeById(id);
            if (res) {
                return ServerResponseEntity.success(count);
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
    @ApiOperation("根据主键来获取栏目视频数据")
    @GetMapping("getVideoColumn")
    public ServerResponseEntity<Object> getStudyFile(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzVideoColumn tzVideoColumn = iTzVideoColumnService.getById(id);
            if (tzVideoColumn != null) {
                return ServerResponseEntity.success(tzVideoColumn);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


}

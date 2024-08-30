package com.yami.shop.multishop.dj.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.TzStudyFile;
import com.yami.shop.bean.model.dj.TzStudyVideo;
import com.yami.shop.bean.model.dj.TzVideoColumn;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzStudyVideoService;
import com.yami.shop.dj.service.ITzVideoColumnService;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author
 **/
@RestController
@Api(tags = " TzStudyVideo数据接口")
@RequestMapping("/multi/tzStudyVideo")
public class TzStudyVideoController {
    @Autowired
    private ITzStudyVideoService tzStudyVideoService;
    @Autowired
    private ITzVideoColumnService iTzVideoColumnService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
    @Autowired
    private AttachFileService attachFileService;

    /**
     * 功能描述：新增TzStudyVideo数据
     *
     * @param models 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增视频")
    @PostMapping("saveVideo")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> saveVideo(@RequestBody Map<String, Object> models) {
        String json1 = JSON.toJSONString(models.get("image"));
        String json2 = JSON.toJSONString(models.get("video"));
        String json3 = JSON.toJSONString(models.get("tzStudyVideo"));
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            AttachFile image = JSON.parseObject(json1, AttachFile.class);
            AttachFile video = JSON.parseObject(json2, AttachFile.class);
            TzStudyVideo tzStudyVideo = JSON.parseObject(json3, TzStudyVideo.class);
            tzStudyVideo.setDelFlag(0);
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            String deptId = shopEmployee.getDeptId();
            if (StringUtils.isBlank(deptId)) {
                return ServerResponseEntity.showFailMsg("部门id为空，请联系管理员");
            }
            tzStudyVideo.setDeptId(deptId);
            if (tzStudyVideo.getId() != null) {
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", tzStudyVideo.getId());
                queryWrapper.eq("tz_attach_file.table_type", "tz_study_video");
                boolean remove = attachFileService.remove(queryWrapper);
                tzStudyVideo.setStatus("0");
                tzStudyVideo.setUpdateBy(username);
                tzStudyVideo.setUpdateTime(new Date());
                boolean updateById = tzStudyVideoService.updateById(tzStudyVideo);
                if (updateById) {
                    List<AttachFile> fileList = new ArrayList<>();
                    image.setForeignKey(tzStudyVideo.getId().toString());
                    image.setTableType("tz_study_video");
                    image.setType(1);
                    image.setUploadTime(new Date());
                    video.setForeignKey(tzStudyVideo.getId().toString());
                    video.setTableType("tz_study_video");
                    video.setType(2);
                    video.setUploadTime(new Date());
                    fileList.add(image);
                    fileList.add(video);
                    boolean save = attachFileService.saveBatch(fileList);
                    if (save) {
                        return ServerResponseEntity.success(updateById);
                    } else {
                        return ServerResponseEntity.showFailMsg("修改失败");
                    }
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }

            } else {
                tzStudyVideo.setStatus("0"); //状态为已新建
                tzStudyVideo.setCreateBy(username);
                tzStudyVideo.setCreateTime(new Date());
                boolean res = tzStudyVideoService.save(tzStudyVideo);
                if (res) {
                    if (tzStudyVideo.getId() == null) {
                        return ServerResponseEntity.showFailMsg("文件id为空，请联系管理员");
                    }
                    List<AttachFile> fileList = new ArrayList<>();
                    image.setForeignKey(tzStudyVideo.getId().toString());
                    image.setTableType("tz_study_video");
                    image.setType(1);
                    image.setUploadTime(new Date());
                    video.setForeignKey(tzStudyVideo.getId().toString());
                    video.setTableType("tz_study_video");
                    video.setType(2);
                    video.setUploadTime(new Date());
                    fileList.add(image);
                    fileList.add(video);
                    boolean save = attachFileService.saveBatch(fileList);
                    if (save) {
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

    ///**
    //* 功能描述：更新数据
    //* @param tzStudyVideo 实体
    //* @return 返回更新结果
    //*/
    //@ApiOperation("更新TzStudyVideo数据")
    //@PostMapping("updateTzStudyVideo")
    //public Result<Object> updateTzStudyVideo(@RequestBody TzStudyVideo tzStudyVideo){
    //	if (StringUtils.isBlank(tzStudyVideo.getId())) {
    //		return ResultUtil.error("参数为空，请联系管理员！！");
    //	}
    //	try {
    //		tzStudyVideo.setUpdateId(securityUtil.getCurrUser().getId());
    //		tzStudyVideo.setUpdateTime(new Date());
    //		boolean res = tzStudyVideoService.updateById(tzStudyVideo);
    //		if (res) {
    //			return ResultUtil.data(res, "修改成功");
    //		} else {
    //			return ResultUtil.data(res, "修改失败");
    //		}
    //	} catch (Exception e) {
    //		 e.printStackTrace();
    //		return ResultUtil.error("保存异常:" + e.getMessage());
    //	}
    //}
    //

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除TzStudyVideo数据")
    @PostMapping("deleteTzStudyVideo")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> deleteTzStudyVideo(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("tz_attach_file.foreign_key", ids);
            queryWrapper.eq("tz_attach_file.table_type", "tz_study_video");
            boolean remove = attachFileService.remove(queryWrapper);
            if (!remove) {
                return ServerResponseEntity.showFailMsg("删除失败！！！");
            }
            boolean res = tzStudyVideoService.removeByIds(Arrays.asList(ids));
            if (!res) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("删除失败！！！");
            }
            return ServerResponseEntity.success(res);

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
    @ApiOperation("根据主键来获取TzStudyVideo数据")
    @GetMapping("getTzStudyVideo")
    public ServerResponseEntity<Object> getTzStudyVideo(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzStudyVideo studyVideo = tzStudyVideoService.getById(id);
            if (studyVideo != null) {
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", studyVideo.getId());
                queryWrapper.eq("tz_attach_file.table_type", "tz_study_video");
                List<AttachFile> list = attachFileService.list(queryWrapper);
                Map<String, Object> map = new HashMap<>();
                for (AttachFile attachFile : list) {
                    if (attachFile.getType() == 2)
                        map.put("video", attachFile);
                    else if (attachFile.getType() == 1)
                        map.put("image", attachFile);
                }
                map.put("studyVideo", studyVideo);
                return ServerResponseEntity.success(map);
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
    @ApiOperation("分页查询TzStudyVideo数据")
    @GetMapping("queryTzStudyVideoList")
    public ServerResponseEntity<Object> queryTzStudyVideoList(TzStudyVideo tzStudyVideo, SearchVo searchVo, PageVo pageVo) {
        try {
            Integer columnId = tzStudyVideo.getColumnId();
            tzStudyVideo.setDelFlag(0);
            QueryWrapper<TzVideoColumn> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_video_column.parent_id", columnId);
            List<TzVideoColumn> list = new ArrayList<>();
            this.recursionTests(columnId, list, "1");

            List<Integer> colunmIds = new ArrayList<>();
            colunmIds.add(columnId); //递归后没有包含本级id，所以需要手动添加
            for (TzVideoColumn tzVideoColumn : list) {
                colunmIds.add(tzVideoColumn.getId());
            }
            IPage<TzStudyVideo> result = tzStudyVideoService.queryTzStudyVideoListByPage(tzStudyVideo, searchVo, pageVo, colunmIds);
            return ServerResponseEntity.success(result);
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

    @GetMapping("/postVideo")
    public ServerResponseEntity<Object> postVideo(@RequestParam("id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        try {
            QueryWrapper<TzStudyVideo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_video.id", id).eq("tz_study_video.del_flag", 0);
            TzStudyVideo tzStudyVideo = tzStudyVideoService.getOne(queryWrapper);
            if (tzStudyVideo == null) {
                return ServerResponseEntity.showFailMsg("数据为空，发布失败");
            }
            tzStudyVideo.setStatus("1");
            boolean updateById = tzStudyVideoService.updateById(tzStudyVideo);
            if (updateById) {
                return ServerResponseEntity.success(updateById);
            } else {
                return ServerResponseEntity.showFailMsg("发布失败");
            }
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("发布失败" + e.getMessage());
        }

    }

}

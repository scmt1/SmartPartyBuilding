package com.yami.shop.multishop.dj.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.TAttachment;
import com.yami.shop.bean.model.dj.TzStudyFile;
import com.yami.shop.bean.model.dj.TzTopic;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.ITzStudyFileService;
import com.yami.shop.dj.service.ITzTopicService;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
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
@RequestMapping("/multi/tzStudyFile")
@Transactional
public class TzStudyFileController {
    @Autowired
    private ITzStudyFileService iTzStudyFileService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;

    @Autowired
    private AttachFileService attachFileService;

    /**
     * @param models
     * @return
     */
    @ApiOperation("新增学习文件")
    @PostMapping("saveStudyFile")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addTzTopic(@RequestBody Map<String, Object> models) {
        String json1 = JSON.toJSONString(models.get("attachFile"));
        String json2 = JSON.toJSONString(models.get("tzStudyFile"));
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = simpleDateFormat.format(new Date());
            AttachFile attachFile = JSON.parseObject(json1, AttachFile.class);
            TzStudyFile tzStudyFile = JSON.parseObject(json2, TzStudyFile.class);
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            String deptId = shopEmployee.getDeptId();
            tzStudyFile.setDeptId(deptId);
            if (tzStudyFile.getId() != null) {
                QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_attach_file.foreign_key", tzStudyFile.getId());
                queryWrapper.eq("tz_attach_file.type", 3);
                queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
                boolean remove = attachFileService.remove(queryWrapper);
                if(!remove){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
                    tzStudyFile.setName(attachFile.getFileName());
                    tzStudyFile.setStatus("0");
                    tzStudyFile.setUpdateBy(username);
                    tzStudyFile.setUpdateTime(new Date());
                    boolean updateById = iTzStudyFileService.updateById(tzStudyFile);
                    if (updateById) {
                        attachFile.setForeignKey(tzStudyFile.getId().toString());
                        attachFile.setTableType("tz_study_file");
                        attachFile.setType(3);
                        attachFile.setUploadTime(new Date());
                        boolean save = attachFileService.save(attachFile);
                        if (save) {
                            return ServerResponseEntity.success(updateById);
                        } else {
                            return ServerResponseEntity.showFailMsg("修改失败");
                        }
                    } else {
                        return ServerResponseEntity.showFailMsg("修改失败");
                    }

            } else {
                tzStudyFile.setDelFlag("0");
                tzStudyFile.setName(attachFile.getFileName());
                tzStudyFile.setStatus("0"); //状态为已新建
                tzStudyFile.setCreateBy(username);
                tzStudyFile.setCreateTime(new Date());
                boolean res = iTzStudyFileService.save(tzStudyFile);
                if (res) {
                    if (tzStudyFile.getId() == null) {
                        return ServerResponseEntity.showFailMsg("文件id为空，请联系管理员");
                    }
                    attachFile.setForeignKey(tzStudyFile.getId().toString());
                    attachFile.setTableType("tz_study_file");
                    attachFile.setType(3);
                    attachFile.setUploadTime(new Date());
                    boolean save = attachFileService.save(attachFile);
                    if (save) {
                        return ServerResponseEntity.success(save);
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ServerResponseEntity.showFailMsg("修改失败");
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
     * 功能描述：分页查询学习文件
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询学习文件")
    @GetMapping("queryTzTStudyFile")
    public ServerResponseEntity<Object> queryTzTStudyFile(TzStudyFile tzStudyFile, SearchVo searchVo, PageVo pageVo) {
        Map<String, Object> map = new HashMap<>();
        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        String deptId = shopEmployee.getDeptId();
        tzStudyFile.setDeptId(deptId);
        try {
            IPage<TzStudyFile> result = iTzStudyFileService.queryTzTopicListByPage(tzStudyFile, searchVo, pageVo);
            map.put("result", result);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


     /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除deleteTStudyFile数据")
    @GetMapping("deleteTStudyFile")
    public ServerResponseEntity<Object> deleteTStudyFile(@RequestParam String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzTopic> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_topic.del_flag", 1).in("tz_topic.id", ids);

            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("tz_attach_file.foreign_key", ids);
            queryWrapper.eq("tz_attach_file.type", 3);
            queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
            boolean remove = attachFileService.remove(queryWrapper);
            if(!remove){
                return ServerResponseEntity.showFailMsg("删除失败");
            }
            //boolean res = tzTopicService.update(updateWrapper);
            boolean res = iTzStudyFileService.removeByIds(Arrays.asList(ids));
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
    @ApiOperation("根据主键来获取getStudyFile数据")
    @GetMapping("getStudyFile")
    public ServerResponseEntity<Object> getStudyFile(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            Map<String ,Object> map=new HashMap<>();
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_attach_file.foreign_key",id);
            queryWrapper.eq("tz_attach_file.type", 3);
            queryWrapper.eq("tz_attach_file.table_type", "tz_study_file");
            List<AttachFile> list = attachFileService.list(queryWrapper);
            TzStudyFile tzStudyFile = iTzStudyFileService.getById(id);
            if (tzStudyFile != null) {
                map.put("attachList",list);
                map.put("tzStudyFile",tzStudyFile);
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
     * 功能描述：根据主键来发布专题
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来发布学习文件")
    @GetMapping("postIt")
    public ServerResponseEntity<Object> postIt(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_file.id",id);
            queryWrapper.eq("tz_study_file.del_flag","0");
            TzStudyFile tzStudyFile = iTzStudyFileService.getOne(queryWrapper);
            tzStudyFile.setStatus("1");
            tzStudyFile.setPostTime(new Date());
            boolean updateById = iTzStudyFileService.updateById(tzStudyFile);
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

    /**
     * 功能描述：根据主键来取消发布学习文件
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来取消发布学习文件")
    @GetMapping("cancelPostIt")
    public ServerResponseEntity<Object> cancelPostIt(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<TzStudyFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_study_file.id",id);
            queryWrapper.eq("tz_study_file.del_flag","0");
            TzStudyFile tzStudyFile = iTzStudyFileService.getOne(queryWrapper);
            tzStudyFile.setStatus("0");
            tzStudyFile.setPostTime(null);
            boolean updateById = iTzStudyFileService.updateById(tzStudyFile);
            if (updateById) {
                return ServerResponseEntity.success("取消成功");
            } else {
                return ServerResponseEntity.showFailMsg("取消失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }



}

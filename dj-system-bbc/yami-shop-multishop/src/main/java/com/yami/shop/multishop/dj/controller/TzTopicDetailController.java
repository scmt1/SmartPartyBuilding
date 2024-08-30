package com.yami.shop.multishop.dj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yami.shop.bean.dto.PartyMemberDto;
import com.yami.shop.bean.dto.TopicDetailDto;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzTopic;
import com.yami.shop.bean.model.dj.TzTopicDetail;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.ImportExeclUtil;
import com.yami.shop.dj.service.ITzTopicDetailService;
//import org.apache.catalina.security.SecurityUtil;
import com.yami.shop.dj.service.ITzTopicService;
import com.yami.shop.multishop.uitls.FileUtil;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author
 **/
@RestController
@Api(tags = " topicDeatil数据接口")
@RequestMapping("/multi/tzTopicDetail")
@Transactional
public class TzTopicDetailController {
    @Autowired
    private ITzTopicDetailService tzTopicDetailService;
    @Autowired
    private ITzTopicService tzTopicService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
/*	@Autowired
	private
	 securityUtil;*/

    /**
     * 功能描述：新增topicDeatil数据
     *
     * @param tzTopicDetail 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增topicDeatil数据")
    @PostMapping("addTzTopicDetail")
    public ServerResponseEntity<Object> addTzTopicDetail(@RequestBody TzTopicDetail tzTopicDetail) {
        try {
            tzTopicDetail.setDelFlag(0);
            if (tzTopicDetail.getId() != null) {
                tzTopicDetail.setUpdateTime(new Date());
                boolean updateById = tzTopicDetailService.updateById(tzTopicDetail);
                if (updateById) {
                    return ServerResponseEntity.success(updateById);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            } else {
                tzTopicDetail.setCreateTime(new Date());
                boolean res = tzTopicDetailService.save(tzTopicDetail);
                if (res) {
                    TzTopic tzTopic = tzTopicService.getById(tzTopicDetail.getTopicId());
                    tzTopic.setStatus(1);
                    tzTopicService.updateById(tzTopic);
                    return ServerResponseEntity.success("保存成功");
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

    /**
     * 功能描述：检查是否已经添加有题
     *
     * @return 返回获取结果
     */
    @ApiOperation("检查是否已经添加有题")
    @GetMapping("checkIsAdd")
    public ServerResponseEntity<Object> queryTzTopicDetailTestList(String topicId) {
        try {
            QueryWrapper<TzTopicDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic_detail.topic_id", topicId);
            queryWrapper.eq("tz_topic_detail.del_flag", 0);
            List<TzTopicDetail> list = tzTopicDetailService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                return ServerResponseEntity.success(true);
            } else {
                return ServerResponseEntity.success(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("下载专项答题模板")
    @GetMapping("/downFormwork")
    public void downFormwork(HttpServletResponse response) throws IOException {
        try {
            tzTopicDetailService.downFormwork(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     */
    @ApiOperation("批量导入专题数据")
    @PostMapping("/topicImport")
    public ServerResponseEntity<Object> partyImport(@RequestParam(value = "file") MultipartFile multipartFile, String topicId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Long deptId = Long.valueOf(1);

        ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
        @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        Pattern pattern = Pattern.compile("-?[0-9]+(\\\\.[0-9]+)?");
        TopicDetailDto topicDetailDto = new TopicDetailDto();

        List<TopicDetailDto> list = ImportExeclUtil.readDateListT(wb, topicDetailDto, "sheet1", 3, 0);

        Map<String, Object> map = new HashMap<>();
        int index = 3;
        int errorCount = 0;
        List<String> errorList = new ArrayList<>();
        List<TzTopicDetail> tzTopicDetails = new ArrayList<>();
        for (TopicDetailDto detailDto : list) {

            TzTopicDetail tzTopicDetail = new TzTopicDetail();
            if (StringUtils.isBlank(detailDto.getQuestion())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：题目不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            tzTopicDetail.setQuestion(detailDto.getQuestion());

            if (StringUtils.isBlank(detailDto.getQuestionType())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：题型不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            } else {
                if (detailDto.getQuestionType().equals("选择题")) {
                    tzTopicDetail.setQuestionType("1");
                } else if (detailDto.getQuestionType().equals("判断题")) {
                    tzTopicDetail.setQuestionType("2");
                } else {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：题型不匹配");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
            }
            if (StringUtils.isNotBlank(detailDto.getItemRecord())) {
                Matcher m = pattern.matcher(detailDto.getItemRecord());
                boolean matches = m.matches();
                if (!matches) {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：单项分值必须是数字");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                } else {
                    Integer record = Integer.valueOf(detailDto.getItemRecord());
                    if (record <= 0) {
                        errorList.add("第" + index + "行导入出错！" + "错误详情：单项分值必须大于0");
                        index = index + 1;
                        errorCount = errorCount + 1;
                        continue;
                    }
                    tzTopicDetail.setItemRecord(2);
                }
            } else {
                //默认2分
                tzTopicDetail.setItemRecord(2);
            }

            if(tzTopicDetail.getQuestionType().equals("1")){
                if (StringUtils.isBlank(detailDto.getItemA())) {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：选择题选项A不能为空");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
                tzTopicDetail.setItemA(detailDto.getItemA());

                if (StringUtils.isBlank(detailDto.getItemB())) {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：选择题选项B不能为空");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
                tzTopicDetail.setItemB(detailDto.getItemB());

                if (StringUtils.isBlank(detailDto.getItemC())) {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：选择题选项C不能为空");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
                tzTopicDetail.setItemC(detailDto.getItemC());

                if (StringUtils.isBlank(detailDto.getItemD())) {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：选择题选项D不能为空");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
                tzTopicDetail.setItemD(detailDto.getItemD());
            }

            if (StringUtils.isBlank(detailDto.getAnswer())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：答案不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }else {
                if(tzTopicDetail.getQuestionType().equals("1")){
                    if(!detailDto.getAnswer().equals("A")&&!detailDto.getAnswer().equals("B")&&!detailDto.getAnswer().equals("C")&&!detailDto.getAnswer().equals("D")){
                        errorList.add("第" + index + "行导入出错！" + "错误详情：选择题答案只能为A、B、C、D");
                        index = index + 1;
                        errorCount = errorCount + 1;
                        continue;
                    }
                }else if(tzTopicDetail.getQuestionType().equals("2")){
                    if(!detailDto.getAnswer().equals("对")&&!detailDto.getAnswer().equals("错")){
                        errorList.add("第" + index + "行导入出错！" + "错误详情：选择题答案只能为对或错");
                        index = index + 1;
                        errorCount = errorCount + 1;
                        continue;
                    }
                }
                tzTopicDetail.setAnswer(detailDto.getAnswer());
            }


            tzTopicDetail.setAnalysis(detailDto.getAnalysis());

            tzTopicDetail.setCreateTime(new Date());
            tzTopicDetail.setDelFlag(0);
            tzTopicDetail.setTopicId(Integer.parseInt(topicId));

            tzTopicDetails.add(tzTopicDetail);
            index++;
        }
        int successCount = tzTopicDetails.size();
        map.put("zong", "导入成功" + successCount + "条，" + "导入失败" + errorCount + "条");
        map.put("errorList", errorList);
        if (tzTopicDetails.size() > 0) {
            boolean saveBatch = tzTopicDetailService.saveBatch(tzTopicDetails);
            if (saveBatch) {
                return ServerResponseEntity.success(map);
            } else {
                return ServerResponseEntity.showFailMsg("导入异常");
            }
        } else {
            return ServerResponseEntity.success(map);
        }

    }

    /**
     * 不同属性和字段相互转换
     *
     * @param type
     * @param row
     * @return
     */
    public String convertFiled(String type, Object row) {
        return null;
    }
}

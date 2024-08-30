package com.yami.shop.multishop.dj.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzStudyFile;
import com.yami.shop.bean.model.dj.TzStudyUser;
import com.yami.shop.bean.model.dj.TzTotalStudyUser;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.IPartyMemberService;
import com.yami.shop.dj.service.ITzStudyFileService;
import com.yami.shop.dj.service.ITzStudyUserService;
import com.yami.shop.dj.service.TzTotalStudyUserService;
import com.yami.shop.multishop.uitls.TokenKey;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.service.AttachFileService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Scope("prototype")
@RestController
@RequestMapping("/app/studyFile")
public class appStudyFileController {
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITzStudyFileService iTzStudyFileService;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ITzStudyUserService iTzStudyUserService;
    @Autowired
    private TzTotalStudyUserService tzTotalStudyUserService;
    private static String globalUserId;

    //设置全局变量globalUserId
    @PostConstruct
    public void init() {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader(TokenKey.HEADER);
        java.lang.String token = tokenStore.decryptToken(authorization);
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
              .get(TokenKey.TOKEN_KEY + token);
        globalUserId = userInfoInTokenBO.getUserId();
    }

    /**
     * 通过全局的用户id查询当前用户能看见的学习资料
     *
     * @return
     */
    @ApiOperation("通过全局的用户id查询当前用户能看见的学习资料")
    @GetMapping("queryStudyFile")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> queryStudyFile() {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        Long deptId = partyMember.getDeptId();
        if (partyMember == null || deptId == null) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        QueryWrapper<TzStudyFile> tzStudyFileQueryWrapper = new QueryWrapper<>();
        tzStudyFileQueryWrapper.eq("tz_study_file.status", 1).eq("tz_study_file.dept_id", deptId).eq("tz_study_file.del_flag", 0);
        List<TzStudyFile> studyFileList = iTzStudyFileService.list(tzStudyFileQueryWrapper);

        if (studyFileList == null || studyFileList.size() == 0) {
            return ServerResponseEntity.showFailMsg("暂无数据");
        }
        for (TzStudyFile tzStudyFile : studyFileList) {
            QueryWrapper<AttachFile> attachFileQueryWrapper = new QueryWrapper<>();
            attachFileQueryWrapper.eq("tz_attach_file.foreign_key", tzStudyFile.getId()).eq("tz_attach_file.table_type", "tz_study_file");
            List<AttachFile> list = attachFileService.list(attachFileQueryWrapper);
            tzStudyFile.setAttachFileList(list);
        }
        return ServerResponseEntity.success(studyFileList);
    }


    /**
     * 查询已发布的学习资料的的分页列表（仅限用户所在部门发布的）
     *
     * @param pageVo
     * @return
     */
    @ApiOperation("查询已发布的学习资料的的分页列表（仅限用户所在部门发布的）")
    @GetMapping("queryFilePageListByApp")
    public ServerResponseEntity<Object> queryFilePageListByApp(PageVo pageVo) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if (partyMember == null || partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("部门id为空，查询失败");
        }
        IPage<TzStudyFile> result = iTzStudyFileService.queryPageListByApp(partyMember.getDeptId(), pageVo);
        return ServerResponseEntity.success(result);
    }

    @ApiOperation("通过学习文件id查询对应的文件信息")
    @GetMapping("queryFileDetailByApp")
    public ServerResponseEntity<Object> queryFileDetailByApp(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        QueryWrapper<AttachFile> attachFileQueryWrapper = new QueryWrapper<>();
        attachFileQueryWrapper.eq("tz_attach_file.foreign_key", fileId).eq("tz_attach_file.table_type", "tz_study_file");
        List<AttachFile> list = attachFileService.list(attachFileQueryWrapper);
        QueryWrapper<TzStudyFile>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tz_study_file.id",fileId);
        TzStudyFile one = iTzStudyFileService.getOne(queryWrapper);
        one.setAttachFileList(list);
        return ServerResponseEntity.success(one);
    }
    @ApiOperation("根据学习id查询某个文件已学习人数")
    @GetMapping("getStudyCount")
    public ServerResponseEntity<Object> getStudyCount(String fileId) {
        if (StringUtils.isBlank(fileId)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        QueryWrapper<TzStudyUser>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tz_study_user.id",fileId).eq("tz_study_user.is_finish","1");
        int count = iTzStudyUserService.count(queryWrapper);
        return ServerResponseEntity.success(count);
    }

    /**
     * 通过全局的用户id,学习id和学习类型查询当前用户的学分学时
     *
     * @param studyId
     * @param type
     * @return
     */
    @ApiOperation("通过全局的用户id,学习id和学习类型查询当前用户的学分学时")
    @GetMapping("queryScoreAndHours")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> queryScoreAndHours(@RequestParam("studyId") Long studyId, @RequestParam("type") String type) {
        if (studyId == null && StringUtils.isBlank(type)) {
            return ServerResponseEntity.showFailMsg("学习id或者学习类型为空");
        }
        try {
            QueryWrapper<TzStudyUser> studyUserQueryWrapper = new QueryWrapper<>();
            studyUserQueryWrapper.eq("tz_study_user.id", globalUserId).eq("tz_study_user.study_id", studyId).eq("tz_study_user.type", type);
            TzStudyUser studyUserServiceOne = iTzStudyUserService.getOne(studyUserQueryWrapper);
            return ServerResponseEntity.success(studyUserServiceOne);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询失败" + e.getMessage());
        }
    }

    @ApiOperation("保存文件精神相关的学分")
    @PostMapping("addScore")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addScore(@RequestBody TzStudyUser tzStudyUser) {
        if (tzStudyUser.getStudyId() == null) {
            return ServerResponseEntity.showFailMsg("学习id为空");
        }
        try {
            tzStudyUser.setUserId(Long.valueOf(Integer.parseInt(globalUserId)));
            tzStudyUser.setType("1");
            boolean save;
            int i = tzTotalStudyUserService.saveOrUpdateScore(globalUserId, tzStudyUser.getStudyScore(), 0);
            if (i == 0) {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
            if (tzStudyUser.getId() == null) {
                tzStudyUser.setCreateTime(new Date());
                save = iTzStudyUserService.save(tzStudyUser);
            } else {
                tzStudyUser.setUpdateTime(new Date());
                save = iTzStudyUserService.updateById(tzStudyUser);
            }
            if (save) {
                return ServerResponseEntity.success(save);
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存失败" + e.getMessage());
        }
    }

    /**
     * 查询文件精神学习的已完成或待完成列表
     *
     * @param pageVo
     * @param isFinish
     * @return
     */
    @ApiOperation("查询文件精神学习的已完成或待完成列表")
    @GetMapping("queryStudyFileByType")
    public ServerResponseEntity<Object> queryStudyFileByType(PageVo pageVo, String isFinish) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        Long deptId = partyMember.getDeptId();
        if (partyMember == null || deptId == null) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        IPage<TzStudyFile> fileIPage = iTzStudyFileService.queryFileBaseCondition(pageVo, globalUserId, deptId, isFinish);
        List<TzStudyFile> records = fileIPage.getRecords();
        if(records!=null&&records.size()>0){
            for (TzStudyFile record : records) {
                if(StringUtils.isNotBlank(record.getIsFinish())&&record.getIsFinish().equals("1")){
                    record.setProgress(100);
                }
            }
        }
        return ServerResponseEntity.success(fileIPage);
    }


}

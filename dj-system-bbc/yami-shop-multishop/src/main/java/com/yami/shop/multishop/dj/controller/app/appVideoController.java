package com.yami.shop.multishop.dj.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzStudyFile;
import com.yami.shop.bean.model.dj.TzStudyUser;
import com.yami.shop.bean.model.dj.TzStudyVideo;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.IPartyMemberService;
import com.yami.shop.dj.service.ITzStudyUserService;
import com.yami.shop.dj.service.ITzStudyVideoService;
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
import java.util.List;

@RestController
@RequestMapping("/app/video")
@Scope("prototype")
public class appVideoController {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private ITzStudyVideoService studyVideoService;
    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private TzTotalStudyUserService tzTotalStudyUserService;
    @Autowired
    private ITzStudyUserService iTzStudyUserService;

    private static java.lang.String globalUserId;

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
     * 查询已发布的党史视频或者在线视频学习的分页列表（仅限用户所在部门发布的）
     * @param pageVo
     * @return
     */
    @ApiOperation("查询已发布的党史视频或者在线视频学习的分页列表（仅限用户所在部门发布的）")
    @GetMapping("queryPageListByApp")
    public ServerResponseEntity<Object> queryPageListByApp(PageVo pageVo,String type) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if (partyMember == null ||  partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("部门id为空，查询失败");
        }
        if (StringUtils.isBlank(type)){
            return ServerResponseEntity.showFailMsg("查询类型不能为空");
        }
        Integer columnId;
        //党史视频查询
        if(type.equals("1")){
            columnId = 22;
        }else {
            //在线视频查询
            columnId = 23;
        }
        IPage<TzStudyVideo> result=studyVideoService.queryPageListByApp(partyMember.getDeptId(),pageVo,columnId);
        return ServerResponseEntity.success(result);
    }
    @ApiOperation("保存党史视频或在线视频学习的学时学分")
    @PostMapping("addScoreAndHour")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addScoreAndHour(@RequestBody TzStudyUser tzStudyUser) {
        if (tzStudyUser.getStudyId() == null) {
            return ServerResponseEntity.showFailMsg("学习id为空");
        }
        if(StringUtils.isBlank(tzStudyUser.getType())){
            return ServerResponseEntity.showFailMsg("视频类型为空");
        }
        try {
            tzStudyUser.setUserId(Long.valueOf(Integer.parseInt(globalUserId)));
            tzStudyUser.setType(tzStudyUser.getType());
            boolean save;
            int i = tzTotalStudyUserService.saveOrUpdateScore(globalUserId, tzStudyUser.getStudyScore(), tzStudyUser.getStudyHours());
            if(i==0){
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
     * 通过视频id查询对应的视频信息
     * @param videoId
     * @return
     */
    @ApiOperation("通过视频id查询对应的视频信息")
    @GetMapping("queryVideoDetailByApp")
    public ServerResponseEntity<Object> queryVideoDetailByApp(String videoId) {
        if(StringUtils.isBlank(videoId)){
            return ServerResponseEntity.showFailMsg("视频id为空，请联系管理员");
        }
        QueryWrapper<AttachFile> attachFileQueryWrapper=new QueryWrapper<>();
        attachFileQueryWrapper.eq("tz_attach_file.foreign_key",videoId).eq("tz_attach_file.table_type","tz_study_video");
        List<AttachFile> list = attachFileService.list(attachFileQueryWrapper);
        return ServerResponseEntity.success(list);
    }

    /**
     * 查询党史视频学习的已完善或待完善列表
     * @param pageVo
     * @param isFinish
     * @return
     */
    @ApiOperation("查询党史视频学习的已完善或待完善列表")
    @GetMapping("queryStudyVideoByType")
    public ServerResponseEntity<Object> queryStudyVideoByType(PageVo pageVo, String isFinish) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        Long deptId = partyMember.getDeptId();
        if (partyMember == null || deptId == null) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        IPage<TzStudyVideo> fileIPage = studyVideoService.queryFileBaseCondition(pageVo, globalUserId, deptId, isFinish);
        return ServerResponseEntity.success(fileIPage);
    }
}

package com.yami.shop.multishop.dj.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.dto.dj.StudyUserDto;
import com.yami.shop.bean.dto.dj.TotalStudyUserDto;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.PartyMember;
import com.yami.shop.bean.model.dj.TzStudyUser;
import com.yami.shop.bean.model.dj.TzStudyVideo;
import com.yami.shop.bean.model.dj.TzTotalStudyUser;
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
@RequestMapping("/app/TotalStudyUser")
@Scope("prototype")
public class appTotalStudyUserController {

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

    private static String globalUserId;

    //设置全局变量globalUserId
    @PostConstruct
    public void init() {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader(TokenKey.HEADER);
        String token = tokenStore.decryptToken(authorization);
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
              .get(TokenKey.TOKEN_KEY + token);
        globalUserId = userInfoInTokenBO.getUserId();
    }

    /**
     * 查询用户总学分和总学时
     * @return
     */
    @ApiOperation("查询用户总学分和总学时")
    @GetMapping("queryTotalScoreAndHour")
    public ServerResponseEntity<Object> queryTotalScoreAndHour() {
        QueryWrapper<TzTotalStudyUser>queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("tz_total_study_user.user_id",globalUserId);
        TzTotalStudyUser one = tzTotalStudyUserService.getOne(queryWrapper);
        if(one==null){
            one=new TzTotalStudyUser();
            one.setTotalHour(0);
            one.setTotalScore(0);
            one.setCreateTime(new Date());
            one.setUserId(globalUserId);
            tzTotalStudyUserService.save(one);
        }
        return ServerResponseEntity.success(one);
    }
    /**
     * 获取用户总学分当前排名
     * @return
     */
    @ApiOperation("获取用户总学分当前排名")
    @GetMapping("getRank")
    public ServerResponseEntity<Object> getRank(){
        TzTotalStudyUser tzTotalStudyUser = tzTotalStudyUserService.getRank(globalUserId);
        Integer rank;
        if(tzTotalStudyUser!=null&&tzTotalStudyUser.getRanks()!=null){
            rank=tzTotalStudyUser.getRanks();
        }else {
            int count = tzTotalStudyUserService.count();
            rank=count;
        }
        return ServerResponseEntity.success(rank);
    }

    /**
     * 党员排名列表
     * @return
     */
    @ApiOperation("党员排名列表")
    @GetMapping("getPartyRank")
    public ServerResponseEntity<Object> getPartyRank(PageVo pageVo){
        IPage<TotalStudyUserDto> tzTotalStudyUser = tzTotalStudyUserService.getPartyRank(globalUserId,pageVo);
        return ServerResponseEntity.success(tzTotalStudyUser);
    }
    /**
     * 查看学分的时间、课程、得分等详细信息
     * @return
     */
    @ApiOperation("查看学分的时间、课程、得分等详细信息")
    @GetMapping("getDetailRecord")
    public ServerResponseEntity<Object> getDetailRecord(PageVo pageVo){
        IPage<StudyUserDto> studyUserDtoIPage = iTzStudyUserService.getDetailRecord(globalUserId,pageVo);
        return ServerResponseEntity.success(studyUserDtoIPage);
    }
}

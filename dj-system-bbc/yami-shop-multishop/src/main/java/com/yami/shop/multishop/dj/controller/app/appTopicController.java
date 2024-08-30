package com.yami.shop.multishop.dj.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.dj.*;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.*;
import com.yami.shop.multishop.uitls.TokenKey;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.manager.TokenStore;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 具体投票项 前端控制器
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Scope("prototype")
@RestController
@RequestMapping("/app/topic")
public class appTopicController {
    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.token.signKey:-mall4j--mall4j-}")
    public String tokenSignKey;


    @Autowired
    private ITzTopicUserService tzTopicUserService;
    @Autowired
    private ITzTopicService tzTopicService;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private ITzTopicDetailService tzTopicDetailService;
    @Autowired
    private ITzTopicScoreService tzTopicScoreService;
    @Autowired
    private TzTotalStudyUserService tzTotalStudyUserService;
    @Autowired
    private ITzStudyUserService iTzStudyUserService;

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplate redisTemplate;


    private static java.lang.String globalUserId;

    //设置全局变量globalUserId
    @PostConstruct
    public void init( ){
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader(TokenKey.HEADER);
        java.lang.String token = tokenStore.decryptToken(authorization);
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
              .get(TokenKey.TOKEN_KEY + token);
        globalUserId = userInfoInTokenBO.getUserId();
    }

    /**
     * 根据用户id（即党员id）查询已答题的列表
     */
    @ApiOperation("根据用户id（即党员id）查询已答题的列表")
    @GetMapping("queryDoTopicList")
    public ServerResponseEntity<Object> queryDoTopicList(String userId){
        if(StringUtils.isBlank(userId)){
            return ServerResponseEntity.showFailMsg("党员id为空，请联系管理员");
        }
        List<TzTopicUserAndTopicDto> result = tzTopicUserService.queryDoTopicList(userId);
        return ServerResponseEntity.success(result);
    }

    /**
     * 根据部门id和用户id（即党员id）查询已答题或者未答题的列表
     * @param userId
     * @param deptId
     * @param isDo
     * @param pageVo
     * @return
     */
    @ApiOperation("根据部门id和用户id（即党员id）查询已答题或者未答题的列表")
    @GetMapping("queryTopicList")
    public ServerResponseEntity<Object> queryTopicList(String userId, String deptId, Boolean isDo, PageVo pageVo){
        if(StringUtils.isBlank(userId)){
            return ServerResponseEntity.showFailMsg("党员id为空，请联系管理员");
        }
        if(StringUtils.isBlank(deptId)){
            return ServerResponseEntity.showFailMsg("部门id为空，请联系管理员");
        }

        IPage<TzTopic> result=tzTopicService.queryTopicList(userId,deptId,isDo,pageVo);
        return ServerResponseEntity.success(result);
    }

    /**
     * 功能描述：保存用户答题的记录
     *
     * @param tzTopicUser 实体
     * @return 返回新增结果
     */
    @ApiOperation("保存用户答题的记录")
    @PostMapping("addTzTopicUserAndScore")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addTzTopicUserAndScore(@RequestParam("tzTopicUser") TzTopicUser tzTopicUser,@RequestBody List<TzTopicScore> tzTopicScoreList) {
        try {
            QueryWrapper<PartyMember>queryWrapper=new QueryWrapper<>();
            queryWrapper.eq("party_member.id",tzTopicUser.getUserId()).eq("party_member.del_flag",0);
            PartyMember one = partyMemberService.getOne(queryWrapper);
            if(one==null||StringUtils.isBlank(one.getRealName())){
                return ServerResponseEntity.showFailMsg("用户信息异常，保存失败");
            }
            if(tzTopicUser.getTopicId()==null){
                return ServerResponseEntity.showFailMsg("topicId为空，保存失败");
            }
            tzTopicUser.setCreateBy(one.getRealName());
            tzTopicUser.setDelFlag(0);
            tzTopicUser.setCreateTime(new Date());
            QueryWrapper<TzTopic> tzTopicQueryWrapper=new QueryWrapper<>();
            tzTopicQueryWrapper.eq("tz_topic.id",tzTopicUser.getTopicId()).eq("tz_topic.status",2).eq("tz_topic.del_flag",0);
            TzTopic tzTopic = tzTopicService.getOne(tzTopicQueryWrapper);
            if(tzTopic==null||tzTopic.getRecords()==null){
                return ServerResponseEntity.showFailMsg("学分为空，保存失败");
            }
            //判断是否及格，不及格没学分
            if(tzTopicUser.getAchieveScore()>tzTopicUser.getTotalScore()*0.6){
                tzTopicUser.setStudyScore(tzTopic.getRecords());
                int i = tzTotalStudyUserService.saveOrUpdateScore(globalUserId, tzTopic.getRecords(), 0);
                if(i==0){
                    return ServerResponseEntity.showFailMsg("保存失败");
                }
            }else {
                tzTopicUser.setStudyScore(0);
                int i = tzTotalStudyUserService.saveOrUpdateScore(globalUserId, 0, 0);
                if(i==0){
                    return ServerResponseEntity.showFailMsg("保存失败");
                }
            }

            //先保存用户答题记录中间表
            boolean res = tzTopicUserService.save(tzTopicUser);
            if(!res){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("保存失败");
            }

                //保存成功后得到用户答题记录中间表的id，然后赋值给用户答题详细分数表
                String topicUserId = tzTopicUser.getId().toString();
                String userId = tzTopicUser.getUserId();
                for (TzTopicScore tzTopicScore : tzTopicScoreList) {
                    tzTopicScore.setCreateBy(one.getRealName());
                    tzTopicScore.setDelFlag(0);
                    tzTopicScore.setCreateTime(new Date());
                    tzTopicScore.setTopicUserId(topicUserId);
                    tzTopicScore.setUserId(userId);
                }
                // 保存用户答题详细分数表
                boolean saveBatch = tzTopicScoreService.saveBatch(tzTopicScoreList);
                if(!saveBatch){
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("保存失败");
                }

            TzStudyUser tzStudyUser=new TzStudyUser();
            tzStudyUser.setUserId(Long.valueOf(Integer.parseInt(globalUserId)));
            tzStudyUser.setType("4");
            tzStudyUser.setStudyScore(tzTopic.getRecords());
            tzStudyUser.setStudyId(Long.valueOf(tzTopicUser.getId()));
            tzStudyUser.setIsFinish("1");
            boolean save;
            if (tzStudyUser.getId() == null) {
                tzStudyUser.setCreateTime(new Date());
                save = iTzStudyUserService.save(tzStudyUser);
            } else {
                tzStudyUser.setUpdateTime(new Date());
                save = iTzStudyUserService.updateById(tzStudyUser);
            }
            if(!save){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("保存失败");
            }
            return ServerResponseEntity.success(save);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     *  功能描述：根据topicId查询该专题的所有题目
     * @param tzTopicDetail
     * @return
     */
    @ApiOperation("根据topicId查询该专题的所有题目")
    @GetMapping("queryTzTopicDetailTestList")
    public ServerResponseEntity<Object> queryTzTopicDetailTestList(TzTopicDetail tzTopicDetail) {
        try {
            List<TzTopicDetail> result = tzTopicDetailService.queryTzTopicDetailTestList(tzTopicDetail);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
    /**
     * 功能描述：根据专题id topicId和用户id userId获取用该专题的总分和所有题目，以及用户的实际得分合并用户的答题情况
     *
     * @param topicId 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据专题id topicId和用户id userId获取用该专题的总分和所有题目，以及用户的实际得分合并用户的答题情况")
    @GetMapping("getDoInfo")
    public ServerResponseEntity<Object> getDoInfo(@RequestParam(name = "topicId") String topicId,@RequestParam(name = "userId") String userId) {
        if (StringUtils.isBlank(topicId)||StringUtils.isBlank(userId)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx"+globalUserId);
        try {
            Map<String, Object> map = new HashMap<>();
            QueryWrapper<TzTopicUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_topic_user.del_flag",0);
            queryWrapper.eq("tz_topic_user.user_id",userId);
            queryWrapper.eq("tz_topic_user.topic_id",topicId);
            //查询用户记录表，正常情况同一个用户只会答题一次，所以取第一个就行
            List<TzTopicUser> list = tzTopicUserService.list(queryWrapper);
            if (list== null||list.size()==0) {
                return ServerResponseEntity.success("查询失败");
            }
            Integer topicUserId = list.get(0).getId();

            Integer totalRecords = list.get(0).getTotalScore();
            Integer records = list.get(0).getAchieveScore();
            QueryWrapper<TzTopicScore> tzTopicScoreQueryWrapper = new QueryWrapper<>();
            tzTopicScoreQueryWrapper.eq("tz_topic_score.del_flag", 0);
            tzTopicScoreQueryWrapper.eq("tz_topic_score.topic_user_id", topicUserId);
            tzTopicScoreQueryWrapper.eq("tz_topic_score.user_id", userId);
            //查询用户对应的每道题的分数以及对错情况
            List<TzTopicScore> topicScoreList = tzTopicScoreService.list(tzTopicScoreQueryWrapper);

            QueryWrapper<TzTopicDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("tz_topic_detail.del_flag", 0);
            wrapper.eq("tz_topic_detail.topic_id", topicId);
            //查询该专题一共有哪些试题
            List<TzTopicDetail> tzTopicDetails = tzTopicDetailService.list(wrapper);
            map.put("TzTopicScore", topicScoreList);
            map.put("TzTopicDetail", tzTopicDetails);
            map.put("records", records);
            map.put("totalRecords", totalRecords);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
}

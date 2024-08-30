package com.yami.shop.multishop.dj.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.dj.*;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.*;
import com.yami.shop.multishop.uitls.TokenKey;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.manager.TokenStore;
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
import java.util.*;

@Scope("prototype")
@RestController
@RequestMapping("/app/vote")
public class appVoteController {
    @Autowired
    private ITzVoteService tzVoteService;
    @Autowired
    private ITzVoteDetailService iTzVoteDetailService;

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ITzSysDeptService iTzSysDeptService;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private ITzVoteUserService iTzVoteUserService;
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
     * 功能描述：分页查询投票列表
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询投票列表")
    @GetMapping("queryTzVoteByApp")
    public ServerResponseEntity<Object> queryTzVoteByApp(SearchVo searchVo, PageVo pageVo) {
        try {
            QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
            partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
            PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
            Long deptId = partyMember.getDeptId();

            IPage<TzVote> result = tzVoteService.queryTzVoteByApp(deptId, searchVo, pageVo);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    @ApiOperation("根据投票id查询投票选项以及投票状态")
    @GetMapping("queryTzVoteById")
    public ServerResponseEntity<Object> queryTzVoteById(String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        try {
            boolean beforeVote = true;
            boolean isEnd = false;
            boolean display = true;
            Map<String, Object> map = new HashMap<>();

            //根据voteId和userId去查询用户投票记录表，如果为空，则证明用户还没投票
            QueryWrapper<TzVoteUser> voteUserQueryWrapper = new QueryWrapper<>();
            voteUserQueryWrapper.eq("tz_vote_user.vote_id", id).eq("tz_vote_user.user_id", globalUserId);
            List<TzVoteUser> voteUsers = iTzVoteUserService.list(voteUserQueryWrapper);
            // 根据voteId去查询投票表，看每个用户最多可以投几张票
            QueryWrapper<TzVote> voteQueryWrapper = new QueryWrapper<>();
            voteQueryWrapper.eq("tz_vote.id", id).eq("tz_vote.del_flag", 0);
            TzVote vote = tzVoteService.getOne(voteQueryWrapper);
            if (vote == null) {
                return ServerResponseEntity.showFailMsg("该投票已删除");
            }
            Date endDate = vote.getEndDate();
            if(new Date().getTime()<endDate.getTime()){
                isEnd = true;
            }
            Integer voteNumber = 1;
            // 投票次数如果没设置，默认为1
            if (vote.getNumberTimes() != null || vote.getNumberTimes() != 0) {
                voteNumber = vote.getNumberTimes();
            }
            //结束状态为未结束
            if(!isEnd){
                //如果用户投票记录表的记录数和投票表设置的投票数相等，则证明投票次数已用完,
                if (voteUsers != null && voteUsers.size() >= voteNumber) {
                    beforeVote = false; //状态设置为投票后
                    map.put("disabled", true);
                } else {
                    map.put("disabled", false);
                }
            }else {
                map.put("disabled", true);
            }
            /*QueryWrapper<TzVote> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_vote.id", id);
            TzVote one = tzVoteService.getOne(queryWrapper);*/
            if (vote == null) {
                return ServerResponseEntity.showFailMsg("查询异常");
            }
            String voteResult = vote.getVoteResult();

            QueryWrapper<TzVoteDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("tz_vote_detail.vote_id", id);
            List<TzVoteDetail> list = iTzVoteDetailService.list(detailQueryWrapper);
            //如果当前状态处于未投票，并且投票结果是投票后可见，那就把投票次数先置为0
            if (voteResult.equals("2") && beforeVote) {
                for (TzVoteDetail tzVoteDetail : list) {
                    display = false;
                    tzVoteDetail.setNumberVote(0);
                }
            }
            //如果当前状态处于未结束，并且投票结果是投票结束后可见，那就把投票次数先置为0
            if (voteResult.equals("3") && !isEnd) {
                for (TzVoteDetail tzVoteDetail : list) {
                    display = false;
                    tzVoteDetail.setNumberVote(0);
                }
            }
            //如果当前是投票结果不可见，那就把投票次数置为0
            if (voteResult.equals("4")) {
                for (TzVoteDetail tzVoteDetail : list) {
                    display = false;
                    tzVoteDetail.setNumberVote(0);
                }
            }
            map.put("vote", vote);
            map.put("display", display);
            map.put("voteDetailList", list);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 用户点击投票
     *
     * @param tzVote
     * @return
     */
    @ApiOperation("用户点击投票")
    @PostMapping("voteByApp")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> voteByApp(@RequestBody TzVote tzVote) {
        if (StringUtils.isBlank(tzVote.getVoteDetailId()) || tzVote.getId() == null) {
            return ServerResponseEntity.showFailMsg("投票失败，请联系管理员");
        }

        //根据voteId和userId去查询用户投票记录表，如果为空，则证明用户还没投票
        QueryWrapper<TzVoteUser> voteUserQueryWrapper = new QueryWrapper<>();
        voteUserQueryWrapper.eq("tz_vote_user.vote_id", tzVote.getId()).eq("tz_vote_user.user_id", globalUserId);
        List<TzVoteUser> voteUsers = iTzVoteUserService.list(voteUserQueryWrapper);
        // 根据voteId去查询投票表，看每个用户最多可以投几张票
        QueryWrapper<TzVote> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("tz_vote.id", tzVote.getId()).eq("tz_vote.del_flag", 0);
        TzVote vote = tzVoteService.getOne(voteQueryWrapper);
        if (vote == null) {
            return ServerResponseEntity.showFailMsg("该投票已删除");
        }
        Integer voteNumber = 1;
        // 投票次数如果没设置，默认为1
        if (vote.getNumberTimes() != null || vote.getNumberTimes() != 0) {
            voteNumber = vote.getNumberTimes();
        }
        //如果用户投票记录表的记录数和投票表设置的投票数相等，则证明投票次数已用完
        if (voteUsers != null && voteUsers.size() >= voteNumber) {
            return ServerResponseEntity.showFailMsg("您的投票次数已用完！！！");
        }

        // 根据传来的投票详情id （voteDetailId），去查询该记录并给该记录的票数number_vote字段加1
        QueryWrapper<TzVoteDetail> detailQueryWrapper = new QueryWrapper<>();
        detailQueryWrapper.eq("tz_vote_detail.id", tzVote.getVoteDetailId());
        TzVoteDetail tzVoteDetail = iTzVoteDetailService.getOne(detailQueryWrapper);
        if (tzVoteDetail == null) {
            return ServerResponseEntity.showFailMsg("该投票不存在，投票失败");
        }
        if (StringUtils.isNotBlank(tzVoteDetail.getVoteBy())) {
            List<String> list = Arrays.asList(tzVoteDetail.getVoteBy().split(","));
            // 如果没包含该用户，则将其添加上
            if (!list.contains(globalUserId)) {
                tzVoteDetail.setVoteBy(tzVoteDetail.getVoteBy() + "," + globalUserId);
            }
        } else {
            // 如果为空则是第一个添加，不需要逗号分隔
            tzVoteDetail.setVoteBy(globalUserId);
        }

        // 给投票次数加1,如果原来投票次数不为空，则从原来的基础上加1，反之为空，则从0开始加1
        Integer numberVote = 0;
        if (tzVoteDetail.getNumberVote() != null) {
            numberVote = tzVoteDetail.getNumberVote() + 1;
        } else {
            numberVote = numberVote + 1;
        }
        tzVoteDetail.setNumberVote(numberVote);
        boolean save = iTzVoteDetailService.updateById(tzVoteDetail);
        TzVoteUser tzVoteUser = new TzVoteUser();
        tzVoteUser.setUserId(globalUserId);
        tzVoteUser.setVoteDetailId(tzVote.getVoteDetailId());
        tzVoteUser.setVoteId(tzVote.getId());
        boolean save1 = iTzVoteUserService.save(tzVoteUser);
        if (save && save1) {
            return ServerResponseEntity.success("投票成功");
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.success("投票异常");
        }
    }
}

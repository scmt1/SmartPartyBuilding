package me.flyray.bsin.server.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.mapper.TzVoteUserMapper;
import me.flyray.bsin.facade.service.TzVoteUserService;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzVoteUserServiceImpl implements TzVoteUserService {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private TzVoteUserMapper tzVoteUserMapper;

    @Autowired
    private JcxfPartyMemberMapper partyMemberMapper;


    public Map<String, Object> queryTzVoteUserListByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            TzVoteUser  tzVoteUser = JSON.parseObject(JSON.toJSONString(map.get("tzVoteUser")), TzVoteUser.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(map.get("searchVo")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

            int page = 1;
            int limit = 10;
            if (pageVo != null) {
                if (pageVo.getPageNumber() != 0) {
                    page = pageVo.getPageNumber();
                }
                if (pageVo.getPageSize() != 0) {
                    limit = pageVo.getPageSize();
                }
            }
            Page<TzVoteUser> pageData = new Page<>(page, limit);
            QueryWrapper<TzVoteUser> queryWrapper = new QueryWrapper<>();
            if (tzVoteUser !=null) {
                queryWrapper = LikeAllField(tzVoteUser,searchVo);
            }
            IPage<TzVoteUser> result = tzVoteUserMapper.selectPage(pageData, queryWrapper);
            for (TzVoteUser user: result.getRecords()) {
                QueryWrapper q = new QueryWrapper();
                q.select("avatar");
                q.select("real_name");
                q.eq("id", user.getUserId());
                JcxfPartyMember partyMember= partyMemberMapper.selectOne(q);
                if (partyMember != null) {
                    user.setAvatar(partyMember.getAvatar());
                    user.setUserName(partyMember.getRealName());
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    /**
     * 功能描述：构建模糊查询
     * @param tzVote 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzVoteUser>  LikeAllField(TzVoteUser tzVoteUser, SearchVo searchVo) {
        QueryWrapper<TzVoteUser> queryWrapper = new QueryWrapper<>();
        if(tzVoteUser.getId() != null){
            queryWrapper.and(i -> i.eq("tz_vote_user.id", tzVoteUser.getId()));
        }
        if (tzVoteUser.getUserId() != null) {
            queryWrapper.and(i -> i.eq("tz_vote_user.user_id", tzVoteUser.getUserId()));
        }
        if (tzVoteUser.getVoteId() != null) {
            queryWrapper.and(i -> i.eq("tz_vote_user.vote_id", tzVoteUser.getVoteId()));
        }
        if (tzVoteUser.getVoteDetailId() != null) {
            queryWrapper.and(i -> i.eq("tz_vote_user.vote_detail_id", tzVoteUser.getVoteDetailId()));
        }


        return queryWrapper;

    }
}

package me.flyray.bsin.facade.service;

import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public interface TzVoteUserService {

    @ApiOperation("根据detailId查询投票记录")
    @Path("queryTzVoteUserListByPage")
    @Produces("application/json")
    Map<String, Object> queryTzVoteUserListByPage(Map<String, Object> requestMap);

}

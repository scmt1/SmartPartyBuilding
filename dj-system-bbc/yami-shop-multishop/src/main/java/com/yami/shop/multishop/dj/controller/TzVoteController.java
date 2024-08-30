package com.yami.shop.multishop.dj.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.interfaces.Func;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yami.shop.bean.model.dj.TzSysDept;
import com.yami.shop.bean.model.dj.TzVote;
import com.yami.shop.bean.model.dj.TzVoteDetail;
import com.yami.shop.bean.model.dj.TzVoteUser;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.dj.service.*;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopEmployee;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/multi/tzVote")
public class TzVoteController {
    @Autowired
    private ITzVoteService tzVoteService;
    @Autowired
    private ITzVoteDetailService iTzVoteDetailService;
    @Autowired
    private ShopEmployeeService shopEmployeeService;
    @Autowired
    private ITzSysDeptService iTzSysDeptService;
    @Autowired
    private ITzVoteUserService iTzVoteUserService;


    /**
     * @param map
     * @return
     */
    @ApiOperation("新增投票")
    @PostMapping("addVote")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addVote(@RequestBody Map<String, Object> map) {
        try {
            String json1 = JSON.toJSONString(map.get("vote"));
            String json2 = JSON.toJSONString(map.get("voteDetail"));
            TzVote tzVote = JSON.parseObject(json1, TzVote.class);
            List<TzVoteDetail> list = JSON.parseArray(json2, TzVoteDetail.class);
            tzVote.setDelFlag("0");
            tzVote.setCreateTime(new Date());
            String voteDeptIds = tzVote.getVoteDeptIds();
            if (StringUtils.isNotBlank(voteDeptIds)) {
                voteDeptIds = voteDeptIds.substring(1, voteDeptIds.length() - 1);
                tzVote.setVoteDeptIds(voteDeptIds);
            }
            boolean save = tzVoteService.save(tzVote);
            if (save) {
                for (TzVoteDetail tzVoteDetail : list) {
                    tzVoteDetail.setVoteId(tzVote.getId());
                    tzVoteDetail.setNumberVote(0);
                }
                boolean save1 = iTzVoteDetailService.saveBatch(list);
                if (save1) {
                    return ServerResponseEntity.success(save1);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("保存失败");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("保存失败" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询vote数据")
    @GetMapping("queryTzVoteList")
    public ServerResponseEntity<Object> queryTzVoteList(TzVote tzVote, SearchVo searchVo, PageVo pageVo) {
        try {
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = shopEmployee.getUsername();
            String deptId = shopEmployee.getDeptId();
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
            if(StringUtils.isBlank(deptId)){
                return ServerResponseEntity.showFailMsg("部门id为空，请联系管理员");
            }
            queryWrapper.select("tz_sys_dept.name").eq("tz_sys_dept.id", deptId);
            queryWrapper.eq("tz_sys_dept.del_flag", 0);
            TzSysDept one = iTzSysDeptService.getOne(queryWrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("startDept", one.getName());
            map.put("createBy", username);
            IPage<TzVote> result = tzVoteService.queryTzVoteListByPage(tzVote, searchVo, pageVo);
            map.put("result", result);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("根据id查询投票信息")
    @GetMapping("queryTzVoteById")
    public ServerResponseEntity<Object> queryTzVoteById(String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        try {
            QueryWrapper<TzVote> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_vote.id", id);
            TzVote one = tzVoteService.getOne(queryWrapper);
            if (one == null) {
                return ServerResponseEntity.showFailMsg("查询异常");
            }
            QueryWrapper<TzVoteDetail> detailQueryWrapper = new QueryWrapper<>();
            detailQueryWrapper.eq("tz_vote_detail.vote_id", id);
            List<TzVoteDetail> list = iTzVoteDetailService.list(detailQueryWrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("vote", one);
            map.put("voteDetail", list);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("根据id查询投票信息")
    @PostMapping("deleteTzVoteById")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> deleteTzVoteById(@RequestBody String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        boolean b = tzVoteService.removeByIds(Arrays.asList(ids));
        QueryWrapper <TzVoteDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tz_vote_detail.vote_id",ids);
        boolean remove = iTzVoteDetailService.remove(queryWrapper);
        if(b&&remove){
            return ServerResponseEntity.success(true);
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.showFailMsg("删除失败");
        }
    }



    /**
     * 模拟移动端用户登录后，访问自己能查看的投票
     * @return
     */
    @ApiOperation("模拟移动端用户登录后，访问自己能查看的投票")
    @PostMapping("queryVote")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity queryVote(){
        //先获取移动端登录用户的信息，得到用户所属的部门id
        String deptId = null;
        if(StringUtils.isBlank(deptId)){
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        // 查询投票结束日期大于当前日期的
        QueryWrapper<TzVote> voteQueryWrapper = new QueryWrapper<>();
        voteQueryWrapper.eq("tz_vote.del_flag",0);
        voteQueryWrapper.ge("tz_vote.end_date",new Date());
        List<TzVote> voteList = new ArrayList<>();
        List<TzVote> list = tzVoteService.list(voteQueryWrapper);
        // 再筛选用户所属的部门id在权限部门id集合里的投票
        if(list!=null&&list.size()>0){
            for (TzVote tzVote : list) {
                String voteDeptIds = tzVote.getVoteDeptIds();
                List<String> stringList = Arrays.asList(voteDeptIds);
                if(stringList.contains(deptId)){
                    voteList.add(tzVote);
                }
            }
        }
        return ServerResponseEntity.success(voteList);
        //voteQueryWrapper.and(i -> i.like("tz_vote.vote_dept_ids", "," + deptId + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), tzTermDept.getDeptId())));
    }
    @ApiOperation("根据id查询具体的投票信息")
    @GetMapping("queryTzVoteDetailById")
    public ServerResponseEntity<Object> queryTzVoteDetailById(String id,SearchVo searchVo, PageVo pageVo) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        TzVoteDetail tzVoteDetail = new TzVoteDetail();
        tzVoteDetail.setVoteId(Integer.parseInt(id));
        QueryWrapper<TzVoteDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tz_vote_detail.vote_id", tzVoteDetail.getVoteId()).orderByDesc("tz_vote_detail.number_vote");
        List<TzVoteDetail> list = iTzVoteDetailService.list(queryWrapper);
        if(list==null||list.size()==0){
              return ServerResponseEntity.success();
        }
        int i = 1;
        for (TzVoteDetail voteDetail : list) {
            voteDetail.setOrders(i);
            i++;
        }
        iTzVoteDetailService.saveOrUpdateBatch(list);
        try {
            IPage<TzVoteDetail> tzVoteDetailIPage = iTzVoteDetailService.queryTzVoteDetailListByPage(tzVoteDetail, searchVo, pageVo);
            return ServerResponseEntity.success(tzVoteDetailIPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
}

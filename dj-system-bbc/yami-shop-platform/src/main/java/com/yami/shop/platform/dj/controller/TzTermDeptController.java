package com.yami.shop.platform.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.yami.shop.bean.model.dj.TzSysDept;
import com.yami.shop.bean.model.dj.TzTermDept;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;

import com.yami.shop.dj.service.ITzSysDeptService;
import com.yami.shop.dj.service.ITzTermDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 **/
@RestController
@Api(tags = " test数据接口")
@RequestMapping("/platform/tzTermDept")
public class TzTermDeptController {
    @Autowired
    private ITzTermDeptService tzTermDeptService;

    @Autowired
    private ITzSysDeptService tzSysDeptService;

/*	@Autowired
	private SecurityUtil securityUtil;*/

    /**
     * 功能描述：新增换届信息数据
     *
     * @param tzTermDept 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增换届信息数据")
    @PostMapping("addTzTermDept")
    public ServerResponseEntity<Object> addTzTermDept(@RequestBody TzTermDept tzTermDept) {
        try {

            //如果id不为空则为修改
            if (tzTermDept.getId() != null) {
                boolean updateById = tzTermDeptService.updateById(tzTermDept);
                if (updateById) {
                    return ServerResponseEntity.success(updateById);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            }
            //如果该班子届次已存在则不允许再添加
            if (tzTermDept.getLeaderTime() != null) {
                QueryWrapper<TzTermDept> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("tz_term_dept.leader_time", tzTermDept.getLeaderTime());
                queryWrapper.eq("tz_term_dept.dept_id", tzTermDept.getDeptId());
                queryWrapper.eq("tz_term_dept.del_flag", 0);
                List<TzTermDept> list = tzTermDeptService.list(queryWrapper);
                if (list.size() > 0) {
                    return ServerResponseEntity.showFailMsg("该班子届次已存在！！！");
                }
            }
            tzTermDept.setRecordAddTime(new Date());
            tzTermDept.setDelFlag(0);
            //tzTermDept.setCreateId(securityUtil.getCurrUser().getId());
            tzTermDept.setCreateTime(new Date());
            boolean res = tzTermDeptService.save(tzTermDept);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：更新数据
     * @param tzTermDept 实体
     * @return 返回更新结果
     */
	/*@ApiOperation("更新test数据")
	@PostMapping("updateTzTermDept")
	public Result<Object> updateTzTermDept(@RequestBody TzTermDept tzTermDept){
		if (StringUtils.isBlank(tzTermDept.getId())) {
			return ResultUtil.error("参数为空，请联系管理员！！");
		}
		try {
			tzTermDept.setUpdateId(securityUtil.getCurrUser().getId());
			tzTermDept.setUpdateTime(new Date());
			boolean res = tzTermDeptService.updateById(tzTermDept);
			if (res) {
				return ResultUtil.data(res, "修改成功");
			} else {
				return ResultUtil.data(res, "修改失败");
			}
		} catch (Exception e) {
			 e.printStackTrace();
			return ResultUtil.error("保存异常:" + e.getMessage());
		}
	}*/

    /**
     * 功能描述：根据主键来删除数据
     *
     * @param ids 主键集合
     * @return 返回删除结果
     */
    @ApiOperation("根据主键来删除test数据")
    @GetMapping("deleteTzTermDept")
    public ServerResponseEntity<Object> deleteTzTermDept(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<TzTermDept> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_term_dept.del_flag", 1).in("tz_term_dept.id", ids);
            boolean res = tzTermDeptService.update(updateWrapper);
            //boolean res = tzTermDeptService.removeByIds(Arrays.asList(ids));
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
    @ApiOperation("根据主键来获取test数据")
    @GetMapping("getTzTermDept")
    public ServerResponseEntity<Object> getTzTermDept(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            TzTermDept res = tzTermDeptService.getById(id);
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
     * 功能描述：根据deptId分页查询换届信息数据
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("根据deptId分页查询换届信息数据")
    @GetMapping("queryTzTermDeptList")
    public ServerResponseEntity<Object> queryTzTermDeptList(TzTermDept tzTermDept, SearchVo searchVo, PageVo pageVo, String type) {
        try {
            Map<String, Object> map = new HashMap<>();
            // 等开通权限后，通过权限来区分该用户的查询范围，1为查询自己的换届历史信息，2为查询所有用户当前换届信息
            //tzTermDept.setRecorder(securityUtil.getCurrUser().getId());
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            tzTermDept.setDelFlag(0);

            List<TzSysDept> deptList = new ArrayList<>();
            IPage<TzTermDept> result;
            if (tzTermDept.getDeptId() != null) {
                //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的换届信息
                if (tzTermDept.getDeptId() != null) {
                    queryWrapper.and(i -> i.eq("tz_sys_dept.del_flag", 0));
                    queryWrapper.and(i -> i.like("tz_sys_dept.parent_ids", "," + tzTermDept.getDeptId() + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), tzTermDept.getDeptId())));
                    //queryWrapper.or();
                    deptList = tzSysDeptService.list(queryWrapper);
                    for (TzSysDept tzSysDept : deptList) {
                        Integer id = tzSysDept.getId();
                        listAll.add(id);
                    }
                }
                result = tzTermDeptService.queryTzTermDeptListByPage(tzTermDept, searchVo, pageVo, listAll, type);
            } else {
                result = tzTermDeptService.queryTzTermDeptListByPage(tzTermDept, searchVo, pageVo, listAll, type);
                List<TzTermDept> records = result.getRecords();
                for (TzTermDept record : records) {
                    QueryWrapper<TzSysDept> wrapper = new QueryWrapper<>();
                    wrapper.eq("tz_sys_dept.del_flag", 0);
                    wrapper.eq("tz_sys_dept.id", record.getDeptId());
                    TzSysDept one = tzSysDeptService.getOne(wrapper);
                    deptList.add(one);
                }

            }
            //IPage<TzTermDept> result = tzTermDeptService.queryTzTermDeptListByPage(tzTermDept, searchVo, pageVo, listAll, type);
            map.put("result", result);
            if(!type.equals("1")){
            List<Integer> tmp = new ArrayList<>();
            List<TzTermDept> records = result.getRecords();
            for (TzTermDept record : records) {
                if (record.getIsChange().equals("是")) {
                    tmp.add(record.getDeptId());
                }
            }
            // 已换届组织
            List<TzSysDept> changeList = deptList.stream().filter(i -> tmp.contains(i.getId())).collect(Collectors.toList());
            List<Integer> changeIds = new ArrayList<>();
            // 已按期换届党组织数
            map.put("changedNum", changeList.size());
            //  已换届党委数
            Integer changedDangweiNum = 0;
            // 已换届党总支数
            Integer changedDangzongNum = 0;
            //  已换届党支部数
            Integer changedDangzhiNum = 0;
            //  已换届联合党支部数
            Integer changedUnitdangzhi = 0;
            for (TzSysDept i : changeList) {
                changeIds.add(i.getId());
                if (i.getType() == 611) {
                    changedDangweiNum = changedDangweiNum + 1;
                } else if (i.getType() == 621) {
                    changedDangzongNum = changedDangzongNum + 1;
                } else if (i.getType() == 631) {
                    changedDangzhiNum = changedDangzhiNum + 1;
                } else if (i.getType() == 632) {
                    changedUnitdangzhi = changedUnitdangzhi + 1;
                }
            }
            // 未换届组织
            List<TzSysDept> unChangeList = deptList.stream().filter(e -> {
                return !changeIds.contains(e.getId());
            }).collect(Collectors.toList());
            // 未按期换届党组织数
            map.put("unChangedNum", unChangeList.size());
            //  未换届党委数
            Integer unChangedDangweiNum = 0;
            // 未换届党总支数
            Integer unChangedDangzongNum = 0;
            //  未换届党支部数
            Integer unChangedDangzhiNum = 0;
            //  未换届联合党支部数
            Integer unChangedUnitdangzhi = 0;
            for (TzSysDept i : unChangeList) {
                if (i.getType() == 611) {
                    unChangedDangweiNum = unChangedDangweiNum + 1;
                } else if (i.getType() == 621) {
                    unChangedDangzongNum = unChangedDangzongNum + 1;
                } else if (i.getType() == 631) {
                    unChangedDangzhiNum = unChangedDangzhiNum + 1;
                } else if (i.getType() == 632) {
                    unChangedUnitdangzhi = unChangedUnitdangzhi + 1;
                }
            }
            map.put("changedDangweiNum", changedDangweiNum);
            map.put("changedDangzongNum", changedDangzongNum);
            map.put("changedDangzhiNum", changedDangzhiNum);
            map.put("changedUnitdangzhi", changedUnitdangzhi);
            map.put("unChangedDangweiNum", unChangedDangweiNum);
            map.put("unChangedDangzongNum", unChangedDangzongNum);
            map.put("unChangedDangzhiNum", unChangedDangzhiNum);
            map.put("unChangedUnitdangzhi", unChangedUnitdangzhi);}
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

}

package com.yami.shop.multishop.dj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.yami.shop.bean.dto.PayFeeDto;
import com.yami.shop.bean.model.dj.TzPayFee;
import com.yami.shop.bean.model.dj.TzPayFeeDetail;
import com.yami.shop.bean.model.dj.TzSysDept;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.ImportExeclUtil;

import com.yami.shop.dj.service.ITzPayFeeDetailService;
import com.yami.shop.dj.service.ITzPayFeeService;
import com.yami.shop.dj.service.ITzSysDeptService;
import com.yami.shop.multishop.uitls.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author
 **/
@RestController
@Api(tags = " test数据接口")
@RequestMapping("/multi/tzPayFee")
@Transactional
public class TzPayFeeController {
    @Autowired
    private ITzPayFeeService tzPayFeeService;

    @Autowired
    private ITzSysDeptService tzSysDeptService;

    @Autowired
    private ITzPayFeeDetailService iTzPayFeeDetailService;
/*	@Autowired
	private SecurityUtil securityUtil;*/

    /**
     * 功能描述：新增test数据
     *
     * @param tzPayFee 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增test数据")
    @PostMapping("addTzPayFee")
    public ServerResponseEntity<Object> addTzPayFee(@RequestBody TzPayFee tzPayFee) {
        try {
            tzPayFee.setDelFlag(0);
            //tzPayFee.setCreateId(securityUtil.getCurrUser().getId());
            tzPayFee.setCreateTime(new Date());
            boolean res = tzPayFeeService.save(tzPayFee);
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
     *
     * @param tzPayFee 实体
     * @return 返回更新结果
     */
    @ApiOperation("更新test数据")
    @PostMapping("updateTzPayFee")
    public ServerResponseEntity<Object> updateTzPayFee(@RequestBody TzPayFee tzPayFee) {
        if (StringUtils.isBlank(tzPayFee.getId().toString())) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
		/*	tzPayFee.setUpdateId(securityUtil.getCurrUser().getId());
			tzPayFee.setUpdateTime(new Date());*/
            boolean res = tzPayFeeService.updateById(tzPayFee);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }




    /**
     * 功能描述：分页查询支部及其所属子支部的缴费记录
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询test数据")
    @GetMapping("queryTzPayFeeList")
    public ServerResponseEntity<Object> queryTzPayFeeList(String searchMoth, TzPayFee tzPayFee, SearchVo searchVo, PageVo pageVo) throws ParseException {
        QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
        List<Integer> listAll = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (searchMoth != null && searchMoth != "") {
            tzPayFee.setPayMonth(sdf.parse(searchMoth));
            //tzPayFee.setPayMonth(new Date(searchMoth));
        }
        //根据党组织id查询该组织下辖的所有组织的id
        if (tzPayFee.getDeptId() != null) {
            queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + tzPayFee.getDeptId() + ","));
            queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), tzPayFee.getDeptId()));
            List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
            for (TzSysDept tzSysDept : deptList) {
                Integer id = tzSysDept.getId();
                listAll.add(id);
            }
        }
        try {
            IPage<TzPayFee> result = tzPayFeeService.queryTzPayFeeListByPage(tzPayFee, searchVo, pageVo, listAll);
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
    @ApiOperation("分页查询test数据")
    @GetMapping("queryTzPayFeeDetailList")
    public ServerResponseEntity<Object> queryTzPayFeeDetailList(TzPayFeeDetail tzPayFeeDetail, String searchMoth, SearchVo searchVo, PageVo pageVo) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (searchMoth != null && searchMoth != "") {
                tzPayFeeDetail.setPayMonth(sdf.parse(searchMoth));
            }
            IPage<TzPayFeeDetail> result = iTzPayFeeDetailService.queryTzPayFeeDetailListByPage(tzPayFeeDetail, searchVo, pageVo);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 按年统计每月支部党费缴纳情况
     * @param tzPayFee
     * @param searchVo
     * @param pageVo
     * @return
     */
    @ApiOperation("按年统计每月支部党费缴纳情况")
    @GetMapping("queryFeeTotalByYear")
    public ServerResponseEntity<Object> queryFeeTotalByYear(TzPayFee tzPayFee, SearchVo searchVo, PageVo pageVo) {
        QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
        List<Integer> listAll = new ArrayList<>();
        List<Integer> listOne = new ArrayList<>(); // 装父本级id
        Map<Integer,List<Integer>> map = new HashMap<>();
        //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
        if (tzPayFee.getDeptId() != null) {
            queryWrapper.or(i -> i.eq("tz_sys_dept.parent_id", tzPayFee.getDeptId()).and(i2->i2.eq("tz_sys_dept.del_flag",0)));
            //queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), tzPayFee.getDeptId()));
            List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
            Integer index = 1;
            listOne.add(tzPayFee.getDeptId());
            map.put(0,listOne);
            for (TzSysDept tzSysDept : deptList) {
                Integer id = tzSysDept.getId();
                listAll.add(id);
                QueryWrapper<TzSysDept> wrapper = new QueryWrapper<>();
                wrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + id + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), id)));
                wrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
                List<TzSysDept> depts = tzSysDeptService.list(wrapper);
                List<Integer> deptids = new ArrayList<>();
                if (depts != null && depts.size() > 0) {
                    for (TzSysDept dept : depts) {
                        deptids.add(dept.getId());
                    }
                }
                map.put(index,deptids);
                index++;
            }
        }
        try {
            List<Map<String, Object>> result = tzPayFeeService.queryFeeTotalByYear(tzPayFee, searchVo, pageVo, listAll,map);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("导出支部缴费信息")
    @GetMapping("/downloadFeeTotalByYear")
    public void downloadFeeTotalByYear(HttpServletResponse response,  TzPayFee tzPayFee) {
        QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
        List<Integer> listAll = new ArrayList<>();
        List<Integer> listOne = new ArrayList<>(); // 装父本级id
        Map<Integer,List<Integer>> map = new HashMap<>();
        //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
        if (tzPayFee.getDeptId() != null) {
            queryWrapper.or(i -> i.eq("tz_sys_dept.parent_id", tzPayFee.getDeptId()).and(i2->i2.eq("tz_sys_dept.del_flag",0)));
            //queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), tzPayFee.getDeptId()));
            List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
            Integer index = 1;
            listOne.add(tzPayFee.getDeptId());
            map.put(0,listOne);
            for (TzSysDept tzSysDept : deptList) {
                Integer id = tzSysDept.getId();
                listAll.add(id);
                QueryWrapper<TzSysDept> wrapper = new QueryWrapper<>();
                wrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + id + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), id)));
                wrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
                List<TzSysDept> depts = tzSysDeptService.list(wrapper);
                List<Integer> deptids = new ArrayList<>();
                if (depts != null && depts.size() > 0) {
                    for (TzSysDept dept : depts) {
                        deptids.add(dept.getId());
                    }
                }
                map.put(index,deptids);
                index++;
            }
        }
        try {
            tzPayFeeService.downloadFeeTotalByYear(response,tzPayFee,map);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * @param response
     * @param tzPayFee
     */
    @ApiOperation("导出党员缴费模板")
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response, @RequestBody TzPayFee tzPayFee) {
        Integer deptId = tzPayFee.getDeptId();
        QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
        List<Integer> deptIds = new ArrayList<>();
        if (deptId == null) {
            deptId = 1;
        }
        Integer finalDeptId = deptId;
        queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + finalDeptId + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), finalDeptId)));
        queryWrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
        List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
        if (deptList != null && deptList.size() > 0) {
            for (TzSysDept tzSysDept : deptList) {
                Integer id = tzSysDept.getId();
                deptIds.add(id);
            }
        }
        try {
            tzPayFeeService.downloadTemplate(deptIds, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param response
     * @param tzPayFeeDetail
     */
    @ApiOperation("导出党员缴费详情")
    @GetMapping("/exportDetail")
    public void exportDetail(HttpServletResponse response, TzPayFeeDetail tzPayFeeDetail) {
        QueryWrapper<TzPayFee> wrapper = new QueryWrapper<>();
        wrapper.eq("tz_pay_fee.id", tzPayFeeDetail.getPayFeeId());
        wrapper.eq("tz_pay_fee.del_flag", 0);
        TzPayFee one = tzPayFeeService.getOne(wrapper);
        Date payMonth = one.getPayMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String format = sdf.format(payMonth);
        try {
            iTzPayFeeDetailService.exportDetail(tzPayFeeDetail, response, format);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 导入数据
     */
    @ApiOperation("导入数据")
    @PostMapping("/importExcel")
    @Transactional
    public ServerResponseEntity<Object> importExcel(@RequestParam(value = "file") MultipartFile multipartFile, String payMonth, String deptName, String deptId) throws Exception {
        TzSysDept byId = tzSysDeptService.getById(deptId);
        deptName = byId.getName();

        //这一步表示如果之前有数据，就把之前的信息先删除掉，然后再传入新数据，即为覆盖
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        QueryWrapper<TzPayFeeDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.eq("tz_pay_fee_detail.dept_id", deptId));
        queryWrapper.and(i -> {
            try {
                i.eq("tz_pay_fee_detail.pay_month", sdf.parse(payMonth));
            } catch (ParseException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                e.printStackTrace();
            }
        });
        List<TzPayFeeDetail> list1 = iTzPayFeeDetailService.list(queryWrapper);
        if (list1 != null && list1.size() > 0) {
            TzPayFeeDetail tzPayFeeDetail = list1.get(0);
            Integer payFeeId = tzPayFeeDetail.getPayFeeId();
            if (payFeeId != null) {
                QueryWrapper<TzPayFee> wrapper = new QueryWrapper<>();
                wrapper.eq("tz_pay_fee.id", payFeeId);
                boolean remove = tzPayFeeService.remove(wrapper);
                System.out.println(remove);
            }
        }
        boolean remove = iTzPayFeeDetailService.remove(queryWrapper);

        //删除完成后再导入新的数据
        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);
        Sheet sheetAt = wb.getSheetAt(0);
        String sheetName = sheetAt.getSheetName();
        PayFeeDto payFeeDto = new PayFeeDto();

        List<PayFeeDto> list = ImportExeclUtil.readDateListT(wb, payFeeDto, sheetName, 3, 0);
        List<TzPayFeeDetail> payFeeDetailList = new ArrayList<>();
        TzPayFeeDetail tzPayFeeDetail = null;
        TzPayFee tzPayFee = new TzPayFee();
        Double payTotal = Double.valueOf(0);
        if (list == null || list.size() < 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.showFailMsg("导入数据为空");
        }
        for (PayFeeDto fee : list) {
            tzPayFeeDetail = new TzPayFeeDetail();
            tzPayFeeDetail.setDelFlag(0);
            tzPayFeeDetail.setPartyMemberId(Integer.valueOf(fee.getPartyId()));
            tzPayFeeDetail.setIdCardLast(fee.getIdcard());
            tzPayFeeDetail.setName(fee.getName());
            String payFee = fee.getPayFee();
            if (StringUtils.isBlank(payFee)) {
                payFee = "0";
            }
            tzPayFeeDetail.setShouldPay(Double.valueOf(payFee));
            tzPayFeeDetail.setActuallyPay(Double.valueOf(0));
            tzPayFeeDetail.setPayMonth(sdf.parse(payMonth));
            tzPayFeeDetail.setStatus("0");
            tzPayFeeDetail.setIsInsteadPay("0");
            tzPayFeeDetail.setDeptName(deptName);
            tzPayFeeDetail.setDeptId(Integer.valueOf(deptId));
            tzPayFeeDetail.setCreateTime(new Date());
            payFeeDetailList.add(tzPayFeeDetail);
            payTotal = payTotal + Double.valueOf(payFee);
        }
        tzPayFee.setPayMonth(sdf.parse(payMonth));
        tzPayFee.setDeptId(Integer.valueOf(deptId));
        tzPayFee.setShouldPay(payTotal);
        tzPayFee.setActuallyPay(Double.valueOf(0));
        tzPayFee.setDeptName(deptName);
        tzPayFee.setDelFlag(0);
        tzPayFee.setStatus("0");
        tzPayFee.setCreateTime(new Date());
        boolean save = tzPayFeeService.save(tzPayFee);
        try {
            if (save) {
                for (TzPayFeeDetail payFeeDetail : payFeeDetailList) {
                    payFeeDetail.setPayFeeId(tzPayFee.getId());
                }
                boolean saveBatch = iTzPayFeeDetailService.saveBatch(payFeeDetailList);
                if (saveBatch) {
                    return ServerResponseEntity.success(saveBatch);
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("导入异常");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("导入异常");
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.showFailMsg("导入异常" + e.getMessage());
        }
    }

    /**
     * 删除支部缴纳党费以及对应的党员记录
     */
    @GetMapping("deleteByIds")
    @ApiOperation(value = "删除支部缴纳党费以及对应的党员记录", notes = "删除支部缴纳党费以及对应的党员记录")
    public ServerResponseEntity<Object> deleteDevelopParty(String id) {
        if (id == null || id == "") {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            List<Long> integerList = new ArrayList<>();

            //先拿到要删除的支部id，根据这个id查询出对应的详情表的内容
        /*    TzPayFee byId = tzPayFeeService.getById(ids[0]);
            Integer id = byId.getId();*/
            QueryWrapper<TzPayFeeDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.and(i -> i.eq("tz_pay_fee_detail.del_flag", 0));
            queryWrapper.and(i -> i.eq("tz_pay_fee_detail.pay_fee_id", id));
            List<TzPayFeeDetail> list = iTzPayFeeDetailService.list(queryWrapper);
            //遍历拿到的详情表内容，获取其中的详情记录id
            for (TzPayFeeDetail detail : list) {
                integerList.add(detail.getId());
            }
            //修改tz_pay_fee表里的del_flag状态，即逻辑删除
            UpdateWrapper<TzPayFee> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tz_pay_fee.del_flag", 1).in("tz_pay_fee.id", id);
            boolean update = tzPayFeeService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                //删除成功后再根据之前拿到的详情记录id集合，去修改对应的详情记录
                UpdateWrapper<TzPayFeeDetail> detailUpdateWrapper = new UpdateWrapper<>();
                detailUpdateWrapper.set("tz_pay_fee_detail.del_flag", 1).in("tz_pay_fee_detail.id", integerList);
                boolean update1 = iTzPayFeeDetailService.update(detailUpdateWrapper);
                if (update1) {
                    return ServerResponseEntity.success(update);
                } else {
                    return ServerResponseEntity.showFailMsg("删除失败");
                }
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }


    @ApiOperation("复制其他月数据")
    @GetMapping("copyDataByMonth")
    public ServerResponseEntity<Object> copyDataByMonth(String payMonth, String copyMonth, String deptId) {
        try {
            //先通过copyMonth查找记录表，看原记录是否存在
            QueryWrapper<TzPayFee> queryWrapper = new QueryWrapper<>();
            queryWrapper.and(i -> i.eq("tz_pay_fee.dept_id", deptId));
            queryWrapper.and(i -> i.like("tz_pay_fee.pay_month", copyMonth));
            queryWrapper.and(i -> i.eq("tz_pay_fee.del_flag", 0));
            TzPayFee one = tzPayFeeService.getOne(queryWrapper);
            if (one == null) {
                return ServerResponseEntity.showFailMsg("所选月份暂无记录，无法复制");
            }
            //获取主表id作为详情表的外键id来查询
            Integer payFeeId = one.getId();

            //查询当前是否已经存在数据，如果存在则不允许重复添加
            QueryWrapper<TzPayFee> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.and(i -> i.eq("tz_pay_fee.dept_id", deptId));
            queryWrapper2.and(i -> i.like("tz_pay_fee.pay_month", payMonth));
            queryWrapper2.and(i -> i.eq("tz_pay_fee.del_flag", 0));
            TzPayFee tzPayFee = tzPayFeeService.getOne(queryWrapper2);
            if (tzPayFee != null) {
                return ServerResponseEntity.showFailMsg("缴纳年月存在数据，不能重复导入!");
            }

            //修改并保存数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            one.setPayMonth(sdf.parse(payMonth));
            one.setStatus("0");
            one.setActuallyPay(0.0);
            one.setDelFlag(0);
            one.setCreateTime(new Date());
            one.setId(null);
            boolean save = tzPayFeeService.save(one);
            if (save) {
                //查询详情表数据并保存为缴纳月数据
                QueryWrapper<TzPayFeeDetail> payFeeDetailQueryWrapper = new QueryWrapper<>();
                payFeeDetailQueryWrapper.and(i -> i.eq("tz_pay_fee_detail.pay_fee_id", payFeeId));
                payFeeDetailQueryWrapper.and(i -> i.like("tz_pay_fee_detail.pay_month", copyMonth));
                payFeeDetailQueryWrapper.and(i -> i.eq("tz_pay_fee_detail.del_flag", 0));
                List<TzPayFeeDetail> tzPayFeeDetails = iTzPayFeeDetailService.list(payFeeDetailQueryWrapper);

                if (tzPayFeeDetails != null && tzPayFeeDetails.size() > 0) {
                    for (TzPayFeeDetail tzPayFeeDetail : tzPayFeeDetails) {
                        tzPayFeeDetail.setId(null);
                        tzPayFeeDetail.setPayMonth(sdf.parse(payMonth));
                        tzPayFeeDetail.setPayFeeId(one.getId());
                        tzPayFeeDetail.setCreateTime(new Date());
                        tzPayFeeDetail.setDelFlag(0);
                        tzPayFeeDetail.setActuallyPay(0.0);
                        tzPayFeeDetail.setStatus("0");
                    }
                    boolean b = iTzPayFeeDetailService.saveBatch(tzPayFeeDetails);
                    if (b) {
                        return ServerResponseEntity.success("复制成功");
                    } else {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ServerResponseEntity.showFailMsg("复制异常");
                    }
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ServerResponseEntity.showFailMsg("复制数据为空");
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("复制失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.showFailMsg("复制异常:" + e.getMessage());
        }
    }
}

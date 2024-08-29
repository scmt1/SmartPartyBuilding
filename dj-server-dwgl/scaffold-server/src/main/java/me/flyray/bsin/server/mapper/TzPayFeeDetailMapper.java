package me.flyray.bsin.server.mapper;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.query.MPJQueryWrapper;
import me.flyray.bsin.server.domain.TzPayFeeDetail;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.vo.PayFeeDetailsVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mike
 * @since 2022-11-08
 */
@DS("dj_party")
public interface TzPayFeeDetailMapper extends BaseMapper<TzPayFeeDetail>, MPJBaseMapper<TzPayFeeDetail> {

    IPage<TzPayFeeDetail> selectByType(@Param("page") Page pageData, @Param(Constants.WRAPPER) MPJQueryWrapper<TzPayFeeDetail> queryWrapper, @Param("type") String type);

    List<TzPayFeeDetail> selectByType(@Param(Constants.WRAPPER) MPJQueryWrapper<TzPayFeeDetail> queryWrapper, @Param("type") String type);

    List<TzPayFeeDetail> exportByType(@Param(Constants.WRAPPER) QueryWrapper<TzPayFeeDetail> queryWrapper, @Param("type") String type);

    IPage<TzPayFeeDetail> queryPartyMemberPayDetailList(@Param("page") Page<TzPayFeeDetail> pageData, @Param(Constants.WRAPPER) QueryWrapper<TzPayFeeDetail> queryWrapper);


    IPage<Map<String, Object>> getPayFeeDetailPersonList(@Param("deptList") List<Long> deptList, @Param("payYear") String payYear,@Param("name") String name,@Param("page") Page page);

    void saveBatchList(@Param("list") List<TzPayFeeDetail> detailArrayList);

    void updatePartyMemberDeptInfo(@Param("payFeeDetail") TzPayFeeDetail payFeeDetail);

    void updatePayFeeDetailDelFlag(@Param("payFeeDetail") TzPayFeeDetail payFeeDetail);

    List<Map<String, Object>> getPayFeeDetailPersonList(@Param("deptList") List<Long> deptList, @Param("payYear") String payYear,@Param("name") String name);

    PayFeeDetailsVo getPayFeeDetailStatistics(@Param("deptList") List<Long> deptList,@Param("payMonth") String payMonth);

    PayFeeDetailsVo getPayFeeStatistics(@Param("deptList") List<Long> deptList,@Param("startMonth") String startMonth,@Param("endMonth") String endMonth);

    List<JcxfPartyMember> getNoDonePartyMember(@Param("deptList") List<Long> deptList,@Param("payMonth") String payMonth);
}

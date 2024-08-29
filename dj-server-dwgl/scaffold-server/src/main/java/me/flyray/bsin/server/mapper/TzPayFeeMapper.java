package me.flyray.bsin.server.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import me.flyray.bsin.server.domain.TzPayFee;
import me.flyray.bsin.server.domain.TzPayFeeDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * @author ：hxh
 * @date ：Created in 2021/11/30 16:54
 * @description：SysDept数据访问
 */

@DS("dj_party")
public interface TzPayFeeMapper extends BaseMapper<TzPayFee> {

    // Map<String, BigDecimal> queryFeeTotalByYear(@Param("deptId") Integer deptId, @Param("month") String[] month, @Param("listAll") List<Integer> listAll);

    Map<String, BigDecimal> queryFeeTotalByYears(@Param("deptIds") List<Long> deptId, @Param("month") String[] month);

    IPage<TzPayFee> queryPayFeePage(@Param("page") Page<TzPayFee> pageData, @Param(Constants.WRAPPER) QueryWrapper<TzPayFee> queryWrapper);
}

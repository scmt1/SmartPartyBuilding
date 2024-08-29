package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.flyray.bsin.server.domain.jcxf.JcxfBranchReelection;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DS(value = "jcxf")
public interface JcxfTermDeptMapper extends BaseMapper<JcxfBranchReelection> {
    Integer selectExceedFinishTimeCount(@Param("deptList") List<Long> deptList, @Param("date") String date);

    List<Long> selectExceedFinishTimeDeptIdList(@Param("deptList") List<Long> deptList, @Param("date") String date);

    List<Long> selectSurplusFinishTimeDeptIdList(@Param("deptList") List<Long> deptList, @Param("days") String days);

    // IPage<JcxfBranchReelection> selectByPage(@Param("page") Page pageData, @Param(Constants.WRAPPER) QueryWrapper<TzTermDept> queryWrapper, @Param("listAll") List<Integer> listAll);

}

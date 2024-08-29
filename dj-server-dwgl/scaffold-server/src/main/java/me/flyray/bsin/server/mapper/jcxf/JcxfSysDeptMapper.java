package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseMapper;
import me.flyray.bsin.server.domain.SysOrg;
import me.flyray.bsin.server.domain.jcxf.JcxfDevelopParty;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDeptChildGroup;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DS(value = "jcxf")
public interface JcxfSysDeptMapper extends BaseMapper<JcxfSysDept> {


    List<JcxfSysDept> getNameList(@Param(value = "id") String id);

    List<JcxfSysDept> getBaseInfoById(@Param(value = "id") String id);

    List<JcxfSysDept> getCountById(@Param(value = "id") String id, @Param(value = "type") String type);

    String getParentIds(@Param(value = "deptId") Long deptId);

    List<Long> selectChildrenIdsById(@Param(value = "id") Long id);

    List<Long> selectChildrenIdsByIdAndOther(@Param(value = "id") Long id,@Param(value = "veteran") String veteran,@Param(value = "organizationType") String organizationType);

    List<JcxfSysDept> selectChildrenInfoById(@Param(value = "id") Long id);

    String getParentDept(@Param(value = "parentId") Long parentId);

    List<JcxfSysDept> getAllName();

    List<JcxfSysDept> getParentDeptListById(@Param(value = "id") String id);

    Integer queryIsLeafById(@Param(value = "id") Long deptId);

    List<Integer> selectChildrenIdsByIdAndType(@Param(value = "id") Integer id, @Param(value = "type") Integer type);

    List<Integer> getChildrenIdsByVeteran(@Param(value = "id") Integer id, @Param(value = "veteran") String veteran,
                                          @Param(value = "typeList") List<Integer> typeList);

    Integer getNextFinallySort();

    IPage<JcxfSysDept> getJcxfDeptByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfSysDept> queryWrapper);

    IPage<JcxfSysDept> getJcxfDeptLowerLeveByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfSysDept> queryWrapper);

    List<JcxfSysDeptChildGroup> getChildDeptMap(@Param(value = "id") Integer id,@Param(value = "pageNum") Integer pageNum,@Param(value = "pageSize") Integer pageSize);

    List<String> getDeptParentIdList(@Param("id") Integer id);

    Boolean addPaasDept(@Param("jcxfSysDept") JcxfSysDept jcxfSysDept);

    Boolean addPassSysOrg(@Param("sysOrg") SysOrg sysOrg);

    Boolean updatePassDept(@Param("jcxfSysDept") JcxfSysDept jcxfSysDept);

    Boolean updatePassSysOrg(@Param("sysOrg") SysOrg sysOrg);

    SysOrg getOrgInfoByOrgId(@Param("orgId") String orgId);
}

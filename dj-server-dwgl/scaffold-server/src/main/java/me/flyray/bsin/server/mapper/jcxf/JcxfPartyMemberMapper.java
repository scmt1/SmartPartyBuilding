package me.flyray.bsin.server.mapper.jcxf;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@DS(value = "jcxf")
public interface JcxfPartyMemberMapper extends BaseMapper<JcxfPartyMember> {

    Integer getPartyAge(@Param(value = "partyTime") Date partyTime);

    Long getpartyNum(@Param(value = "list") List<Long> list);

    List<JcxfPartyMember> getInfo(@Param(value = "deptIds") List<Long> deptIds);

    Integer isHasPositionByPartyId(@Param(value = "partyId") Long partyId);

    IPage<JcxfPartyMember> getJcxfPartyMemberByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfPartyMember> queryWrapper);

    IPage<JcxfPartyMemberVo> getPartyMemberListByPage(@Param("page") Page page, @Param(Constants.WRAPPER) QueryWrapper<JcxfPartyMember> queryWrapper);

    JcxfPartyMemberVo selectPartyMemberVoById(Long partyId);

    List<Map<String, Object>> queryDeptPartyMemberList(@Param("deptId") String deptId, @Param("flag") String flag);

    List<JcxfPartyMember> getPartyMemberNoEncrypt();

    List<JcxfPartyMember> getPartyMemberInfo();

    List<JcxfPartyMemberVo> selectPartyMemberVoByDeptId(String deptId);

    List<JcxfPartyMember> queryNoPayMemberList(@Param("deptId")String deptId, @Param("payMonth")String payMonth);
}

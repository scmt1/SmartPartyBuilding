package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import me.flyray.bsin.server.utils.AESUtil2;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.flyray.bsin.facade.service.JcxfPartyFloatingMemberService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyFloatingMember;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysArea;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyFloatingMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysAreaMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class JcxfPartyFloatingMemberServiceImpl implements JcxfPartyFloatingMemberService {

    @Autowired
    private JcxfPartyFloatingMemberMapper jcxfPartyFloatingMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysAreaMapper jcxfSysAreaMapper;

    @Override
    public Map<String, Object> flowOutPartyMemberList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfPartyFloatingMember partyMember = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMember")), JcxfPartyFloatingMember.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

        try {
            List<Long> listAll = new ArrayList<>();
            if (partyMember.getDeptId() != null) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(partyMember.getDeptId());
                listAll.add(partyMember.getDeptId());
            }

            QueryWrapper<JcxfPartyFloatingMember> queryWrapper = new QueryWrapper();
            queryWrapper.eq("party_floating_member.del_flag", 0);
            queryWrapper.in("party_floating_member.dept_id", listAll);
            queryWrapper.eq("party_floating_member.floating_type", 2); // 类型(1:流入,2:流出)

            if (StringUtils.isNotBlank(partyMember.getRealName())) {
                queryWrapper.like("party_floating_member.real_name", partyMember.getRealName());
            }

            if (StringUtils.isNotBlank(partyMember.getIdcard())) {
                queryWrapper.eq("party_floating_member.idcard", partyMember.getIdcard());
            }

            if (StringUtils.isNotBlank(partyMember.getPhone())) {
                queryWrapper.eq("party_floating_member.phone", partyMember.getPhone());
            }

            if (StringUtils.isNotBlank(partyMember.getFloatingStatus())) {
                queryWrapper.eq("party_floating_member.floating_status", partyMember.getFloatingStatus());
            }

            if (StringUtils.isNotEmpty(partyMember.getWorking())) {
                queryWrapper.eq("party_floating_member.working", partyMember.getWorking());
            }

            if (StringUtils.isNotEmpty(partyMember.getOutType())) {
                queryWrapper.eq("party_floating_member.out_type", partyMember.getOutType());
            }

            queryWrapper.and(i ->i.eq("party_floating_member.floating_back", 0).or().isNull("party_floating_member.floating_back"));
            queryWrapper.orderByDesc("party_floating_member.create_date");

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

            Page<JcxfPartyFloatingMember> pageData = new Page<>(page, limit);
            IPage<JcxfPartyFloatingMember> result = jcxfPartyFloatingMemberMapper.getPartyFloatingMemberByPage(pageData, queryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyFloatingById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            JcxfPartyFloatingMember floatingMember = jcxfPartyFloatingMemberMapper.selectById(id);

            if (floatingMember != null) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(floatingMember.getDeptId());
                if (dept != null) {
                    floatingMember.setDeptName(dept.getName());
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(floatingMember));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取异常");
        }
    }

    @Override
    public Map<String, Object> getPartyFloatingByPartyMemberId(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", false);
            queryWrapper.eq("floating_back", "0");
            queryWrapper.eq("party_id", id);
            JcxfPartyFloatingMember floatingMember = jcxfPartyFloatingMemberMapper.selectOne(queryWrapper);

            if (floatingMember != null) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(floatingMember.getDeptId());
                if (dept != null) {
                    floatingMember.setDeptName(dept.getName());
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(floatingMember));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取异常");
        }
    }

    @Override
    public synchronized Map<String, Object> addFloatingMember(Map<String, Object> requestMap) {
        try {
            JcxfPartyFloatingMember partyMember = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfPartyFloatingMember.class);

            JcxfPartyMemberVo member = jcxfPartyMemberMapper.selectPartyMemberVoById(partyMember.getPartyId());

            if (member != null) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("floating_back", "0");
                queryWrapper.eq("party_id", partyMember.getPartyId());
                queryWrapper.eq("del_flag", false);

                JcxfPartyFloatingMember floatingMember = jcxfPartyFloatingMemberMapper.selectOne(queryWrapper);
                if (floatingMember != null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"已存在流出信息");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到党员信息");
            }

            partyMember.setFloatingBack("0");
            partyMember.setDelFlag(false);
            partyMember.setCreateDate(new Date());
            partyMember.setIdcard(AESUtil2.decrypt(member.getIdcard()));
            partyMember.setPhone(AESUtil2.decrypt(member.getPhone()));

            // 补全区域信息
            JcxfSysArea area = jcxfSysAreaMapper.selectById(partyMember.getAreaId());
            if (area != null) {
                partyMember.setFloatingPlace(area.getMergerName());

                if (Integer.parseInt(area.getLevel()) > 0) {
                    partyMember.setFloatingProvince(area.getMergerIds().split(",")[1]);
                }

                if (Integer.parseInt(area.getLevel()) > 1) {
                    String floatingCity = area.getMergerIds().split(",")[2];
                    JcxfSysArea cityArea = jcxfSysAreaMapper.selectById(floatingCity);
                    if (cityArea != null) {
                        partyMember.setFloatingCity(floatingCity);
                        partyMember.setCityLatitude(new BigDecimal(cityArea.getLat()));
                        partyMember.setCityLongitude(new BigDecimal(cityArea.getLng()));
                    }
                }

            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到区域信息");
            }

            jcxfPartyFloatingMemberMapper.insert(partyMember);

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", partyMember.getPartyId());
            updateWrapper.set("is_flow", "1");
            updateWrapper.set("del_flag", 3);//流出
            updateWrapper.set("flow_place", partyMember.getFloatingPlace());
            jcxfPartyMemberMapper.update(null, updateWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
        }
    }

    @Override
    public Map<String, Object> updateFloatingMember(Map<String, Object> requestMap) {
        try {
            JcxfPartyFloatingMember partyMember = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfPartyFloatingMember.class);

            if (partyMember.getAreaId() == null && StringUtils.isBlank(partyMember.getFloatingPlace())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"请选择区域");
            }
            if (partyMember.getAreaId() != null) {
                // 补全区域信息
                JcxfSysArea area = jcxfSysAreaMapper.selectById(partyMember.getAreaId());
                if (area != null) {
                    partyMember.setFloatingPlace(area.getMergerName());

                    if (Integer.parseInt(area.getLevel()) > 0) {
                        partyMember.setFloatingProvince(area.getMergerIds().split(",")[1]);
                    }

                    if (Integer.parseInt(area.getLevel()) > 1) {
                        String floatingCity = area.getMergerIds().split(",")[2];
                        JcxfSysArea cityArea = jcxfSysAreaMapper.selectById(floatingCity);
                        if (cityArea != null) {
                            partyMember.setFloatingCity(floatingCity);
                            partyMember.setCityLatitude(new BigDecimal(cityArea.getLat()));
                            partyMember.setCityLongitude(new BigDecimal(cityArea.getLng()));
                        }
                    }

                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到区域信息");
                }
            }

            jcxfPartyFloatingMemberMapper.updateById(partyMember);

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", partyMember.getPartyId());
            updateWrapper.set("is_flow", "1");
            updateWrapper.set("del_flag", 3);//流出
            updateWrapper.set("flow_place", partyMember.getFloatingPlace());

            jcxfPartyMemberMapper.update(null, updateWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"提交失败");
        }
    }

    @Override
    public Map<String, Object> delFloatingMember(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("del_flag", 1);
            jcxfPartyFloatingMemberMapper.update(null, updateWrapper);

            JcxfPartyFloatingMember jcxfPartyFloatingMember = jcxfPartyFloatingMemberMapper.selectById(id);
            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", jcxfPartyFloatingMember.getPartyId());
            up.set("is_flow", "0");
            up.set("del_flag", 0);
            up.set("flow_place", null);
            jcxfPartyMemberMapper.update(null, up);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());


        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
        }
    }

    @Override
    public Map<String, Object> setReturnPartyMember(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("party_id", id);
            updateWrapper.set("floating_back", "1");
            jcxfPartyFloatingMemberMapper.update(null, updateWrapper);

            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", id);
            up.set("is_flow", "0");
            up.set("del_flag", 0);
            jcxfPartyMemberMapper.update(null, up);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> getPartyFloatingListByPartyMemberId(Map<String, Object> requestMap) {
        try {
            String partyMemberId = (String) requestMap.get("partyMemberId");
            List<JcxfPartyFloatingMember> floatingMember = jcxfPartyFloatingMemberMapper.getPartyFloatingListByPartyMemberId(partyMemberId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(floatingMember));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"获取异常");
        }
    }

}

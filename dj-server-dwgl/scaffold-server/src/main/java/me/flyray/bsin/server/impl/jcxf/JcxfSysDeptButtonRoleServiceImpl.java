package me.flyray.bsin.server.impl.jcxf;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.JcxfSysDeptButtonRoleService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.PageVo;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JcxfSysDeptButtonRoleServiceImpl implements JcxfSysDeptButtonRoleService {

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;


    @Override
    public Map<String, Object> getDeptButtonRoleByPage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);

            String deptName = (String) dataMap.get("deptName");
            String buttonRole = (String) dataMap.get("buttonRole");

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
            IPage<JcxfSysDept> pageData = new Page<>(page, limit);

            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.isNotNull("button_role");
            queryWrapper.ne("button_role", "");
            queryWrapper.eq("del_flag", 0);

            if (StringUtils.isNotBlank(deptName)) {
                QueryWrapper q1 = new QueryWrapper();
                q1.select("id");
                q1.eq("del_flag", 0);
                q1.like("name", deptName);
                List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(q1);
                if (deptList == null || deptList.size() > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page()));
                }

                List<Integer> ids = new ArrayList<>();
                for (JcxfSysDept dept: deptList) {
                    ids.add(dept.getId());
                }
                queryWrapper.in("id", ids);
            }

            if (StringUtils.isNotBlank(buttonRole)) {
                queryWrapper.and(q -> q.like("button_role", ","+buttonRole+",")
                        .or().likeRight("button_role", buttonRole+",")
                        .or().likeLeft("button_role", ","+buttonRole+""));
            }

            IPage<JcxfSysDept> result = jcxfSysDeptMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> updateDeptButtonRole(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String buttonRole = (String) requestMap.get("buttonRole");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("button_role", buttonRole);
            int count = jcxfSysDeptMapper.update(null, updateWrapper);
            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }

    @Override
    public Map<String, Object> addDeptButtonRole(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String buttonRole = (String) requestMap.get("buttonRole");

            JcxfSysDept dept = jcxfSysDeptMapper.selectById(id);
            if (dept == null || dept.getDelFlag().intValue() != 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到该部门信息");
            }

            if (dept.getButtonRole().length() > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该部门已有权限信息");
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("button_role", buttonRole);
            int count = jcxfSysDeptMapper.update(null, updateWrapper);
            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }
    }
}

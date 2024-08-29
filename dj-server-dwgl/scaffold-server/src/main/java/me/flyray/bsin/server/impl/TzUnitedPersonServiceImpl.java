package me.flyray.bsin.server.impl;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.TzUnitedPersonService;
import me.flyray.bsin.facade.service.TzXxqgService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.mapper.TzUnitedDeptMapper;
import me.flyray.bsin.server.mapper.TzUnitedPersonMapper;
import me.flyray.bsin.server.mapper.TzXxqgMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzUnitedPersonServiceImpl extends ServiceImpl<TzUnitedPersonMapper, TzUnitedPerson> implements TzUnitedPersonService {
    @Autowired
    private TzUnitedPersonMapper tzUnitedPersonMapper;

    @Autowired
    private TzUnitedDeptMapper tzUnitedDeptMapper;


    @Override
    public Map<String, Object> addUnitedPerson(Map<String, Object> requestMap) {
        try {
            TzUnitedPerson tzUnitedPerson = JSON.parseObject(JSON.toJSONString(requestMap.get("unitedPerson")), TzUnitedPerson.class);
            if (tzUnitedPerson.getId() == null) {
                tzUnitedPerson.setDelFlag(0);
                tzUnitedPerson.setCreateTime(new Date());
                int insert = tzUnitedPersonMapper.insert(tzUnitedPerson);
                if (insert > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            } else {
                tzUnitedPerson.setUpdateTime(new Date());
                int i = tzUnitedPersonMapper.updateById(tzUnitedPerson);
                if (i > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
        }
    }

    @Override
    public Map<String, Object> queryUnitedPersonList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        TzUnitedPerson tzUnitedPerson = JSON.parseObject(JSON.toJSONString(dataMap.get("unitedPerson")), TzUnitedPerson.class);
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
        try {
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

            List<Integer> deptIds = tzUnitedDeptMapper.selectChildrenIdsById(tzUnitedPerson.getUnitedId());
            deptIds.add(tzUnitedPerson.getUnitedId());

            IPage<TzUnitedPerson> pageData = new Page<>(page, limit);
            MPJQueryWrapper<TzUnitedPerson> queryWrapper = new MPJQueryWrapper<>();
            queryWrapper.selectAll(TzUnitedPerson.class);
            queryWrapper.eq("t.united_object", tzUnitedPerson.getUnitedObject())
                    .in("t.united_id", deptIds)
                    .eq("t.del_flag", 0);
            queryWrapper.select("d.name as united_name");
            queryWrapper.leftJoin("tz_united_dept d on d.id = t.united_id");
            IPage<TzUnitedPerson> result = tzUnitedPersonMapper.selectJoinPage(pageData, TzUnitedPerson.class, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getUnitePerson(Map<String, Object> requestMap) {
        try {
            Integer id = (Integer) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            TzUnitedPerson tzUnitedPerson = tzUnitedPersonMapper.selectById(id);
            if (tzUnitedPerson == null || tzUnitedPerson.getDelFlag().intValue() != 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到数据");
            }
            TzUnitedDept dept = tzUnitedDeptMapper.selectById(tzUnitedPerson.getUnitedId());
            if (dept != null) {
                tzUnitedPerson.setUnitedName(dept.getName());
            } else {
                tzUnitedPerson.setUnitedName("");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedPerson));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> deleteUnitePerson(Map<String, Object> requestMap) {
        try {
            List<String> ids = new ArrayList<>();
            for (Object id : (List<?>) requestMap.get("ids")) {
                ids.add(String.valueOf(id));
            }
            if (ids == null || ids.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }

            UpdateWrapper<TzUnitedPerson> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tz_united_person.id", ids);
            updateWrapper.set("del_flag", 1);
            int update = tzUnitedPersonMapper.update(null, updateWrapper);
            if (update > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> addUnitedPersonList(Map<String, Object> requestMap) {
        try {
            List<Object> list = (List<Object>) requestMap.get("unitedPersonList");
            if (list == null || list.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"数据为空，添加失败");
            }
            Integer correct = 0;
            Integer unCorrect = 0;
            for (Object o : list) {
                TzUnitedPerson tzUnitedPerson = JSON.parseObject(JSON.toJSONString(o), TzUnitedPerson.class);
                tzUnitedPerson.setDelFlag(0);
                tzUnitedPerson.setCreateTime(new Date());
                int insert = tzUnitedPersonMapper.insert(tzUnitedPerson);
                if (insert > 0) {
                    correct++;
                } else {
                    unCorrect++;
                }
            }
            Map<String, Integer> map = new HashMap<>();
            map.put("success", correct);
            map.put("error", unCorrect);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存异常");
        }

    }

    @Override
    public Map<String, Object> getPersonNumberByUnitedObject(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");

            String unitedId = (String) map.get("unitedId");
            // 获取所有下级
            /*QueryWrapper q = new QueryWrapper();
            q.select("id");
            q.eq("del_flag", 0);
            q.like("parent_ids", "%,"+ unitedId +",%");
            List<TzUnitedDept> tzUnitedDeptList = tzUnitedDeptMapper.selectList(q);
            List<Integer> ids = new ArrayList();
            for (TzUnitedDept dept: tzUnitedDeptList) {
                ids.add(dept.getId());
            }
            ids.add(Integer.parseInt(unitedId));*/

            List<Integer> ids = tzUnitedDeptMapper.selectChildrenIdsById(Integer.parseInt(unitedId));
            ids.add(Integer.parseInt(unitedId));

            List<String> objectList = (List<String>) map.get("objectList");
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("count(*) as num, united_object as unitedObject");
            queryWrapper.in("united_object", objectList);
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("united_id", ids);
            // queryWrapper.eq("united_id", unitedId);
            queryWrapper.groupBy("united_object");
            List<Map<String, Object>> list = tzUnitedPersonMapper.selectMaps(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }
}

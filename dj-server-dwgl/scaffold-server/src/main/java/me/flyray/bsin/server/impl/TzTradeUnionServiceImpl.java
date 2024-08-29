package me.flyray.bsin.server.impl;

import cn.hutool.core.io.IORuntimeException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import me.flyray.bsin.facade.service.TzTradeUnionService;
import me.flyray.bsin.facade.service.TzXxqgService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzTradeActiveMapper;
import me.flyray.bsin.server.mapper.TzTradeUnionMapper;
import me.flyray.bsin.server.mapper.TzXxqgMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
public class TzTradeUnionServiceImpl extends ServiceImpl<TzTradeUnionMapper, TzTradeUnion> implements TzTradeUnionService {
    @Autowired
    private TzTradeUnionMapper tzTradeUnionMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzTradeActiveMapper tzTradeActiveMapper;

    @Override
    public Map<String, Object> getParentDeptById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员！");
            }

            TzTradeUnion tzTradeUnion = tzTradeUnionMapper.selectById(id);
            if (tzTradeUnion != null && tzTradeUnion.getDelFlag().intValue() == 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parent_id", id);
                int count = tzTradeUnionMapper.selectCount(queryWrapper);
                if (count > 0) {
                    tzTradeUnion.setLeaf(false);
                } else {
                    tzTradeUnion.setLeaf(true);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnion));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getParentDeptByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            if (StringUtils.isBlank(deptId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未获取到部门信息");
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("del_flag", 0);
            TzTradeUnion tzTradeUnion = tzTradeUnionMapper.selectOne(queryWrapper);
            if (tzTradeUnion != null) {
                QueryWrapper q2 = new QueryWrapper();
                q2.eq("parent_id", tzTradeUnion.getId());
                q2.eq("del_flag", 0);
                int count = tzTradeUnionMapper.selectCount(q2);
                if (count > 0) {
                    tzTradeUnion.setLeaf(false);
                } else {
                    tzTradeUnion.setLeaf(true);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnion));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getTradeUnionDeptByName(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String name = (String) requestMap.get("name");

            if (StringUtils.isNotBlank(name)) {
                QueryWrapper q = new QueryWrapper();
                q.eq("dept_id", deptId);
                q.eq("del_flag", 0);
                TzTradeUnion tzTradeUnion = tzTradeUnionMapper.selectOne(q);
                if (tzTradeUnion != null) {
                    QueryWrapper queryWrapper = new QueryWrapper();
                    queryWrapper.like("name", name);
                    queryWrapper.like("parent_ids", "," + tzTradeUnion.getId() + ",");
                    queryWrapper.eq("del_flag", 0);
                    List<TzTradeUnion> tzTradeUnionList = tzTradeUnionMapper.selectList(queryWrapper);
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnionList));
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
                }
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getTradeUnionDeptList(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
            }
            QueryWrapper<TzTradeUnion> q = new QueryWrapper<>();
            q.eq("tz_trade_union.parent_id", id);
            q.eq("del_flag", 0);
            List<TzTradeUnion> tzTradeUnions = tzTradeUnionMapper.selectList(q);
            for (TzTradeUnion tzTradeUnion : tzTradeUnions) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parent_id", tzTradeUnion.getId());
                queryWrapper.eq("del_flag", 0);
                int count = tzTradeUnionMapper.selectCount(queryWrapper);
                if (count > 0) {
                    tzTradeUnion.setLeaf(false);
                } else {
                    tzTradeUnion.setLeaf(true);
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnions));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }


    @Override
    public Map<String, Object> determineNode(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");//当前的叶子节点id
        String deptId = (String) requestMap.get("deptId");//当前账号的的部门id


        TzTradeUnion tzTradeUnion1 = tzTradeUnionMapper.selectById(id);

        //如果叶子节点的部门id和当前账号部门id相等，那么当前节点的数据也可以查看
        if (tzTradeUnion1.getDeptId() != null && tzTradeUnion1.getDeptId().toString().equals(deptId)) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
        }
        //获取当前节点的父级id集合
        String parentIds = tzTradeUnion1.getParentIds();

        QueryWrapper<TzTradeUnion> q = new QueryWrapper<>();
        q.eq("tz_trade_union.dept_id", deptId);
        List<TzTradeUnion> unionList = tzTradeUnionMapper.selectList(q);
        if (unionList == null) {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(false));
        }
        //获取当前登录部门对应的树节点id集合
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < unionList.size(); i++) {
            stringList.add(unionList.get(i).getId().toString());
        }
        //判断如果当前节点的父级id集合包含有当前登录部门对应的树节点id集合，证明当前节点是当前登录部门id的字级
        boolean containsValue = false;
        for (String value : stringList) {
            if (parentIds.contains(value + ",")) {
                containsValue = true;
                break;
            }
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(containsValue));
    }

    @Override
    public Map<String, Object> getTradeUnion(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (id == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数不能为空");
        }
        TzTradeUnion tzTradeUnion = tzTradeUnionMapper.selectById(id);
        if (tzTradeUnion != null) {
            if(tzTradeUnion.getDelFlag() == 0){
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzTradeUnion.getDeptId());
                if (dept != null && dept.getDelFlag() == 0) {
                    tzTradeUnion.setJcxfSysDept(dept);
                }
                TzTradeUnion union = tzTradeUnionMapper.selectById(tzTradeUnion.getParentId());
                if (union != null) {
                    tzTradeUnion.setParentName(union.getName());
                } else {
                    tzTradeUnion.setParentName("");
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnion));
            }else{
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未查询到数据");
        }

    }

    @Override
    public Map<String, Object> addTradeUnion(Map<String, Object> requestMap) {
        try {
            TzTradeUnion tzTradeUnion = JSON.parseObject(JSON.toJSONString(requestMap.get("trade")), TzTradeUnion.class);
            if (tzTradeUnion.getParentId() == null) {
                tzTradeUnion.setParentId(0L);
                tzTradeUnion.setParentIds("0");
            }
            // 处理绑定部门已失效的情况
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzTradeUnion.getDeptId());
            if (dept == null || dept.getDelFlag().intValue() != 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未查询到所选部门信息，请刷新重试");
            } else {
                // 如果部门已被绑定
                QueryWrapper qk = new QueryWrapper();
                qk.eq("dept_id", tzTradeUnion.getDeptId());
                qk.eq("del_flag", 0);
                if (tzTradeUnion.getId() != null) {
                    qk.ne("id", tzTradeUnion.getId());
                }
                int count1 = tzTradeUnionMapper.selectCount(qk);
                if (count1 > 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该部门已被机构绑定，请重新选择");
                }

                // 如果存在上级机构所绑定部门失效
                Long parentId = tzTradeUnion.getParentId();
                if (parentId.intValue() != 0) {
                    TzTradeUnion parentTradeUnion = tzTradeUnionMapper.selectById(parentId);
                    String[] unitedIds = parentTradeUnion.getParentIds().split(",");
                    QueryWrapper qn = new QueryWrapper();
                    qn.select("dept_id");
                    qn.in("id", unitedIds);
                    qn.eq("del_flag", 0);
                    List<TzTradeUnion> tzTradeUnionList = tzTradeUnionMapper.selectList(qn);
                    List<Long> deptIds = new ArrayList<>();
                    for (TzTradeUnion tzTradeUnion1 : tzTradeUnionList) {
                        deptIds.add(tzTradeUnion1.getDeptId());
                    }
                    deptIds.add(parentTradeUnion.getDeptId());

                    QueryWrapper q = new QueryWrapper();
                    q.in("id", deptIds);
                    q.eq("del_flag", 1);
                    int count = jcxfSysDeptMapper.selectCount(q);
                    if (count > 0) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在上级机构绑定部门失效，请先修改上级机构信息");
                    }
                    tzTradeUnion.setParentIds(parentTradeUnion.getParentIds() + parentId + ",");
                }

            }

            if (tzTradeUnion.getId() == null) {
                tzTradeUnion.setDelFlag(0);
                tzTradeUnion.setCreateTime(new Date());
                int insert = tzTradeUnionMapper.insert(tzTradeUnion);
                if (insert > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnion));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
                }
            } else {
                tzTradeUnion.setUpdateTime(new Date());
                int i = tzTradeUnionMapper.updateById(tzTradeUnion);
                if (i > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzTradeUnion));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
        }

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteTrade(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员！");
            }

            QueryWrapper q = new QueryWrapper();
            q.select("id");
            q.like("parent_ids", "%," + id + ",%");
            q.eq("del_flag", 0);
            List<TzTradeUnion> tzTradeUnionList = tzTradeUnionMapper.selectList(q);
            List<Integer> ids = new ArrayList<>();
            for (TzTradeUnion tzTradeUnion : tzTradeUnionList) {
                ids.add(tzTradeUnion.getId());
            }
            ids.add(Integer.parseInt(id));

            UpdateWrapper<TzTradeUnion> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tz_trade_union.id", ids);
            updateWrapper.set("del_flag", 1);
            tzTradeUnionMapper.update(null, updateWrapper);

            UpdateWrapper up1 = new UpdateWrapper();
            up1.in("trade_id", ids);
            up1.set("del_flag", 1);
            tzTradeActiveMapper.update(null, up1);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());


        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除异常");
        }
    }
}

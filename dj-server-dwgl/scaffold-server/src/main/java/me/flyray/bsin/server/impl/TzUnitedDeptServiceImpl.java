package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzUnitedDeptService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.domain.TzUnitedDept;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzUnitedDeptMapper;
import me.flyray.bsin.server.mapper.TzUnitedPersonMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
public class TzUnitedDeptServiceImpl extends ServiceImpl<TzUnitedDeptMapper, TzUnitedDept> implements TzUnitedDeptService {
    @Autowired
    private TzUnitedDeptMapper tzUnitedDeptMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzUnitedPersonMapper tzUnitedPersonMapper;

    @Override
    public Map<String, Object> getParentDeptById(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
        }
        try {
            TzUnitedDept tzUnitedDept = tzUnitedDeptMapper.selectById(id);
            if (tzUnitedDept != null && tzUnitedDept.getDelFlag().intValue() == 0) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzUnitedDept.getDeptId());
                if (dept != null && dept.getDelFlag().intValue() == 0) {
                    tzUnitedDept.setJcxfSysDept(dept);
                }

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parent_id", id);
                int count = tzUnitedDeptMapper.selectCount(queryWrapper);
                if (count > 0) {
                    tzUnitedDept.setLeaf(false);
                } else {
                    tzUnitedDept.setLeaf(true);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedDept));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }

    @Override
    public Map<String, Object> getParentDeptByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            if (StringUtils.isBlank(deptId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("del_flag", 0);
            TzUnitedDept tzUnitedDept = tzUnitedDeptMapper.selectOne(queryWrapper);
            if (tzUnitedDept != null) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(tzUnitedDept.getDeptId());
                if (dept != null && dept.getDelFlag().intValue() == 0) {
                    tzUnitedDept.setJcxfSysDept(dept);
                }

                QueryWrapper q2 = new QueryWrapper();
                q2.eq("parent_id", tzUnitedDept.getId());
                q2.eq("del_flag", 0);
                int count = tzUnitedDeptMapper.selectCount(q2);
                if (count > 0) {
                    tzUnitedDept.setLeaf(false);
                } else {
                    tzUnitedDept.setLeaf(true);
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedDept));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> getUnitedDeptList(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            QueryWrapper<TzUnitedDept> q = new QueryWrapper<>();
            q.eq("parent_id", id);
            q.eq("del_flag", 0);
            List<TzUnitedDept> tzUnitedDepts = tzUnitedDeptMapper.selectList(q);
            for (TzUnitedDept tzUnitedDept: tzUnitedDepts) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parent_id", tzUnitedDept.getId());
                queryWrapper.eq("del_flag", 0);
                int count = tzUnitedDeptMapper.selectCount(queryWrapper);
                if (count > 0) {
                    tzUnitedDept.setLeaf(false);
                } else {
                    tzUnitedDept.setLeaf(true);
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedDepts));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }
    }

    @Override
    public Map<String, Object> determineNode(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");//当前的叶子节点id
            String deptId = (String) requestMap.get("deptId");//当前账号的的部门id
            QueryWrapper<TzUnitedDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_united_dept.id", id);
            TzUnitedDept tzUnitedDept1 = tzUnitedDeptMapper.selectOne(queryWrapper);
            //如果叶子节点的部门id和当前账号部门id相等，那么当前节点的数据也可以查看
            if (tzUnitedDept1.getDeptId() != null && tzUnitedDept1.getDeptId().toString().equals(deptId)) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(true));
            }
            //获取当前节点的父级id集合
            String parentIds = tzUnitedDept1.getParentIds();

            QueryWrapper<TzUnitedDept> q = new QueryWrapper<>();
            q.eq("tz_united_dept.dept_id", deptId);
            List<TzUnitedDept> unionList = tzUnitedDeptMapper.selectList(q);
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询失败");
        }

    }

    @Override
    public Map<String, Object> getUnitedDept(Map<String, Object> requestMap) {
        try {
            Integer id = (Integer) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空");
            }
            TzUnitedDept tzUnitedDept = tzUnitedDeptMapper.selectById(id);
            if (tzUnitedDept == null || tzUnitedDept.getDelFlag() == 0) {
                JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectById(tzUnitedDept.getDeptId());
                if (jcxfSysDept != null && jcxfSysDept.getDelFlag() == 0) {
                    tzUnitedDept.setJcxfSysDept(jcxfSysDept);
                }

                TzUnitedDept dept = tzUnitedDeptMapper.selectById(tzUnitedDept.getParentId());
                if (dept != null) {
                    tzUnitedDept.setParentName(dept.getName());
                } else {
                    tzUnitedDept.setParentName("");
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedDept));
            }
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询出错，请刷新重试");
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }

    }

    @Override
    public Map<String, Object> addUnitedDept(Map<String, Object> requestMap) {
        try {
            TzUnitedDept unitedDept = JSON.parseObject(JSON.toJSONString(requestMap.get("unitedDept")), TzUnitedDept.class);
            if(unitedDept.getParentId() == null) {
                unitedDept.setParentId(0L);
                unitedDept.setParentIds("0");
            }

            // 处理绑定部门已失效的情况
            JcxfSysDept dept = jcxfSysDeptMapper.selectById(unitedDept.getDeptId());
            if (dept == null || dept.getDelFlag().intValue() != 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未查询到所选部门信息，请刷新重试");
            } else {
                // 如果部门已被绑定
                QueryWrapper qk = new QueryWrapper();
                qk.eq("dept_id", unitedDept.getDeptId());
                qk.eq("del_flag", 0);
                if (unitedDept.getId() != null) {
                    qk.ne("id", unitedDept.getId());
                }
                int count1 = tzUnitedDeptMapper.selectCount(qk);
                if (count1 > 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"该部门已被机构绑定，请重新选择");
                }

                // 如果存在上级机构所绑定部门失效
                Long parentId = unitedDept.getParentId();
                if (parentId.intValue() != 0) {
                    TzUnitedDept parentTradeUnion = tzUnitedDeptMapper.selectById(parentId);
                    String[] unitedIds = parentTradeUnion.getParentIds().split(",");
                    QueryWrapper qn = new QueryWrapper();
                    qn.select("dept_id");
                    qn.in("id", unitedIds);
                    qn.eq("del_flag", 0);
                    List<TzUnitedDept> tzUnitedDeptList = tzUnitedDeptMapper.selectList(qn);
                    List<Long> deptIds = new ArrayList<>();
                    for (TzUnitedDept tzUnitedDept: tzUnitedDeptList) {
                        deptIds.add(tzUnitedDept.getDeptId());
                    }
                    deptIds.add(parentTradeUnion.getDeptId());

                    QueryWrapper q = new QueryWrapper();
                    q.in("id", deptIds);
                    q.eq("del_flag", 1);
                    int count = jcxfSysDeptMapper.selectCount(q);
                    if (count > 0) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在上级机构绑定部门失效，请先修改上级机构信息");
                    }
                    unitedDept.setParentIds(parentTradeUnion.getParentIds() + parentId + ",");
                }

            }

            if (unitedDept.getId() == null) {
                /*Long parentId = unitedDept.getParentId();
                TzUnitedDept parentTradeUnion = tzUnitedDeptMapper.selectById(parentId);
                String parentIds = parentTradeUnion.getParentIds();
                unitedDept.setParentIds(parentIds + parentId + ",");*/

                unitedDept.setDelFlag(0);
                unitedDept.setCreateTime(new Date());
                int insert = tzUnitedDeptMapper.insert(unitedDept);
                if (insert > 0) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL),"保存失败");
                }
            } else {
                unitedDept.setUpdateTime(new Date());
                int i = tzUnitedDeptMapper.updateById(unitedDept);
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> deleteUnitedDept(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (org.apache.commons.lang.StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"参数为空，请联系管理员！");
            }

            QueryWrapper q = new QueryWrapper();
            q.select("id");
            q.like("parent_ids", "%,"+ id +",%");
            q.eq("del_flag", 0);
            List<TzUnitedDept> tzUnitedDeptList = tzUnitedDeptMapper.selectList(q);
            List<Integer> ids = new ArrayList<>();
            for (TzUnitedDept tzUnitedDept: tzUnitedDeptList) {
                ids.add(tzUnitedDept.getId());
            }
            ids.add(Integer.parseInt(id));

            UpdateWrapper<TzUnitedDept> updateWrapper = new UpdateWrapper<>();
            updateWrapper.in("tz_united_dept.id", ids);
            updateWrapper.set("del_flag", 1);
            tzUnitedDeptMapper.update(null, updateWrapper);

            UpdateWrapper up = new UpdateWrapper();
            up.in("united_id", ids);
            up.set("del_flag", 1);
            tzUnitedPersonMapper.update(null, up);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"删除异常");
        }
    }

    @Override
    public Map<String, Object> getUnitedDeptByName(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String name = (String) requestMap.get("name");
            if (StringUtils.isNotBlank(name)) {
                QueryWrapper q = new QueryWrapper();
                q.eq("dept_id", deptId);
                q.eq("del_flag", 0);
                TzUnitedDept tzUnitedDept = tzUnitedDeptMapper.selectOne(q);

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.like("name", name);
                queryWrapper.like("parent_ids", ","+ tzUnitedDept.getId() +",");
                queryWrapper.eq("del_flag", 0);
                List<TzUnitedDept> tzUnitedDeptList = tzUnitedDeptMapper.selectList(queryWrapper);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzUnitedDeptList));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询异常");
        }
    }
}

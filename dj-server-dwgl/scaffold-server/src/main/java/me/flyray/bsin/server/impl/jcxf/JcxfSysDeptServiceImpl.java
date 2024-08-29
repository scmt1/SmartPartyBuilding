package me.flyray.bsin.server.impl.jcxf;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.util.StringUtil;
import me.flyray.bsin.facade.service.JcxfSysDeptService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.*;
import me.flyray.bsin.server.domain.vo.DeptTree;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.BeanUtils;
import me.flyray.bsin.server.utils.BuildTreeUtils;
import me.flyray.bsin.server.utils.jcxf.PinYinUtil;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@DS(value = "jcxf")
public class JcxfSysDeptServiceImpl extends ServiceImpl<JcxfSysDeptMapper, JcxfSysDept> implements JcxfSysDeptService {

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfTermDeptMapper jcxfTermDeptMapper;

    @Autowired
    private TzTwoBestOneFirstMapper tzTwoBestOneFirstMapper;

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Autowired
    private JcxfSysUserMapper jcxfSysUserMapper;

    @Autowired
    private JcxfBranchTaskAssignMapper jcxfBranchTaskAssignMapper;

    @Autowired
    private JcxfBranchTaskExecuteMapper jcxfBranchTaskExecuteMapper;

    @Autowired
    private JcxfDeptPositionMapper jcxfDeptPositionMapper;

    @Autowired
    private JcxfDeptPositionPhotoMapper jcxfDeptPositionPhotoMapper;

    @Autowired
    private JcxfDevelopPartyMapper jcxfDevelopPartyMapper;

    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;


    /**
     * 分页查询示例
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getTzSysDeptNameList(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            List<JcxfSysDept> list = jcxfSysDeptMapper.getNameList(deptId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }


    @Override
    public Map<String, Object> queryTzSysDeptList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfSysDept tzSysDept = JSON.parseObject(JSON.toJSONString(dataMap.get("tzSysDept")), JcxfSysDept.class);
        SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("searchVo")), SearchVo.class);
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
            tzSysDept.setDelFlag(0);
            Page<JcxfSysDept> pageData = new Page<>(page, limit);
            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            if (tzSysDept != null) {
                queryWrapper = LikeAllField(tzSysDept, searchVo);
            }
            queryWrapper.and(i -> i.like("t.parent_ids", "," + String.valueOf(tzSysDept.getId()))
                    .or(i3 -> i3.eq("t.id", tzSysDept.getId())));


            queryWrapper.groupBy("t.id");
            queryWrapper.orderByAsc( "id");

            IPage<JcxfSysDept> result = jcxfSysDeptMapper.getJcxfDeptByPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> queryTzSysDeptLowerLevelList(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfSysDept tzSysDept = JSON.parseObject(JSON.toJSONString(dataMap.get("tzSysDept")), JcxfSysDept.class);
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
            tzSysDept.setDelFlag(0);
            Page<JcxfSysDept> pageData = new Page<>(page, limit);
            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", tzSysDept.getParentId());
            queryWrapper.eq("del_flag", 0);
            queryWrapper.orderByAsc("parent_id");
            queryWrapper.orderByAsc("create_date");
            IPage<JcxfSysDept> result = jcxfSysDeptMapper.getJcxfDeptLowerLeveByPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> queryIsLeafById(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            Integer integer = jcxfSysDeptMapper.queryIsLeafById(Long.valueOf(deptId));
            Boolean isLeaf;
            if (integer > 0) {
                //证明不是叶子节点
                isLeaf = false;
            } else {
                isLeaf = true;
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(isLeaf));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getTzSysDeptList(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        List<JcxfSysDept> list = jcxfSysDeptMapper.getParentDeptListById(deptId);

        // 判断是否有党员或者组织本身通过了两优一先
        for (JcxfSysDept tzSysDept : list) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("create_dept_id", tzSysDept.getId());
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("status", "6");
            int count = tzTwoBestOneFirstMapper.selectCount(queryWrapper);
            if (count > 0) {
                tzSysDept.setRedFlag(true);
            } else {
                tzSysDept.setRedFlag(false);
            }
        }

        return RespBodyHandler.setRespBodyListDto(list);
    }

    @Override
    public void exportProvince(Map<String, Object> requestMap) {
        /*try {
            ShopEmployee shopEmployee = shopEmployeeService.getShopEmployeeById(SecurityUtils.getShopUser().getEmployeeId());
            String tenantId = shopEmployee.getTenantId();
            sysDept.setTenantId(tenantId);
            tzSysDeptService.exportProvince(response, sysDept, searchVo);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    @DSTransactional
    public Map<String, Object> deleteTzSysDept(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员");
        }

        QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sys_dept.parent_id", Long.valueOf(id));
        queryWrapper.eq("del_flag", false);
        int count = jcxfSysDeptMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请先删除该机构的下级机构后再操作");
        }

        QueryWrapper q1 = new QueryWrapper();
        q1.eq("dept_id", Long.valueOf(id));
        q1.eq("del_flag", false);
        int users = jcxfSysUserMapper.selectCount(q1);
        if (users > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该机构已添加管理员账号，请删除管理员账号后再操作");
        }

        List<Integer> ids = new ArrayList<>();
        ids.add(4); // 停止党籍
        ids.add(5); // 开除党籍
        ids.add(6); // 已死亡
        QueryWrapper q2 = new QueryWrapper();
        q2.eq("dept_id", Long.valueOf(id));
        q2.eq("del_flag", false);
        q2.ne("is_lost", 1);
        q2.notIn("party_state", ids);
        int partyMembers = jcxfPartyMemberMapper.selectCount(q2);
        if (partyMembers > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该机构已添加党员，请删除党员后再操作");
        }


        UpdateWrapper<JcxfSysDept> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("sys_dept.del_flag", 1).in("sys_dept.id", id);
        int update = jcxfSysDeptMapper.update(null, updateWrapper);
        if (update > 0) {
            /*删除任务分配表信息*/
            deleteTaskRelated(Long.valueOf(id));
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除失败");
        }

    }

    /**
     * 删除相关的任务清单信息
     *
     * @param deptId
     */
    public void deleteTaskRelated(Long deptId) {
        /*删除任务分配表信息*/
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("dept_id", deptId);
        queryWrapper.eq("del_flag", 0);

        List<JcxfBranchTaskAssign> listAssign = jcxfBranchTaskAssignMapper.selectList(queryWrapper);
        if (listAssign != null && listAssign.size() > 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("dept_id", deptId);
            updateWrapper.eq("del_flag", 0);
            updateWrapper.set("del_flag", 1);
            jcxfBranchTaskAssignMapper.update(null, updateWrapper);
        }
        /*删除任务执行表信息*/
        QueryWrapper queryWrapper1 = new QueryWrapper();
        queryWrapper1.eq("dept_id", deptId);
        queryWrapper1.eq("del_flag", 0);
        List<JcxfBranchTaskExecute> listExecute = jcxfBranchTaskExecuteMapper.selectList(queryWrapper1);
        if (listExecute != null && listExecute.size() > 0) {
            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("dept_id", deptId);
            updateWrapper.eq("del_flag", 0);
            updateWrapper.set("del_flag", 1);
            jcxfBranchTaskExecuteMapper.update(null, updateWrapper);
        }
    }

    @Override
    public Map<String, Object> getTzSysDept(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员！");
        }
        try {
            JcxfSysDept res = jcxfSysDeptMapper.selectById(id);
            if (res != null) {
                // 判断是否叶子节点
                QueryWrapper q = new QueryWrapper();
                q.eq("del_flag", 0);
                q.eq("parent_id", id);
                int count = jcxfSysDeptMapper.selectCount(q);
                if (count > 0) {
                    res.setLeaf(false);
                } else {
                    res.setLeaf(true);
                }

                // 判断是否有党员或者组织本身通过了两优一先
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("create_dept_id", res.getId());
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("status", "6");
                res.setTzTwoBestOneFirstList(tzTwoBestOneFirstMapper.selectList(queryWrapper));
                if (res.getTzTwoBestOneFirstList() != null && res.getTzTwoBestOneFirstList().size() > 0) {
                    res.setRedFlag(true);
                }

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getTzSysDeptByParentId(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfSysDept tzSysDept = JSON.parseObject(JSON.toJSONString(dataMap.get("tzSysDept")), JcxfSysDept.class);
        if (tzSysDept.getParentId() == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员！");
        }
        try {
            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("parent_id", tzSysDept.getParentId());
            queryWrapper.eq("del_flag", 0);
            queryWrapper.orderByAsc("parent_id");
            queryWrapper.orderByAsc("create_date");
            List<JcxfSysDept> res = jcxfSysDeptMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    @DSTransactional
    public Map<String, Object> addTzSysDept(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("tenantId");
            JcxfSysDept tzSysDept = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfSysDept.class);
            String token = tzSysDept.getToken();
            String parentIds;


            tzSysDept.setTenantId(tenantId);

            // 判断部门类型，若上级是支部，则不能新增，若存在下级，则不能修改为支部
            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);

            if (tzSysDept.getType() != null) {
                if (tzSysDept.getParentId() != null && tzSysDept.getParentId() != 0) {
                    JcxfSysDept deptParent = jcxfSysDeptMapper.selectById(tzSysDept.getParentId());
                    if (deptParent != null && deptParent.getDelFlag() != 0 && typeList.indexOf(tzSysDept.getType()) > -1) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL), "所属上级组织类别为支部");
                    }
                }

                if (tzSysDept.getId() != null) {
                    QueryWrapper q1 = new QueryWrapper();
                    q1.eq("del_flag", 0);
                    q1.eq("parent_id", tzSysDept.getId());
                    int count = jcxfSysDeptMapper.selectCount(q1);
                    if (count > 0 && typeList.indexOf(tzSysDept.getType()) > -1) {
                        throw new BusinessException(String.valueOf(ResponseCode.FAIL), "当前部门存在下级");
                    }
                }
            }
            if(StringUtils.isNotBlank(tzSysDept.getManagePhone())){
                //判断绑定的管理员手机号是否已存在组织绑定
                LambdaQueryWrapper<JcxfSysDept> sysDeptQueryWrapper = new LambdaQueryWrapper<JcxfSysDept>();
                String[] split = tzSysDept.getManagePhone().split(",");
                sysDeptQueryWrapper.eq(JcxfSysDept::getDelFlag, 0);
                sysDeptQueryWrapper.ne(JcxfSysDept::getId, tzSysDept.getId());
                sysDeptQueryWrapper.and(i -> {
                    for (int j = 0; j < split.length; j++) {
                        if(j != split.length - 1) {
                            i.like(JcxfSysDept::getManagePhone,split[j]).or();
                        }else {
                            i.like(JcxfSysDept::getManagePhone,split[j]);
                        }
                    }
                });
                Integer integer = jcxfSysDeptMapper.selectCount(sysDeptQueryWrapper);
                if(integer > 0){
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "管理员手机号重复");
                }
            }

            if (tzSysDept.getId() != null) {
                Long parentId = tzSysDept.getParentId();
                JcxfSysDept byId = jcxfSysDeptMapper.selectById(parentId);
                if (null != byId && byId.getId() != 1L) {
                    tzSysDept.setFinallySort(byId.getFinallySort());
                } else {
                    tzSysDept.setFinallySort(jcxfSysDeptMapper.getNextFinallySort());
                }
                tzSysDept.setFinallySort(tzSysDept.getFinallySort());
                //设置grade层级
                String replace = tzSysDept.getParentIds().replaceAll(",", "");
                int a = tzSysDept.getParentIds().length();
                int b = replace.length();
                tzSysDept.setGrade(String.valueOf(a - b));

                //判断pyName字段是否为空,为空则根据name进行获取拼音首字母
                if (tzSysDept.getPyName() == null || "" .equals(tzSysDept.getPyName())) {
                    tzSysDept.setPyName(PinYinUtil.getFirstSpell(tzSysDept.getName()));
                }

                tzSysDept.setUpdateDate(new Date());
                int res = jcxfSysDeptMapper.updateById(tzSysDept);

                //获取机构信息
                SysOrg orgInfo = jcxfSysDeptMapper.getOrgInfoByOrgId(String.valueOf(tzSysDept.getId()));
                //更新权限部门、机构信息
                jcxfSysDeptMapper.updatePassDept(tzSysDept);
                SysOrg sysOrg = new SysOrg();
                sysOrg.setOrgId(tzSysDept.getId().toString());
                sysOrg.setOrgName(tzSysDept.getName());
                if(orgInfo != null && "-1".equals(orgInfo.getParentId())){
                    sysOrg.setParentId("-1");
                }else{
                    sysOrg.setParentId(tzSysDept.getParentId().toString());
                }
                sysOrg.setTenantId(tzSysDept.getTenantId());
                jcxfSysDeptMapper.updatePassSysOrg(sysOrg);
                if (res > 0) {
                    setDept(tzSysDept);
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
                }
            }

            if (tzSysDept.getParentId() == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "无父级组织，请联系管理员!");
            } else {
                Long parentId = tzSysDept.getParentId();
                JcxfSysDept byId = jcxfSysDeptMapper.selectById(parentId);
                if (byId == null) {
                    parentIds = parentId.toString() + ",";
                } else {
                    String byIdParentIds = byId.getParentIds();
                    parentIds = byIdParentIds + parentId.toString() + ",";
                }
//                parentIds = parentIds.substring(0, parentIds.lastIndexOf(","));
                tzSysDept.setParentIds(parentIds);

                if (null != byId && byId.getId() != 1L) {
                    tzSysDept.setFinallySort(byId.getFinallySort());
                } else {
                    tzSysDept.setFinallySort(jcxfSysDeptMapper.getNextFinallySort());
                }
                tzSysDept.setFinallySort(byId.getFinallySort());
            }

            //设置grade层级
            String replace = tzSysDept.getParentIds().replaceAll(",", "");
            int a = tzSysDept.getParentIds().length();
            int b = replace.length();
            tzSysDept.setGrade(String.valueOf(a - b));

            //判断pyName字段是否为空,为空则根据name进行获取拼音首字母
            if (tzSysDept.getPyName() == null || "" .equals(tzSysDept.getPyName())) {
                tzSysDept.setPyName(PinYinUtil.getFirstSpell(tzSysDept.getName()));
            }

            tzSysDept.setDelFlag(0);
            tzSysDept.setCreateTime(new Date());
            int res = jcxfSysDeptMapper.insert(tzSysDept);
            if(res > 0){
                //往权限库加部门和机构
                jcxfSysDeptMapper.addPaasDept(tzSysDept);
                SysOrg sysOrg = new SysOrg();
                sysOrg.setOrgId(tzSysDept.getId().toString());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String timestamp = dateFormat.format(new Date());
                Random random = new Random();
                int randomNumber = random.nextInt(1000000); // 生成一个0到999999之间的随机数
                String orgCode = timestamp + String.format("%06d", randomNumber);
                sysOrg.setOrgCode(orgCode);
                sysOrg.setOrgName(tzSysDept.getName());
                sysOrg.setParentId(tzSysDept.getParentId().toString());
                sysOrg.setTenantId(tzSysDept.getTenantId());
                sysOrg.setCreateTime(new Date());
                jcxfSysDeptMapper.addPassSysOrg(sysOrg);
            }
            setDept(tzSysDept);

            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), e.getMessage());
        }
    }

    private void setDept(JcxfSysDept jcxfSysDept) {
        //编辑机构时，当所编辑机构的党组织所在单位情况为'与上级党组织相同时'，如果该机构有下级机构，则设置所有下级机构的单位名称、单位类别、单位建立组织情况、
        JcxfSysDictionary dictionary = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyOrgSituation", "与上级党组织相同");
        if (jcxfSysDept.getId() != null && dictionary != null) {
            if (dictionary.getDetail() != null && dictionary.getDetail().equals(String.valueOf(jcxfSysDept.getPartyOrgSituation()))) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.like("parent_ids", "," + jcxfSysDept.getId() + ",");
                queryWrapper.eq("del_flag", false);

                List<JcxfSysDept> childDepts = jcxfSysDeptMapper.selectList(queryWrapper);

                if (childDepts != null && childDepts.size() > 0) {
                    for (JcxfSysDept dept : childDepts) {
                        dept.setPartyOrgSituation(jcxfSysDept.getPartyOrgSituation());
                        dept.setUnitName(jcxfSysDept.getUnitName());
                        dept.setPartyOrgType(jcxfSysDept.getPartyOrgType());
                        dept.setUnitOrgSituation(jcxfSysDept.getUnitOrgSituation());
                        dept.setUnitCode(jcxfSysDept.getUnitCode());
                        String s = dept.getParentIds().replaceAll(",", "");
                        dept.setGrade(String.valueOf(dept.getParentIds().length() - s.length()));
                        jcxfSysDeptMapper.updateById(dept);
                    }
                }
            } else {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("parent_id", jcxfSysDept.getId());
                queryWrapper.eq("del_flag", false);

                List<JcxfSysDept> depts = jcxfSysDeptMapper.selectList(queryWrapper);//当前编辑机构的下级机构

                for (JcxfSysDept dept : depts) {
                    if (null != dept.getPartyOrgSituation() && dept.getPartyOrgSituation().toString().equals(dictionary.getDetail())) {
                        QueryWrapper<JcxfSysDept> queryWrapper1 = new QueryWrapper();
                        queryWrapper1.eq("del_flag", false);
                        queryWrapper1.and(i -> i.eq("parent_id", jcxfSysDept.getId()).or().eq("id", jcxfSysDept.getId()));
                        List<JcxfSysDept> sysDepts = jcxfSysDeptMapper.selectList(queryWrapper1);

                        if (sysDepts != null && sysDepts.size() > 0) {
                            for (JcxfSysDept sysDept : sysDepts) {
                                sysDept.setPartyOrgSituation(Integer.valueOf(dictionary.getDetail()));
                                sysDept.setUnitName(jcxfSysDept.getUnitName());
                                sysDept.setPartyOrgType(jcxfSysDept.getPartyOrgType());
                                sysDept.setUnitOrgSituation(jcxfSysDept.getUnitOrgSituation());
                                sysDept.setUnitCode(jcxfSysDept.getUnitCode());
                                String s = sysDept.getParentIds().replaceAll(",", "");
                                sysDept.setGrade(String.valueOf(sysDept.getParentIds().length() - s.length()));
                                jcxfSysDeptMapper.updateById(dept);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getBaseInfoById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            Map<String, Integer> map = new HashMap<>();

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(id));
            deptIds.add(Long.valueOf(id));

            QueryWrapper q = new QueryWrapper();
            q.eq("del_flag", 0);
            q.in("dept_id", deptIds);
            int count = jcxfPartyMemberMapper.selectCount(q);
            map.put("partyNum", count);

            QueryWrapper q1 = new QueryWrapper();
            q1.eq("del_flag", 0);
            q1.like("parent_ids", "," + id + ",");
            int count1 = jcxfSysDeptMapper.selectCount(q1);
            map.put("downTotal", count1);

            List<Integer> typeList = new ArrayList<>();
            typeList.add(611); // 611 党委
            typeList.add(621); // 621 党总支部
            typeList.add(631); // 631 党支部
            typeList.add(632); // 632 联合党支部

            QueryWrapper q2 = new QueryWrapper();
            q2.eq("del_flag", 0);
            q2.like("parent_ids", "," + id + ",");
            q2.in("type", typeList);
            int count2 = jcxfSysDeptMapper.selectCount(q2);
            map.put("baseTotal", count2);

            QueryWrapper q3 = new QueryWrapper();
            q3.eq("del_flag", 0);
            q3.like("parent_ids", "," + id + ",");
            q3.eq("type", 931);
            int count3 = jcxfSysDeptMapper.selectCount(q3);
            map.put("lsdzb", count3);

            QueryWrapper q4 = new QueryWrapper();
            q4.eq("del_flag", 0);
            q4.like("parent_ids", "," + id + ",");
            q4.in("type", typeList);
            int count4 = jcxfSysDeptMapper.selectCount(q4);
            map.put("lsdlhzb", count4);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getCountById(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        String type = (String) requestMap.get("type");
        Integer count = 0;
        try {
            List<JcxfSysDept> res = jcxfSysDeptMapper.getCountById(id, type);
            if (res != null && res.size() > 0) {
                count = res.size();
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getDeptIntroduceById(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
        }
        Map<String, Object> map = new HashMap<>();

        try {
            // 查询支部简介
            JcxfSysDept sysDept = jcxfSysDeptMapper.selectById(id);
            String deptIntroduction = sysDept.getDeptIntroduction();
            map.put("deptIntroduction", deptIntroduction);

            // 查询阵地图片
            /*QueryWrapper<AttachFile> wrapper = new QueryWrapper<>();
            wrapper.eq("tz_attach_file.foreign_key", id);
            wrapper.eq("tz_attach_file.table_type", "tz_sys_dept");
            List<AttachFile> attachFiles = attachFileMapper.selectList(wrapper);
            map.put("deptAttachFiles", attachFiles);*/

            // 本部所有党员信息查询
            QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("dept_id", id);
            queryWrapper.eq("del_flag", 0);
            List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(queryWrapper);
            map.put("partyInfoList", list);


            //预备党员人数查询
            QueryWrapper queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("dept_id", id);
            queryWrapper2.eq("del_flag", 0);
            queryWrapper2.eq("person_type", 4); // 发展纪实阶段（2.列为入党积极分子，3.确定为发展对象，4.讨论吸收为预备党员，5.转为正式党员）
            int preCount = jcxfDevelopPartyMapper.selectCount(queryWrapper2);
            map.put("preCount", preCount);

            // 入党积极分子人数查询
            QueryWrapper queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("dept_id", id);
            queryWrapper3.eq("del_flag", 0);
            queryWrapper3.eq("person_type", "2"); // 发展纪实阶段（2.列为入党积极分子，3.确定为发展对象，4.讨论吸收为预备党员，5.转为正式党员）
            int activeCount = jcxfDevelopPartyMapper.selectCount(queryWrapper3);
            map.put("activeCount", activeCount);

            //换届时间查询
            QueryWrapper<JcxfBranchReelection> termDeptQueryWrapper = new QueryWrapper<>();
            termDeptQueryWrapper.eq("branch_reelection.del_flag", 0);
            termDeptQueryWrapper.eq("branch_reelection.dept_id", id);
            termDeptQueryWrapper.orderByDesc("branch_reelection.this_session_finish_time"); //根据本届届满时间排序取最新的一条
            termDeptQueryWrapper.last("LIMIT 1");
            JcxfBranchReelection one1 = jcxfTermDeptMapper.selectOne(termDeptQueryWrapper);
            if (one1 != null) {
                map.put("thisChangeTime", one1.getThisSessionStartTime());
                map.put("thisFinishTime", one1.getThisSessionFinishTime());
            } else {
                map.put("thisChangeTime", null);
                map.put("thisFinishTime", null);
            }
            // 活动阵地信息查询
            QueryWrapper queryWrapper4 = new QueryWrapper();
            queryWrapper4.eq("dept_id", id);
            queryWrapper4.eq("del_flag", 0);
            JcxfDeptPosition one = jcxfDeptPositionMapper.selectOne(queryWrapper4);
            map.put("activeDept", one);

            //活动阵地照片查询
            if (one != null) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.eq("dept_id", id);
                map.put("activeAttachFiles", jcxfDeptPositionPhotoMapper.selectList(queryWrapper1));
            } else {
                map.put("activeAttachFiles", null);
            }

            //党内表彰查询
            JcxfSysDept byId = jcxfSysDeptMapper.selectById(id);

            // 判断是否有党员或者组织本身通过了两优一先
            QueryWrapper qFlag = new QueryWrapper();
            qFlag.eq("create_dept_id", byId.getId());
            qFlag.eq("del_flag", 0);
            qFlag.eq("status", "6");
            byId.setTzTwoBestOneFirstList(tzTwoBestOneFirstMapper.selectList(qFlag));
            if (byId.getTzTwoBestOneFirstList() != null && byId.getTzTwoBestOneFirstList().size() > 0) {
                byId.setRedFlag(true);
            }

            map.put("deptInfo", byId);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getChildrenDeptListByName(Map<String, Object> requestMap) {
        try {
            String name = (String) requestMap.get("deptName");
            String deptId = (String) requestMap.get("deptId");

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            ids.add(Long.valueOf(deptId));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.like("name", name);
            queryWrapper.in("id", ids);
            queryWrapper.last(" limit 15");

            List<JcxfSysDept> list = jcxfSysDeptMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getAllDeptListByName(Map<String, Object> requestMap) {
        try {
            String name = (String) requestMap.get("deptName");
            String deptId = (String) requestMap.get("deptId");
            List<Integer> typeList = (List<Integer>) requestMap.get("typeList");

            JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
            Long searchDeptId = null;
            if (dept.getParentId() == 0) {
                searchDeptId = Long.valueOf(dept.getId());
            } else {
                searchDeptId = Long.valueOf(dept.getParentIds().split(",")[1]);
            }

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(searchDeptId);
            ids.add(searchDeptId);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.like("name", name);
            queryWrapper.in("id", ids);
            if (typeList != null && typeList.size() > 0) {
                queryWrapper.in("type", typeList);
            }

            queryWrapper.last(" limit 15");

            List<JcxfSysDept> list = jcxfSysDeptMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getSameClassDeptListByDeptId(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(deptId);

        QueryWrapper q = new QueryWrapper();
        q.eq("del_flag", 0);
        q.eq("parent_id", tzSysDept.getParentId());
        q.ne("id", deptId);
        List<JcxfSysDept> tzSysDeptList = jcxfSysDeptMapper.selectList(q);

        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzSysDeptList));
    }

    @Override
    public Map<String, Object> queryBaseDeptCount(Map<String, Object> requestMap) {
        try {
            Integer id = (Integer) requestMap.get("id");
            Integer type = (Integer) requestMap.get("type");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "部门id为空，请联系管理员！");
            }
            List<Integer> deptIds = jcxfSysDeptMapper.selectChildrenIdsByIdAndType(id, type);
            Integer count = 0;
            if (deptIds != null) {
                deptIds.add(Integer.valueOf(id));
                count = deptIds.size();
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(count));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> getPartyCommitteeCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(deptId);

            String queryDeptId = "";

            if (tzSysDept.getParentId() == 0) {
                queryDeptId = tzSysDept.getId().toString();
            } else {
                queryDeptId = tzSysDept.getParentIds().split(",")[1];
            }
            String finalQueryDeptId = queryDeptId;

            List<Long> situationList = new ArrayList<>();
            situationList.add(11l); //建立党委的
            situationList.add(15l); //建立党小组

            Long num1 = 0l;
            Long num2 = 0l;

            QueryWrapper<JcxfSysDept> q1 = new QueryWrapper();
            q1.select("unit_org_situation as situation, count(*) as num");
            q1.and(i -> i.like("parent_ids", "," + finalQueryDeptId + ",").or(i2 -> i2.eq("id", finalQueryDeptId)));
            q1.eq("del_flag", 0);
            q1.in("unit_org_situation", situationList);//建立党委的
            q1.groupBy("unit_org_situation");
            List<Map<String, Object>> list = jcxfSysDeptMapper.selectMaps(q1);
            for (Map<String, Object> map : list) {
                if (map.get("situation").equals(11)) {
                    num1 = (Long) map.get("num");
                } else if (map.get("situation").equals(15)) {
                    num2 = (Long) map.get("num");
                }
            }

            /*QueryWrapper<JcxfSysDept> q2 = new QueryWrapper();
            q2.and(i -> i.like("parent_ids", ","+ finalQueryDeptId +",").or(i2 -> i2.eq("id", finalQueryDeptId)));
            q2.and(i -> i.eq("del_flag", 0));
            q2.and(i -> i.eq("unit_org_situation", "15"));//建立党小组
            int num2 = jcxfSysDeptMapper.selectCount(q2);*/


            List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);
            QueryWrapper<JcxfSysDept> q3 = new QueryWrapper();
            q3.and(i -> i.like("parent_ids", "," + finalQueryDeptId + ",").or(i2 -> i2.eq("id", finalQueryDeptId)));
            q3.and(i -> i.eq("del_flag", 0));
            q3.and(i -> i.in("type", typeList));
            // q3.eq("basic_party_committee", "1");//是否基层党委 0否 1是
            int num3 = jcxfSysDeptMapper.selectCount(q3);

            Map<String, Object> map = new HashMap<>();
            map.put("dw", num1);
            map.put("dxz", num2);
            map.put("basic", num3);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> getChildrenIdsById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(id))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getChildrenInfoById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfSysDeptMapper.selectChildrenInfoById(Long.valueOf(id))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> updateDeptTag(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String tag = (String) requestMap.get("tag");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("tag", tag);

            int res = jcxfSysDeptMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
        }
    }

    @Override
    public Map<String, Object> getSysDeptList(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        List<JcxfSysDept> list = new ArrayList<JcxfSysDept>();

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id, parent_id, name, type");
        queryWrapper.eq("del_flag", 0);
        if (StringUtil.isEmpty(deptId)) {
            queryWrapper.eq("parent_id", 0);
        } else {
            queryWrapper.eq("parent_id", deptId);
        }
        list = jcxfSysDeptMapper.selectList(queryWrapper);
        return RespBodyHandler.setRespBodyListDto(list);
    }

    @Override
    public Map<String, Object> bsinGetSysDeptDelFlagList(Map<String, Object> requestMap) {
        try {
            List<String> deptIds = (List<String>) requestMap.get("deptIds");
            if (deptIds != null && deptIds.size() > 0) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.select("id");
                queryWrapper.in("id", deptIds);
                queryWrapper.eq("del_flag", 0);
                List<JcxfSysDept> depts = jcxfSysDeptMapper.selectList(queryWrapper);

                if (depts != null && depts.size() > 0) {
                    List<String> ids = new ArrayList<>();
                    for (JcxfSysDept dept : depts) {
                        ids.add(String.valueOf(dept.getId()));
                    }

                    deptIds.removeAll(ids);
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(deptIds));
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(deptIds));
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(deptIds));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取组织失败");
        }
    }

    @Override
    public Map<String, Object> bsinGetSysDeptList(Map<String, Object> requestMap) {
        try {
            String parentId = (String) requestMap.get("parentId");

            if (StringUtils.isBlank(parentId)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "父级id为空");
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("id, parent_id, code, name, type, create_by, update_by");
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("parent_id", parentId);
            queryWrapper.orderByAsc("name");
            List<JcxfSysDept> depts = jcxfSysDeptMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(depts));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取组织失败");
        }
    }

    @Override
    public Map<String, Object> getDeptTree(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");

        JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectById(deptId);

        // 获取所有机构信息
        List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        deptIds.add(Long.valueOf(deptId));
        QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", deptIds);
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByAsc("name");
        List<JcxfSysDept> sysDeptList = jcxfSysDeptMapper.selectList(queryWrapper);
        List<DeptTree> deptTrees = BeanUtils.toBean(sysDeptList, DeptTree.class);

        DeptTree deptTree = new DeptTree();
        deptTree.setId(deptId);
        deptTree.setName(jcxfSysDept.getName());
        deptTree.setParentId("0");
        deptTrees.add(deptTree);
        List<DeptTree> build = BuildTreeUtils.build(deptTrees);// 组装成父子的树形目录结构
        return RespBodyHandler.setRespBodyListDto(build);
    }

    @Override
    public Map<String, Object> getNoDoneDeptTree(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String payMonth = (String) requestMap.get("payMonth");

        JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectById(deptId);

        // 获取所有机构信息
        List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
        deptIds.add(Long.valueOf(deptId));
        QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", deptIds);
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByAsc("name");
        List<JcxfSysDept> sysDeptList = jcxfSysDeptMapper.selectList(queryWrapper);

        LambdaQueryWrapper<TzPayFeeDetail> tzPayFeeDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tzPayFeeDetailLambdaQueryWrapper.eq(TzPayFeeDetail::getPayMonth, payMonth);
        tzPayFeeDetailLambdaQueryWrapper.eq(TzPayFeeDetail::getDelFlag, 0);
        tzPayFeeDetailLambdaQueryWrapper.eq(TzPayFeeDetail::getStatus, 0);
        tzPayFeeDetailLambdaQueryWrapper.gt(TzPayFeeDetail::getShouldPay, 0);
        tzPayFeeDetailLambdaQueryWrapper.select(TzPayFeeDetail::getDeptId);
        List<TzPayFeeDetail> tzPayFeeDetails = tzPayFeeDetailMapper.selectList(tzPayFeeDetailLambdaQueryWrapper);


        Integer allNoDoneNum = 0;
        for (JcxfSysDept sysDept:sysDeptList) {
            List<Long> ids = sysDeptList.stream().filter(item -> item.getParentIds()
                    .contains("," + String.valueOf(sysDept.getId()) + ",")).map(item -> Long.parseLong(String.valueOf(item.getId()))).collect(Collectors.toList());
            ids.add(Long.valueOf(sysDept.getId()));
            List<TzPayFeeDetail> collect = tzPayFeeDetails.stream().filter(item -> ids.contains(item.getDeptId())).collect(Collectors.toList());
            sysDept.setNoDoneNum(collect.size());
            if(ids.size() == 1){
                allNoDoneNum += collect.size();
            }
        }

        sysDeptList = sysDeptList.stream().filter(deptInfo -> deptInfo.getNoDoneNum() > 0).collect(Collectors.toList());;

        List<DeptTree> deptTrees = BeanUtils.toBean(sysDeptList, DeptTree.class);

        DeptTree deptTree = new DeptTree();
        deptTree.setId(deptId);
        deptTree.setName(jcxfSysDept.getName());
        deptTree.setParentId("0");
        deptTree.setNoDoneNum(allNoDoneNum);
        deptTrees.add(deptTree);
        List<DeptTree> build = BuildTreeUtils.build(deptTrees);// 组装成父子的树形目录结构
        return RespBodyHandler.setRespBodyListDto(build);
    }


    /**
     * 功能描述：构建模糊查询
     *
     * @param tzSysDept 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<JcxfSysDept> LikeAllField(JcxfSysDept tzSysDept, SearchVo searchVo) {
        QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(tzSysDept.getCode())) {
            queryWrapper.and(i -> i.like("t.code", tzSysDept.getCode()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getName())) {
            queryWrapper.and(i -> i.like("t.name", tzSysDept.getName()));
        }
        if (tzSysDept.getType() != null) {
            queryWrapper.and(i -> i.like("t.type", tzSysDept.getType()));
        }
        if (tzSysDept.getDeptType() != null) {
            queryWrapper.and(i -> i.like("t.dept_type", tzSysDept.getDeptType()));
        }
        if (tzSysDept.getFinallySort() != null) {
            queryWrapper.and(i -> i.like("t.finally_sort", tzSysDept.getFinallySort()));
        }
        if (tzSysDept.getGrade() != null) {
            queryWrapper.and(i -> i.like("t.grade", tzSysDept.getGrade()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getAddress())) {
            queryWrapper.and(i -> i.like("t.address", tzSysDept.getAddress()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getZipCode())) {
            queryWrapper.and(i -> i.like("t.zip_code", tzSysDept.getZipCode()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getMaster())) {
            queryWrapper.and(i -> i.like("t.master", tzSysDept.getMaster()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getPhone())) {
            queryWrapper.and(i -> i.like("t.phone", tzSysDept.getPhone()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getFax())) {
            queryWrapper.and(i -> i.like("t.fax", tzSysDept.getFax()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getEmail())) {
            queryWrapper.and(i -> i.like("t.email", tzSysDept.getEmail()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getIcon())) {
            queryWrapper.and(i -> i.like("t.icon", tzSysDept.getIcon()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getCreateBy())) {
            queryWrapper.and(i -> i.like("t.create_by", tzSysDept.getCreateBy()));
        }
        if (tzSysDept.getCreateDate() != null) {
            queryWrapper.and(i -> i.like("t.create_date", tzSysDept.getCreateDate()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getUpdateBy())) {
            queryWrapper.and(i -> i.like("t.update_by", tzSysDept.getUpdateBy()));
        }
        if (tzSysDept.getUpdateDate() != null) {
            queryWrapper.and(i -> i.like("t.update_date", tzSysDept.getUpdateDate()));
        }
        if (tzSysDept.getPartyOrgSituation() != null) {
            queryWrapper.and(i -> i.like("t.party_org_situation", tzSysDept.getPartyOrgSituation()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getPartyOrgContact())) {
            queryWrapper.and(i -> i.like("t.party_org_contact", tzSysDept.getPartyOrgContact()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getPartyOrgManager())) {
            queryWrapper.and(i -> i.like("t.party_org_manager", tzSysDept.getPartyOrgManager()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getUnitName())) {
            queryWrapper.and(i -> i.like("t.unit_name", tzSysDept.getUnitName()));
        }
        if (tzSysDept.getPartyOrgType() != null) {
            queryWrapper.and(i -> i.like("t.party_org_type", tzSysDept.getPartyOrgType()));
        }
        if (tzSysDept.getUnitOrgSituation() != null) {
            queryWrapper.and(i -> i.like("t.unit_org_situation", tzSysDept.getUnitOrgSituation()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getUnitCode())) {
            queryWrapper.and(i -> i.like("t.unit_code", tzSysDept.getUnitCode()));
        }
        if (tzSysDept.getPartyOrgCreateType() != null) {
            queryWrapper.and(i -> i.like("t.party_org_create_type", tzSysDept.getPartyOrgCreateType()));
        }
        if (tzSysDept.getApprovalCreateTime() != null) {
            queryWrapper.and(i -> i.like("t.approval_create_time", tzSysDept.getApprovalCreateTime()));
        }
        if (tzSysDept.getLongitude() != null) {
            queryWrapper.and(i -> i.like("t.longitude", tzSysDept.getLongitude()));
        }
        if (tzSysDept.getLatitude() != null) {
            queryWrapper.and(i -> i.like("t.latitude", tzSysDept.getLatitude()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getDeptLabel())) {
            queryWrapper.and(i -> i.like("t.dept_label", tzSysDept.getDeptLabel()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getDeptPhoto())) {
            queryWrapper.and(i -> i.like("t.dept_photo", tzSysDept.getDeptPhoto()));
        }
        if (tzSysDept.getDeptIntroduction() != null) {
            queryWrapper.and(i -> i.like("t.dept_introduction", tzSysDept.getDeptIntroduction()));
        }
        if (tzSysDept.getOrganizationType() != null) {
            queryWrapper.and(i -> i.eq("t.organization_type", tzSysDept.getOrganizationType()));
        }
        if (tzSysDept.getDemonstrativeSchool() != null) {
            queryWrapper.and(i -> i.like("t.demonstrative_school", tzSysDept.getDemonstrativeSchool()));
        }
        if (tzSysDept.getDemonstrativePartyOrg() != null) {
            queryWrapper.and(i -> i.like("t.demonstrative_party_org", tzSysDept.getDemonstrativePartyOrg()));
        }
        if (tzSysDept.getAreaId() != null) {
            queryWrapper.and(i -> i.like("t.area_id", tzSysDept.getAreaId()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getOrgCode())) {
            queryWrapper.and(i -> i.like("t.org_code", tzSysDept.getOrgCode()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getParentOrgCode())) {
            queryWrapper.and(i -> i.like("t.parent_org_code", tzSysDept.getParentOrgCode()));
        }
        if (tzSysDept.getSort() != null) {
            queryWrapper.and(i -> i.like("t.sort", tzSysDept.getSort()));
        }
        if (tzSysDept.getUndoFlag() != null) {
            queryWrapper.and(i -> i.like("t.undo_flag", tzSysDept.getUndoFlag()));
        }
        if (tzSysDept.getUndoDate() != null) {
            queryWrapper.and(i -> i.like("t.undo_date", tzSysDept.getUndoDate()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getOldId())) {
            queryWrapper.and(i -> i.like("t.old_id", tzSysDept.getOldId()));
        }
        if (tzSysDept.getProxyId() != null) {
            queryWrapper.and(i -> i.like("t.proxy_id", tzSysDept.getProxyId()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getPyName())) {
            queryWrapper.and(i -> i.like("t.py_name", tzSysDept.getPyName()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getMerchant())) {
            queryWrapper.and(i -> i.like("t.merchant", tzSysDept.getMerchant()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getMerstatus())) {
            queryWrapper.and(i -> i.like("t.merstatus", tzSysDept.getMerstatus()));
        }
        if (StringUtils.isNotBlank(tzSysDept.getVeteran())) {
            queryWrapper.and(i -> i.like("t.veteran", tzSysDept.getVeteran()));
        }
        /*if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.lambda().and(i -> i.between(JcxfSySDept::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }*/
        queryWrapper.eq("t.del_flag", 0);
        return queryWrapper;
    }

}

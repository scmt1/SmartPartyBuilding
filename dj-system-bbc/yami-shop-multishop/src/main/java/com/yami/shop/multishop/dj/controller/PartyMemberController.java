package com.yami.shop.multishop.dj.controller;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import com.dept.controller.CommonDeptController;
import com.dept.service.CommonDeptService;
import com.dept.service.CommonPartyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.util.Base64;
import com.yami.shop.bean.constant.CommonConstant;
import com.yami.shop.bean.dto.PartyMemberDto;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.TDict;
import com.yami.shop.bean.model.TDictData;
import com.yami.shop.bean.model.dj.*;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.HttpClientUtils;
import com.yami.shop.common.util.ImportExeclUtil;
import com.yami.shop.dj.service.IDevelopPartyService;
import com.yami.shop.dj.service.IPartyMemberService;
import com.yami.shop.dj.service.IPartyMemberTransferService;
import com.yami.shop.dj.service.ITzSysDeptService;

import com.yami.shop.multishop.uitls.FileUtil;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.TDictDataService;
import com.yami.shop.service.TDictService;
import com.yami.shop.sys.common.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/platform/partyMember")
@Slf4j
@Api(tags = "党员管理")
public class PartyMemberController {
    @Autowired
    private SysUserService sysUserService;

    @Value("${rsa.publicKey}")
    private String publicKey;

    @Value("${rsa.private_key}")
    private String private_key;
    @Autowired
    private IPartyMemberService partyMemberService;
    //@Autowired
    //private SecurityUtil securityUtil;

    @Autowired
    private ITzSysDeptService tzSysDeptService;

    @Autowired
    private AttachFileService attachFileService;

    @Autowired
    private IPartyMemberTransferService partyMemberTransferService;

    @Autowired
    private IDevelopPartyService developPartyService;

    @Autowired
    private TDictService tDictService;

    @Autowired
    private TDictDataService tDictDataService;

    @Value("${application.partyUrl}")
    private String partyUrl;

    @Autowired
    private PasswordManager passwordManager;

    @Autowired
    private CommonDeptService commonDeptService;

    @Autowired
    private CommonPartyService commonPartyService;
    //private final ITAttachmentService tAttachmentService;
/*    @Autowired
    private  TzSysDeptController controller;*/
    /**
     * 功能描述：新增党员数据
     *
     * @param partyMember 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增党员数据")
    @PostMapping("addPartyMember")
    public ServerResponseEntity<Object> addPartyMember(@RequestBody PartyMember partyMember) {
        try {
            if (partyMember.getId() != null) {
                partyMember.setUpdateDate(new Date());
                boolean res = partyMemberService.updateById(partyMember);
                if (res) {
                    List<AttachFile> attachmentList = partyMember.getAttachFiles();
                    if (attachmentList != null && attachmentList.size() > 0) {
                        attachmentList.forEach(i -> {
                            if (i.getFileId() == null) {
                                i.setForeignKey(partyMember.getId().toString());
                                attachFileService.save(i);
                            }
                        });
                    }
                    String[] ids = partyMember.getIds();
                    if (ids != null && ids.length > 0) {
                        //QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
                        attachFileService.removeByIds(Arrays.asList(ids));
                    }
                    return ServerResponseEntity.success(res);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            }
            partyMember.setDelFlag(0);
            //tzSysDept.setCreateId(securityUtil.getCurrUser().getId());
            partyMember.setCreateDate(new Date());
            partyMember.setUsername(partyMember.getRealName());
            String idcard = partyMember.getIdcard();
            String substring = null;
            //初始密码默认为身份证号后6位
            if (idcard != null && idcard != "") {
                substring = idcard.substring(12);
            }
            //密码加密
      /*      RSA rsa = new RSA(null, publicKey);
            byte[] bytes = substring.getBytes();
            byte[] encrypt = rsa.encrypt(bytes, KeyType.PublicKey);
            String encryptString = Base64.encodeToString(encrypt, Base64.NO_WRAP);
            String result = new String(encryptString);*/
            long time = new Date().getTime();
            String result=passwordManager.encryptPassword(time + substring);
            partyMember.setPassword(result);
            boolean res = partyMemberService.save(partyMember);
            if (res) {
                List<AttachFile> attachFiles = partyMember.getAttachFiles();
                if (partyMember.getAttachFiles() != null) {
                    for (AttachFile attachFile : attachFiles) {
                        attachFile.setForeignKey(partyMember.getId().toString());
                    }
                }
                attachFileService.saveBatch(attachFiles);
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询党员数据")
    @GetMapping("queryPartyMemberList")
    public ServerResponseEntity<Object> queryPartyMemberList(PartyMember partyMember, SearchVo searchVo, PageVo pageVo, String today) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();

            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), partyMember.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            IPage<PartyMember> result = partyMemberService.queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据组织id查询激活党员数量
     *
     * @param partyMember
     * @return
     */
    @ApiOperation("根据组织id查询激活党员数量")
    @GetMapping("queryCountActive")
    public ServerResponseEntity<Object> queryCountActive(PartyMember partyMember) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            HashMap<String, Integer> map = new HashMap<>();
            //根据党员所属组织id，查询出对应的组织以及组织下面的子组织，并把这些组织id放在list集合中
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), partyMember.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }

            QueryWrapper<PartyMember> wrapper = new QueryWrapper<>();
            QueryWrapper<PartyMember> wrapper2 = new QueryWrapper<>();
            //根据组织id集合查询出所有的党员
            if (listAll != null && listAll.size() > 0) {
                wrapper.and(i -> i.in("party_member.dept_id", listAll));
                wrapper2.and(i -> i.in("party_member.dept_id", listAll));
            }
            //查询为空的app_login_date，就是未激活的
            wrapper.and(i -> i.isNull("party_member.app_login_date"));
            wrapper.and(i -> i.eq("party_member.del_flag", 0));
            //查询不为空的app_login_date，就是激活的
            wrapper2.and(i -> i.isNotNull("party_member.app_login_date"));
            wrapper2.and(i -> i.eq("party_member.del_flag", 0));
            int unActive = partyMemberService.count(wrapper);
            int active = partyMemberService.count(wrapper2);
            map.put("active", active);
            map.put("unActive", unActive);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 党员列表查询
     */
    @GetMapping("queryPartyMemberListOld")
    @ApiOperation(value = "党员列表查询", notes = "党员列表查询")
    public ServerResponseEntity<Object> queryPartyMemberListOld() {
        Map<String, Object> params = new HashMap<>();

        params.put("pageNum", 1);
        params.put("pageSize", 10);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sexName", "");
        jsonObject.put("positionName", "");
        jsonObject.put("isFirstSecretary", "");
        jsonObject.put("personType", "");
        jsonObject.put("nationName", "");
        jsonObject.put("partyState", "");
        jsonObject.put("educationName", "");
        jsonObject.put("isLost", "");
        jsonObject.put("partyAge", "");
        jsonObject.put("isFLow", "");
        jsonObject.put("isSuffer", "");
        jsonObject.put("deptName", "");
        jsonObject.put("startTimeSearch", "");
        jsonObject.put("finishTimeSearch", "");
        jsonObject.put("dept_id_child", 100);
        params.put("whereMap", jsonObject);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json;charset=utf-8");
        String postResult = HttpClientUtils.sendPostRequest(partyUrl + "/jcxf/cloudPlatfrom/party/member/list", params, httpHeaders);
        PartyResponseEntity partyResponseEntity = JSON.parseObject(postResult, PartyResponseEntity.class);
        return ServerResponseEntity.success(partyResponseEntity.getData());
    }

    /**
     * 党员分页、党员是否激活、今日新增党员数量查询
     */
    @GetMapping("queryPartyMemberListById")
    @ApiOperation(value = "党员分页、党员是否激活、今日新增党员数量查询", notes = "党员分页、党员是否激活、今日新增党员数量查询")
    public ServerResponseEntity<Object> queryPartyMemberListById(int pageSize, int pageNum, String parentIdChild, String isActive, String startTimeSearch) {
        Map<String, Object> params = new HashMap<>();
        parentIdChild = parentIdChild != "" ? parentIdChild : "100";
        if (pageNum != 0 && pageSize != 0) {
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);
        }
        //partyState = partyState !=  ? partyState : 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sexName", "");
        jsonObject.put("positionName", "");
        jsonObject.put("isFirstSecretary", "");
        jsonObject.put("personType", "");
        jsonObject.put("nationName", "");
        if (isActive != null && isActive != "") {
            jsonObject.put("isActive", isActive);
        }
        jsonObject.put("partyState", "");
        jsonObject.put("educationName", "");
        jsonObject.put("isLost", "");
        jsonObject.put("partyAge", "");
        jsonObject.put("isFLow", "");
        jsonObject.put("isSuffer", "");
        jsonObject.put("deptName", "");
        if (startTimeSearch != null && startTimeSearch != "") {
            jsonObject.put("create_date", startTimeSearch);
        }

        jsonObject.put("finishTimeSearch", "");
        jsonObject.put("dept_id_child", parentIdChild);
        params.put("whereMap", jsonObject);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json;charset=utf-8");
        String postResult = HttpClientUtils.sendPostRequest(partyUrl + "/jcxf/cloudPlatfrom/party/member/list", params, httpHeaders);
        PartyResponseEntity partyResponseEntity = JSON.parseObject(postResult, PartyResponseEntity.class);
        return ServerResponseEntity.success(partyResponseEntity.getData());
    }

    /**
     * 党员的党组织关系转移
     *
     * @param partyIds deptId
     * @return
     */
    @GetMapping("partyMemberShift")
    @ApiOperation(value = "党员的党组织关系转移")
    public ServerResponseEntity<Object> partyMemberShift(String[] partyIds, Long deptId) {
        if (partyIds == null || partyIds.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员");
        }
        try {
            for (String partyId : partyIds) {
                PartyMember byId = partyMemberService.getById(partyId);
                if (byId != null) {
                    UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("party_member.id", byId.getId());
                    byId.setDeptId(deptId);
                    boolean update = partyMemberService.update(byId, updateWrapper);
                    if (!update) {
                        return ServerResponseEntity.showFailMsg("党组织转移异常");
                    }
                }
            }
            return ServerResponseEntity.success("党组织转移成功");
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("党组织转移异常:" + e.getMessage());
        }

    }

    /**
     * 党员信息查询
     */
    @GetMapping("queryPartyMemberInfo")
    @ApiOperation(value = "党员信息查询", notes = "党员信息查询")
    public ServerResponseEntity<Object> queryPartyMemberInfo(String id) {
        MultiValueMap<String, Object> mulmap = new LinkedMultiValueMap<>();
        mulmap.add("id", id);
        String postResult = HttpClientUtils.sendPostRequestForm(partyUrl + "/jcxf/cloudPlatfrom/party/member/detail", mulmap, null);
        PartyResponseEntity partyResponseEntity = JSON.parseObject(postResult, PartyResponseEntity.class);
        return ServerResponseEntity.success(partyResponseEntity.getData());
    }

    /**
     * 根据不同条件查询对应的党员列表
     *
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("findPartyMemberByPage")
    @ApiOperation(value = "根据不同条件查询对应的党员列表", notes = "根据不同条件查询对应的党员列表")
    public ServerResponseEntity<Object> findDeptByPage(@RequestBody Object object) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Accept", "application/json;charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        String s = JSON.toJSONString(object);
        String value = mapper.writeValueAsString(object);
        String postResult = HttpClientUtils.sendPostRequest(partyUrl + "/jcxf/cloudPlatfrom/party/member/list", s, httpHeaders);
        PartyResponseEntity partyResponseEntity = JSON.parseObject(postResult, PartyResponseEntity.class);
        return ServerResponseEntity.success(partyResponseEntity.getData());
    }


    /**
     * 党员信息修改
     */
    @GetMapping("editPartyMemberInfo")
    @ApiOperation(value = "党员信息修改", notes = "党员信息修改")
    public ServerResponseEntity<Object> editPartyMemberInfo(String id) {


        return null;
    }


    /**
     * 党员信息删除
     */
    @GetMapping("deletePartyMemberInfo")
    @ApiOperation(value = "党员信息删除", notes = "党员信息删除")
    public ServerResponseEntity<Object> deletePartyMemberInfo(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("party_member.del_flag", 1).set("party_member.update_date", new Date()).in("party_member.id", ids);
            boolean update = partyMemberService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }


    /**
     * 功能描述：根据主键来获取数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取党员数据")
    @GetMapping("getPartyInfo")
    public ServerResponseEntity<Object> getPartyInfo(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<AttachFile> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_attach_file.foreign_key", id);
            PartyMember res = partyMemberService.getById(id);
            List<AttachFile> list = attachFileService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                res.setAttachFiles(list);
            }
            if (res != null) {
                // 根据result拿到的deptId去查询对应的所属部门
                if (res.getDeptId() != null) {
                    TzSysDept tzSysDept = tzSysDeptService.getById(res.getDeptId());
                    res.setDeptName(tzSysDept.getName());
                }
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    @GetMapping("resetPassword")
    @ApiOperation(value = "重置密码")
    public ServerResponseEntity<Object> resetPassword(String id) {
        if (id == null || id == "") {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员");
        }
        try {
            PartyMember byId = partyMemberService.getById(id);
            if (byId != null) {
                String idcard = byId.getIdcard();
                String substring = null;
                if (idcard != null && idcard != "") {
                    substring = idcard.substring(12);
                }
                //密码加密
              /*  RSA rsa = new RSA(null, publicKey);
                byte[] bytes = substring.getBytes();
                byte[] encrypt = rsa.encrypt(bytes, KeyType.PublicKey);
                String encryptString = Base64.encodeToString(encrypt, Base64.NO_WRAP);
                String result = new String(encryptString);*/

                    /*//密码解密
                    RSA rsa2 = new RSA(private_key, null);
                    String result2 = new String(rsa2.decrypt(result, KeyType.PrivateKey));*/
                long time = new Date().getTime();
                String result=passwordManager.encryptPassword(time + substring);

                UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("party_member.id", byId.getId());
                byId.setPassword(result);
                boolean update = partyMemberService.update(byId, updateWrapper);
                if (!update) {
                    return ServerResponseEntity.showFailMsg("重置密码失败");
                }
            }
            return ServerResponseEntity.success("重置密码成功");
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("重置异常:" + e.getMessage());
        }

    }

    /**
     * 查询已删除党员
     */
    @GetMapping("queryDeletePartyMemberList")
    @ApiOperation(value = "查询已删除党员", notes = "查询已删除党员")
    public ServerResponseEntity<Object> queryDeletePartyMemberList(PartyMember partyMember, SearchVo searchVo, PageVo pageVo, String today) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            partyMember.setDelFlag(1);
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), partyMember.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            IPage<PartyMember> result = partyMemberService.queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    /**
     * 党员信息恢复
     */
    @GetMapping("recoverDeletePartyMemberInfo")
    @ApiOperation(value = "党员信息恢复", notes = "党员信息恢复")
    public ServerResponseEntity<Object> recoverDeletePartyMemberInfo(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("party_member.del_flag", 0).set("party_member.update_date", new Date()).in("party_member.id", ids);
            boolean update = partyMemberService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("恢复失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("恢复异常:" + e.getMessage());
        }
    }

    /**
     * 流出党员分页查询
     */
    @GetMapping("flowOutPartyMemberList")
    @ApiOperation(value = "流出党员分页查询", notes = "流出党员分页查询")
    public ServerResponseEntity<Object> flowOutPartyMemberList(PartyMember partyMember, SearchVo searchVo, PageVo pageVo, String today) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            partyMember.setDelFlag(0);
            partyMember.setIsFlow("1");
            //partyMember.setFlowPlace("not null");
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), partyMember.getDeptId())));
                queryWrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            IPage<PartyMember> result = partyMemberService.queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    /**
     * 流出党员流回操作
     */
    @GetMapping("setReturnPartyMember")
    @ApiOperation(value = "流出党员流回操作", notes = "流出党员流回操作")
    public ServerResponseEntity<Object> setReturnPartyMember(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("party_member.is_flow", 0).set("party_member.update_date", new Date()).in("party_member.id", ids);
            boolean update = partyMemberService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }

    /**
     * 流出党员分页查询
     */
    @GetMapping("flowInPartyMemberList")
    @ApiOperation(value = "流入党员分页查询", notes = "流入党员分页查询")
    public ServerResponseEntity<Object> flowInPartyMemberList(PartyMember partyMember, SearchVo searchVo, PageVo pageVo, String today) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            partyMember.setDelFlag(0);
            partyMember.setIsFloatIn("1");
            //partyMember.setFlowPlace("not null");
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), partyMember.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            IPage<PartyMember> result = partyMemberService.queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 流入党员流出操作
     */
    @GetMapping("setFlowOutPartyMember")
    @ApiOperation(value = "流出党员流回操作", notes = "流出党员流回操作")
    public ServerResponseEntity<Object> setFlowOutPartyMember(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<PartyMember> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("party_member.is_float_in", 0).set("party_member.update_date", new Date()).in("party_member.id", ids);
            boolean update = partyMemberService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("操作异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据所属的组织id查询该组织对应的党员，不查询该组织下属组织的党员信息
     *
     * @return 返回获取结果
     */
    @ApiOperation("根据所属的组织id查询该组织对应的党员")
    @GetMapping("queryOneselfPartyMemberList")
    public ServerResponseEntity<Object> queryOneselfPartyMemberList(PartyMember partyMember) {
        if (partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            List<PartyMember> result = partyMemberService.queryOneselfPartyMemberList(partyMember);
            if (result != null && result.size() > 0) {
                for (PartyMember member : result) {
                    member.setKey(member.getId().toString());
                    member.setLabel(member.getRealName());
                }
            }
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据所属的组织id查询该组织对应的流动党员，不查询该组织下属组织的党员信息
     *
     * @return 返回获取结果
     */
    @ApiOperation("根据所属的组织id查询该组织对应的流动党员")
    @GetMapping("queryOneselfFlowPartyMemberList")
    public ServerResponseEntity<Object> queryOneselfFlowPartyMemberList(PartyMember partyMember) {
        if (partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            List<PartyMember> result = partyMemberService.queryOneselfFlowPartyMemberList(partyMember);
            if (result != null && result.size() > 0) {
                for (PartyMember member : result) {
                    member.setKey(member.getId().toString());
                    member.setLabel(member.getRealName());
                }
            }
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }



    /**
     * 功能描述：新增流出党员信息
     *
     * @param partyMember 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增流出党员信息")
    @PostMapping("addFlowOutPartyMember")
    public ServerResponseEntity<Object> addFlowOutPartyMember(@RequestBody PartyMember partyMember) {
        try {
            //if (partyMember.getIsFlow()!=null&&partyMember.getIsFlow()=="1") {
            //partyMember.setIsFlow("1");
            if (partyMember.getIsFlow() == "1") {
                partyMember.setFlowDate(new Date());
            } else if (partyMember.getIsFloatIn() == "1") {
                partyMember.setFloatDate(new Date());
            }
            partyMember.setFlowDate(new Date());
            boolean res = partyMemberService.updateById(partyMember);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
            //}

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据身份证号来获取数据
     *
     * @param idcard 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据身份证号来获取数据")
    @GetMapping("getDataInfoByIdcard")
    public ServerResponseEntity<Object> getDataInfoByIdcard(@RequestParam(name = "idcard") String idcard) {
        if (StringUtils.isBlank(idcard)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("party_member.idcard", idcard);
            List<PartyMember> list = partyMemberService.list(queryWrapper);
            if (list != null) {
                for (PartyMember partyMember : list) {
                    // 根据result拿到的deptId去查询对应的所属部门
                    if (partyMember.getDeptId() != null) {
                        TzSysDept tzSysDept = tzSysDeptService.getById(partyMember.getDeptId());
                        partyMember.setDeptName(tzSysDept.getName());
                    }
                }

                return ServerResponseEntity.success(list);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }


    /**
     * 党组织关系转移 这一步仅为转出，属于待处理状态，还未接收
     *
     * @param partyMember
     * @param partyMemberTransfer
     * @return
     */
    @ApiOperation("党组织关系转移")
    @PostMapping("organizationTransfer")
    public ServerResponseEntity<Object> organizationTransfer(@RequestBody PartyMember partyMember, @RequestBody PartyMemberTransfer partyMemberTransfer) {
        if (partyMember.getId() == null) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }

        //@NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = sysUserService.getSysUserById(SecurityUtils.getShopUser().getUserId()).getUsername();
        @NotBlank(message = "用户名不能为空") @Size(min = 2, max = 20, message = "用户名长度要在2-20之间") String username = "";
        partyMemberTransfer.setUpdateBy(username);
        partyMemberTransfer.setReceiveStatus("1");
        partyMemberTransfer.setPartyMemberId(partyMember.getId());
        partyMemberTransfer.setName(partyMember.getRealName());
        partyMemberTransfer.setId(null);
        partyMemberTransfer.setDelFlag(0);
        partyMemberTransfer.setDoTime(new Date());

        Long id = partyMember.getId();
        QueryWrapper<PartyMemberTransfer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_member_transfer.party_member_id", id);
        PartyMemberTransfer one = partyMemberTransferService.getOne(queryWrapper);
        try {
            // 先判断该党员是否处于党组织转移中，如果是则返回提示用户
            if (one != null) {
                System.out.println(one.getReceiveStatus().equals("1") + "===================");
                if (one.getReceiveStatus().equals("1")) {
                    System.out.println(one.getReceiveStatus() == "1");
                    return ServerResponseEntity.showFailMsg("改成员已处于党组织转移状态中！！");
                } else if (one.getReceiveStatus().equals("2")) {
                    //如果该党员第一次转移流程已经走完，并且又发生了党组织转移，则直接在之前的基础上修改而非新增
                    partyMemberTransfer.setId(one.getId());
                    boolean update = partyMemberTransferService.updateById(partyMemberTransfer);
                    if (update) {
                        return ServerResponseEntity.success(update);
                    } else {
                        return ServerResponseEntity.showFailMsg("修改失败");
                    }
                }
            }
            boolean res = partyMemberTransferService.save(partyMemberTransfer);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }


    /**
     * 党组织关系转移 这一步接收党员，操作完后属于已接收状态
     *
     * @param partyMember
     * @param partyMemberTransfer
     * @return
     */
    @ApiOperation("党组织关系转移")
    @PostMapping("organizationReceive")
    public ServerResponseEntity<Object> organizationReceive(@RequestBody PartyMember partyMember, @RequestBody PartyMemberTransfer partyMemberTransfer) {
        if (partyMember.getId() == null) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        Long id = partyMember.getId();
        QueryWrapper<PartyMemberTransfer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_member_transfer.party_member_id", id);
        PartyMemberTransfer one = partyMemberTransferService.getOne(queryWrapper);
        one.setReceiveStatus(partyMemberTransfer.getReceiveStatus());
        if (partyMemberTransfer.getReceiveStatus() == "3") {
            one.setRejectReason(partyMemberTransfer.getRejectReason());
        }
        try {
            boolean res = partyMemberTransferService.updateById(one);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    @ApiOperation("党组织关系转移分页查询")
    @GetMapping("queryOrganizationTransfer")
    public ServerResponseEntity<Object> queryOrganizationTransfer(PartyMember partyMember, PartyMemberTransfer partyMemberTransfer, SearchVo searchVo, PageVo pageVo, String today) {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();

            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + partyMember.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), partyMember.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            Map<String, Object> result = partyMemberTransferService.queryPartyMemberListByPage(partyMemberTransfer, searchVo, pageVo, listAll);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：新增发展党员信息
     *
     * @param developParty 实体
     * @return 返回新增结果
     */
    @ApiOperation("新增发展党员信息")
    @PostMapping("addDevelopPartyMember")
    public ServerResponseEntity<Object> addDevelopPartyMember(@RequestBody DevelopParty developParty) {
        String developState = developParty.getDevelopState();

        if (developState.equals("2") && developParty.getSubmitDate() == null) {
            return ServerResponseEntity.showFailMsg("流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.equals("3") && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null)) {
            return ServerResponseEntity.showFailMsg("流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.equals("4") && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null)) {
            return ServerResponseEntity.showFailMsg("流程顺序不对，请检查发展纪实阶段顺序");
        } else if (developState.equals("5") && (developParty.getSubmitDate() == null || developParty.getActiveDate() == null || developParty.getDevelopDate() == null || developParty.getPreDate() == null)) {
            return ServerResponseEntity.showFailMsg("流程顺序不对，请检查发展纪实阶段顺序");
        }
        developParty.setDelFlag(0);
        if (developState.equals("4") || developState.equals("5")) {
            PartyMember partyMember = new PartyMember();
            //同步数据到党员表
            PartyMember sync = partyMemberService.sync(developParty, partyMember);
            boolean save = partyMemberService.save(sync);
            if (!save) {
                return ServerResponseEntity.showFailMsg("操作失败");
            }
        }
        try {
            if (developParty.getId() != null) {
                developParty.setUpdateTime(new Date());
                boolean res = developPartyService.updateById(developParty);
                if (res) {
                    return ServerResponseEntity.success(res);
                } else {
                    return ServerResponseEntity.showFailMsg("修改失败");
                }
            }

            boolean res = developPartyService.save(developParty);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("保存异常:" + e.getMessage());
        }
    }

    /**
     * 发展党员分页查询
     */
    @GetMapping("queryDevelopPartyList")
    @ApiOperation(value = "发展党员分页查询", notes = "发展党员分页查询")
    public ServerResponseEntity<Object> queryDevelopPartyList(DevelopParty developParty, SearchVo searchVo, PageVo pageVo) {
        Map<String, Object> mapAll = new HashMap<>();
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Integer> listAll = new ArrayList<>();
            developParty.setDelFlag(0);
            //partyMember.setFlowPlace("not null");
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (developParty.getDeptId() != null) {
                queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + developParty.getDeptId() + ","));
                queryWrapper.or(i -> i.eq(("tz_sys_dept.id"), developParty.getDeptId()));
                List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    listAll.add(id);
                }
            }
            //这一步是统计发展党员总数，提交入党申请书人数等等
            Map<String, Integer> baseInfo = developPartyService.getBaseInfo(listAll);

            IPage<DevelopParty> result = developPartyService.queryDevelopPartyList(developParty, searchVo, pageVo, listAll);
            mapAll.put("result", result);
            mapAll.put("baseInfo", baseInfo);
            return ServerResponseEntity.success(mapAll);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：根据主键来获取发展党员的数据
     *
     * @param id 主键
     * @return 返回获取结果
     */
    @ApiOperation("根据主键来获取发展党员的数据")
    @GetMapping("getDevelopParty")
    public ServerResponseEntity<Object> getDevelopParty(@RequestParam(name = "id") String id) {
        if (StringUtils.isBlank(id)) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            DevelopParty res = developPartyService.getById(id);
            if (res != null) {
                // 根据result拿到的deptId去查询对应的所属部门
                if (res.getDeptId() != null) {
                    TzSysDept tzSysDept = tzSysDeptService.getById(res.getDeptId());
                    res.setDeptName(tzSysDept.getName());
                }
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 发展对象信息删除
     */
    @GetMapping("deleteDevelopParty")
    @ApiOperation(value = "发展对象信息删除", notes = "发展对象信息删除")
    public ServerResponseEntity<Object> deleteDevelopParty(String[] ids) {
        if (ids == null || ids.length == 0) {
            return ServerResponseEntity.showFailMsg("参数为空，请联系管理员！！");
        }
        try {
            UpdateWrapper<DevelopParty> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("develop_party.del_flag", 1).set("develop_party.update_time", new Date()).in("develop_party.id", ids);
            boolean update = developPartyService.update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return ServerResponseEntity.success(update);
            } else {
                return ServerResponseEntity.showFailMsg("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("删除异常:" + e.getMessage());
        }
    }


    /**
     * 党员信息接收分页查询
     */
    @GetMapping("queryReceivePartyMember")
    @ApiOperation(value = "发展党员分页查询", notes = "发展党员分页查询")
    public ServerResponseEntity<Object> queryReceivePartyMember(PartyMemberTransfer partyMemberTransfer, SearchVo searchVo, PageVo pageVo) {
        try {
            IPage<PartyMemberTransfer> result = partyMemberTransferService.queryReceivePartyMember(partyMemberTransfer, searchVo, pageVo);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("下载党员模板")
    @GetMapping("/downFormwork")
    public void downFormwork(HttpServletResponse response) throws IOException {
        try {
            partyMemberService.downFormwork(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     */
    @ApiOperation("批量导入党员数据")
    @PostMapping("/partyImport")
    public ServerResponseEntity<Object> partyImport(@RequestParam(value = "file") MultipartFile multipartFile, String deptName, Long deptId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        File file = FileUtil.toFile(multipartFile);
        InputStream in = new FileInputStream(file);
        Workbook wb = ImportExeclUtil.chooseWorkbook(file.getName(), in);

        PartyMemberDto partyMember = new PartyMemberDto();

        List<PartyMemberDto> list = ImportExeclUtil.readDateListT(wb, partyMember, "sheet1", 3, 0);

        Map<String, Object> map = new HashMap<>();
        int index = 3;
        int errorCount = 0;
        List<String> errorList = new ArrayList<>();
        List<PartyMember> partyMemberList = new ArrayList<>();
        for (PartyMemberDto partyMemberDto : list) {

            PartyMember partyMember1 = new PartyMember();
            if (StringUtils.isBlank(partyMemberDto.getName())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：姓名不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setRealName(partyMemberDto.getName());


            if (StringUtils.isBlank(partyMemberDto.getIdCard())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：身份证号不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setIdcard(partyMemberDto.getIdCard());

            if (StringUtils.isBlank(partyMemberDto.getSex())) {
                if (partyMemberDto.getSex() == "未知") {
                    partyMember1.setSex(0);
                } else if (partyMemberDto.getSex() == "男") {
                    partyMember1.setSex(1);
                } else if (partyMemberDto.getSex() == "女") {
                    partyMember1.setSex(2);
                } else {
                    errorList.add("第" + index + "行导入出错！" + "错误详情：性别为空或不匹配");
                    index = index + 1;
                    errorCount = errorCount + 1;
                    continue;
                }
            }
            String nation = convertFiled("nation", partyMemberDto.getNation());
            if (StringUtils.isBlank(nation)) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：民族不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setNational(nation);

            partyMember1.setNativePlace(partyMemberDto.getJiguan());

            if (StringUtils.isBlank(partyMemberDto.getBirthday())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：出生日期不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setBirthday(StringUtils.isNotBlank(partyMemberDto.getBirthday()) ? sdf.parse(partyMemberDto.getBirthday()) : null);

            String education = convertFiled("education", partyMemberDto.getEducation());
            if (StringUtils.isBlank(education)) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：学历为空或学历不匹配");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setEducation(education);

            String personType = convertFiled("personType", partyMemberDto.getPersonType());
            if (StringUtils.isBlank(personType)) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：人员类别为空或不匹配");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setPersonType(personType != null && personType != "" ? Integer.parseInt(personType) : null);

            partyMember1.setDeptName(partyMemberDto.getDeptName());

            partyMember1.setPartyTime(StringUtils.isNotBlank(partyMemberDto.getPartyTime()) ? sdf.parse(partyMemberDto.getPartyTime()) : null);

            partyMember1.setTransferTime(StringUtils.isNotBlank(partyMemberDto.getTransferTime()) ? sdf.parse(partyMemberDto.getTransferTime()) : null);

            String workPosition = convertFiled("workPosition", partyMemberDto.getWorkPosition());
            if (StringUtils.isBlank(workPosition)) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：工作岗位为空或不匹配");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setWorkPosition(workPosition);

            partyMember1.setAddress(partyMemberDto.getAddress());

            if (StringUtils.isBlank(partyMemberDto.getPhone())) {
                errorList.add("第" + index + "行导入出错！" + "错误详情：手机号不能为空");
                index = index + 1;
                errorCount = errorCount + 1;
                continue;
            }
            partyMember1.setPhone(partyMemberDto.getPhone());

            partyMember1.setTelephone(partyMemberDto.getTelephone());

            partyMember1.setIsFamer(partyMemberDto.getIsFamer().equals("是") ? 1 : 0);

            partyMember1.setIsLost(partyMemberDto.getIsLost().equals("是") ? "1" : "0");

            partyMember1.setLostTime(partyMemberDto.getLoatTIme() != null && partyMemberDto.getLoatTIme() != "" ? sdf.parse(partyMemberDto.getLoatTIme()) : null);

            partyMember1.setIsFlow(partyMemberDto.getIsFlow().equals("是") ? "1" : "0");

            partyMember1.setDeptId(deptId);
            partyMember1.setDelFlag(0);
            partyMember1.setPartyState(1);
            partyMember1.setCreateDate(new Date());

            partyMember1.setUsername(partyMember1.getRealName());
            String idcard = partyMember1.getIdcard();
            String substring = null;
            //初始密码默认为身份证号后6位
            if (idcard != null && idcard != "") {
                substring = idcard.substring(12);
            }
            //密码加密
          /*  RSA rsa = new RSA(null, publicKey);
            byte[] bytes = substring.getBytes();
            byte[] encrypt = rsa.encrypt(bytes, KeyType.PublicKey);
            String encryptString = Base64.encodeToString(encrypt, Base64.NO_WRAP);
            String result = new String(encryptString);*/
            long time = new Date().getTime();
            String result=passwordManager.encryptPassword(time + substring);
            partyMember1.setPassword(result);

            partyMemberList.add(partyMember1);
        }
        int successCount = partyMemberList.size();
        map.put("zong", "导入成功" + successCount + "条，" + "导入失败" + errorCount + "条");
        map.put("errorList", errorList);
        if (partyMemberList.size() > 0) {
            boolean saveBatch = partyMemberService.saveBatch(partyMemberList);
            if (saveBatch) {
                return ServerResponseEntity.success(map);
            } else {
                return ServerResponseEntity.showFailMsg("导入异常");
            }
        } else {
            return ServerResponseEntity.success(map);
        }

    }

    /**
     * 不同属性和字段相互转换
     *
     * @param type
     * @param row
     * @return
     */
    public String convertFiled(String type, Object row) {
        String tmpRow = null;
        String tmp = "";
        if (row != null) {
            tmpRow = row.toString().trim();
        } else {
            return tmp;
        }
        TDict byType = tDictService.findByType(type);

        if (byType != null) {
            List<TDictData> tDictDataList = tDictDataService.findByDictIdAndStatusOrderBySortOrder(byType.getId(), CommonConstant.STATUS_NORMAL);
            for (TDictData tDictData : tDictDataList) {
                if (tDictData.getTitle().replaceAll("\\d+", "").trim().equals(tmpRow)) {
                    tmp = tDictData.getValue();
                }
            }

        }
        return tmp;
    }

    @ApiOperation("导出党员基本信息")
    @GetMapping("/exportPartyMember")
    public void exportPartyMember(HttpServletResponse response, PartyMember partyMember, SearchVo searchVo, String type, String[] ids) throws IOException {
        try {
            partyMemberService.exportPartyMember(response, partyMember, searchVo, type, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("统计性别、是否失联、贫困、流动党员数据")
    @GetMapping("/getInfo")
    public ServerResponseEntity<Object> getInfo(String deptId) throws IOException {
        try {
            QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<>();
            List<Integer> deptIds = new ArrayList<>();
            if (StringUtils.isBlank(deptId)) {
                deptId = "1";
            }
            String finalDeptId = deptId;
            queryWrapper.or(i -> i.like("tz_sys_dept.parent_ids", "," + finalDeptId + ",").or(i2 -> i2.eq(("tz_sys_dept.id"), finalDeptId)));
            queryWrapper.and(i -> i.eq(("tz_sys_dept.del_flag"), 0));
            List<TzSysDept> deptList = tzSysDeptService.list(queryWrapper);
            if (deptList != null && deptList.size() > 0) {
                for (TzSysDept tzSysDept : deptList) {
                    Integer id = tzSysDept.getId();
                    deptIds.add(id);
                }
                Map<String, Object> map = partyMemberService.getInfo(deptIds);
                return ServerResponseEntity.success(map);
            } else {
                return ServerResponseEntity.showFailMsg("该部门不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("统计异常:" + e.getMessage());
        }
    }

    @ApiOperation("导出党员基本信息")
    @GetMapping("/exportBasePartyMember")
    public void exportBasePartyMember(HttpServletResponse response, PartyMember partyMember) throws IOException {
        try {
            partyMemberService.exportBasePartyMember(response, partyMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导出党员基本信息")
    @GetMapping("/exportCollectPartyMember")
    public void exportCollectPartyMember(HttpServletResponse response, PartyMember partyMember) throws IOException {
        try {
            partyMemberService.exportProvinceBak(response, partyMember);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

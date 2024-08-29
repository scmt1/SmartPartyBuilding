package me.flyray.bsin.server.impl.jcxf;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.flyray.bsin.facade.service.JcxfPartyMemberService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.config.WxConfig;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.*;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;
import me.flyray.bsin.server.impl.AttachFileServiceImpl;
import me.flyray.bsin.server.impl.TzMessageServiceImpl;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.*;
import me.flyray.bsin.server.utils.*;
import me.flyray.bsin.server.utils.jcxf.Constant;
import me.flyray.bsin.server.utils.jcxf.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class JcxfPartyMemberServiceImpl extends ServiceImpl<JcxfPartyMemberMapper, JcxfPartyMember> implements JcxfPartyMemberService {
    @Autowired(required = true)
    private RedisUtils redisUtils;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private JcxfSysDeptServiceImpl jcxfSysDeptService;

    @Autowired
    private PasswordManager passwordManager;


    @Autowired
    private JcxfPartyMemberTransferMapper jcxfPartyMemberTransferMapper;


    @Autowired
    private TzStudyUserMapper jcxfStudyUserMapper;

    @Autowired
    private TzPayFeeDetailMapper tzPayFeeDetailMapper;

    @Autowired
    private TzLoginLogMapper tzLoginLogMapper;


    @Autowired
    private JcxfPartyFloatingMemberMapper jcxfPartyFloatingMemberMapper;

    @Autowired
    private JcxfSysDictionaryMapper jcxfSysDictionaryMapper;

    @Autowired
    private JcxfSysUserPartyMapper jcxfSysUserPartyMapper;

    @Autowired
    private JcxfOrganizationLifeMapper jcxfOrganizationLifeMapper;

    @Autowired
    private JcxfPartyMeetingUserMapper jcxfPartyMeetingUserMapper;

    @Autowired
    private JcxfPartyMemberVoMapper jcxfPartyMemberVoMapper;

    @Autowired
    private WxConfig wxConfig;


    @Override
    public Map<String, Object> queryBoss(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String positionType = (String) requestMap.get("positionType");
        JcxfPartyMember partyMember = new JcxfPartyMember();
        partyMember.setDeptId(Long.valueOf(deptId));
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
        if (partyMember.getDelFlag() == null) {
            partyMember.setDelFlag(false);
        }
        queryWrapper.eq("party_member.del_flag", 0).eq("party_member.dept_id", deptId).eq("party_member.position", positionType);
//        queryWrapper.eq("party_member.del_flag", 0).eq("party_member.dept_id", deptId);
        Page<JcxfPartyMember> page = new Page<>(1, 12);
        IPage<JcxfPartyMember> partyMemberIPage = jcxfPartyMemberMapper.selectPage(page, queryWrapper);
        List<JcxfPartyMember> records = partyMemberIPage.getRecords();
        JcxfPartyMember[] partyMembers = new JcxfPartyMember[records.size()];
        JcxfPartyMember[] members = records.toArray(partyMembers);

        /*for (int i = 0; i < members.length; i++) {
            QueryWrapper<AttachFile> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("foreign_key", members[i].getId());
            List<AttachFile> list = attachFileService.list(queryWrapper1);
            members[i].setAttachFiles(list);
        }*/
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(records));
    }

    @Override
    public Map<String, Object> getPartMemberListByIdCard(Map<String, Object> requestMap) {
        try {
            String idcard = (String) requestMap.get("idcard");
            String deptId = (String) requestMap.get("deptId");

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("idcard", AESUtil2.encrypt(idcard));
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("dept_id", deptId);

            // 排除组织关系转移表中待审核的
            QueryWrapper q = new QueryWrapper();
            q.select("party_id");
            q.eq("del_flag", 0);
            q.eq("out_branch_id", deptId);
            q.eq("accept_status", 0);
            List<JcxfPartyMemberTransfer> logs = jcxfPartyMemberTransferMapper.selectList(q);
            if (logs != null && logs.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (JcxfPartyMemberTransfer log : logs) {
                    ids.add(log.getPartyId());
                }
                queryWrapper.notIn("id", ids);
            }


            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfPartyMemberMapper.selectList(queryWrapper)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartMemberListByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            QueryWrapper q = new QueryWrapper();
            q.eq("dept_id", deptId);
            q.eq("del_flag", 0);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfPartyMemberMapper.selectList(q)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartMemberListByDeptIds(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            deptIds.add(Long.valueOf(deptId));
            QueryWrapper q = new QueryWrapper();
            q.in("dept_id", deptIds);
            q.eq("del_flag", 0);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfPartyMemberMapper.selectList(q)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> partyMemberLogin(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String phone = (String) map.get("phone");
            String password = (String) map.get("password");

            QueryWrapper q = new QueryWrapper();
            q.eq("phone", AESUtil2.encrypt(phone));
            q.eq("del_flag", 0);
            List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.selectList(q);
            if (partyMemberList.size() > 1) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在多个相同电话账户");
            }
            if (partyMemberList.size() != 0) {
                JcxfPartyMember partyMember = partyMemberList.get(0);

                String ipPassword = passwordManager.decryptPassword(password);
                String dbPassword = passwordManager.decryptPassword(partyMember.getPassword());
                if (ipPassword.equals(dbPassword)) {
                    JcxfSysDept tzSysDept = jcxfSysDeptService.getById(partyMember.getDeptId());
                    partyMember.setDeptName(tzSysDept.getName());
                    partyMember.setTenantId(tzSysDept.getTenantId());
                    partyMember.setPassword("");

                    saveLoginLog(partyMember, "1", "1");

                    UpdateWrapper updateWrapper = new UpdateWrapper();
                    updateWrapper.eq("id", partyMember.getId());
                    updateWrapper.set("app_login_date", new Date());
                    jcxfPartyMemberMapper.update(null, updateWrapper);

                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(partyMember));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "密码错误");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请输入正确的手机号");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "登录失败，手机号或密码有误");
        }
    }

    private void saveLoginLog(JcxfPartyMember partyMember, String type, String result) {
        // 登录日志
        TzLoginLog log = new TzLoginLog();
        log.setDeptId(partyMember.getDeptId());
        log.setPartyMemberId(partyMember.getId());
        log.setRealName(partyMember.getRealName());
        log.setIdCard(partyMember.getIdcard());
        log.setPhone(partyMember.getPhone());
        log.setCreateTime(new Date());
        log.setType(type); //   1、密码登录 2、短信验证码登录 3、统一认证登录 4微信登录
        log.setResult(result); // 1、成功 2、失败
        tzLoginLogMapper.insert(log);
    }

    @Override
    public Map<String, Object> partyMemberLoginByCode(Map<String, Object> requestMap) {
        try {
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String phone = (String) map.get("phone");
            String decryptPhone = (String) map.get("phone");
            phone = AESUtil2.encrypt(phone);
            String ipCode = (String) map.get("code");
            //获取缓存的验证码
            String redisCode = (String) redisUtils.get(decryptPhone);
            QueryWrapper q = new QueryWrapper();
            q.eq("phone", phone);
            q.eq("del_flag", 0);
            List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.selectList(q);
            if (partyMemberList != null || partyMemberList.size() == 0) {
                if (partyMemberList.size() > 1) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在多个相同电话账户");
                }

                if (ipCode.equals(redisCode)) {
                    JcxfPartyMember partyMember = partyMemberList.get(0);
                    JcxfSysDept tzSysDept = jcxfSysDeptService.getById(partyMember.getDeptId());
                    partyMember.setDeptName(tzSysDept.getName());
                    partyMember.setPassword("");

                    // 登录日志
                    TzLoginLog log = new TzLoginLog();
                    log.setDeptId(partyMember.getDeptId());
                    log.setPartyMemberId(partyMember.getId());
                    log.setRealName(partyMember.getRealName());
                    log.setPhone(phone);
                    log.setIdCard(partyMember.getIdcard());
                    log.setCreateTime(new Date());
                    log.setType("2"); //   1、密码登录 2、短信验证码登录 3、统一认证登录
                    log.setResult("1"); // 1、成功 2、失败
                    tzLoginLogMapper.insert(log);
                    redisUtils.del(decryptPhone);
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(partyMember));
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码错误");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请输入正确的手机号");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "登录异常:" + e.getMessage());
        }
    }


    @SneakyThrows
    @Override
    public Map<String, Object> wxLogin(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String code = (String) map.get("code");
        WxOAuth2AccessToken wxAccessToken = wxConfig.getWxUserAccessToken(code);
        String openId = wxAccessToken.getOpenId();
//        wxConfig.sendWxgMessage("ouwkV6ilptDf2Diz00Kk9rwul9ss", "X6d7CdOkzlCPwSTkexZaolIAly0nkQ_zTtR6DiGgYME", null, "https://app.jcjgdj.cn");


//        //判断openid是否绑定了党员，如果绑定了直接登录成功 ，如果没有绑定，返回openid给前端，前端跳转到手机号绑定界面获取验证码成功后绑定
        LambdaQueryWrapper<JcxfPartyMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JcxfPartyMember::getOpenid, openId);
        JcxfPartyMember selectOne = jcxfPartyMemberMapper.selectOne(queryWrapper);
        if (selectOne != null) {
            JcxfSysDept tzSysDept = jcxfSysDeptService.getById(selectOne.getDeptId());
            selectOne.setDeptName(tzSysDept.getName());
            saveLoginLog(selectOne, "4", "1");
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(selectOne));
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(222222,openId));
    }

    @Override
    public Map<String, Object> wxBindPhone(Map<String, Object> requestMap) {
        Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
        String vcode = (String) map.get("code");
        String phone = (String) map.get("phone");
        String openid = (String) map.get("openid");
        //获取缓存的验证码
        String redisCode = (String) redisUtils.get("bind:" + phone);
        if (vcode.equals(redisCode)) {
            LambdaQueryWrapper<JcxfPartyMember> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(JcxfPartyMember::getPhone, AESUtil2.encrypt(phone));
            queryWrapper.eq(JcxfPartyMember::getDelFlag, 0);
            JcxfPartyMember selectOne = jcxfPartyMemberMapper.selectOne(queryWrapper);
            if (selectOne == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请输入正确的手机号");
            }
            JcxfSysDept tzSysDept = jcxfSysDeptService.getById(selectOne.getDeptId());
            selectOne.setDeptName(tzSysDept.getName());
            //将openid和该手机号的党员绑定
            JcxfPartyMember partyMember = new JcxfPartyMember();
            partyMember.setId(selectOne.getId());
            partyMember.setOpenid(openid);
            jcxfPartyMemberMapper.updateById(partyMember);

            redisUtils.del("bind:" + phone);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(selectOne));//绑定成功
        } else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码错误");
        }
    }

    @Override
    public Map<String, Object> updatePartyMemberAvatar(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String avatar = (String) requestMap.get("avatar");

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("avatar", avatar);

            int res = jcxfPartyMemberMapper.update(null, updateWrapper);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改异常");
        }
    }

    @Override
    public Map<String, Object> partyMemberPasswordVerify(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String password = (String) requestMap.get("oldPassword");

            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(id);
            if (partyMember != null) {
                String ipPassword = passwordManager.decryptPassword(password);
                String dbPassword = passwordManager.decryptPassword(partyMember.getPassword()).substring(13);
                if (ipPassword.equals(dbPassword)) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "密码错误");
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取用户信息异常");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证异常");
        }
    }

    @Override
    public Map<String, Object> partyMemberPasswordUpdate(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String password = (String) requestMap.get("newPassword");

            //密码加密
            long time = new Date().getTime();
            String result = passwordManager.encryptPassword(time + password);

            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", id);
            up.set("password", result);

            int res = jcxfPartyMemberMapper.update(null, up);
            if (res > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改密码异常");
        }
    }

    @Override
    public Map<String, Object> getPoliticalBirthdayByDeptId(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            String type = (String) requestMap.get("type");

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // 注意月份是从0开始计数的
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(deptId);

            Long searchDeptId = null;
            if (tzSysDept.getParentId() == 0) {
                searchDeptId = Long.valueOf(tzSysDept.getId());
            } else {
                searchDeptId = Long.valueOf(tzSysDept.getParentIds().split(",")[1]);
            }

            List<Long> ids = jcxfSysDeptMapper.selectChildrenIdsById(searchDeptId);

            if (ids.size() > 0) {
                QueryWrapper q = new QueryWrapper();
                q.in("dept_id", ids);
                q.eq("del_flag", 0);
                q.lt("YEAR(party_time)", year);
                q.eq("MONTH(party_time)", month);
                q.eq("DAY(party_time)", day);

                if ("limit".equals(type)) {
                    q.last("limit 3");
                }
                q.orderByAsc("YEAR(party_time)");

                List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(q);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                for (JcxfPartyMember partyMember : list) {
                    String[] data = simpleDateFormat.format(partyMember.getPartyTime()).split("-");
                    partyMember.setPoliticsAge(year - Integer.parseInt(data[0]));
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));

            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new ArrayList()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPayFeePartyMemberByDeptId(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        String type = (String) requestMap.get("type");

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("id,real_name,idcard");

        /*if (type.equals("all")) {
            List<Integer> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Integer.parseInt(deptId));
            deptIds.add(Integer.parseInt(deptId));
            queryWrapper.in("dept_id", deptIds);
        } else if (type.equals("this")) {*/
        queryWrapper.eq("dept_id", deptId);
        /*} else {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"查询错误");
        }*/

        queryWrapper.eq("del_flag", 0);
        List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(queryWrapper);
        for (JcxfPartyMember jcxfPartyMember : list) {
            if (StringUtils.isNotBlank(jcxfPartyMember.getIdcard())) {
                jcxfPartyMember.setIdcard(jcxfPartyMember.getIdcard().substring(jcxfPartyMember.getIdcard().length() - 6));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在党员身份证为空的数据，请先完善党员信息");
            }
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(list));
    }

    @Override
    @DSTransactional
    public Map<String, Object> addPartyMember(Map<String, Object> requestMap) {
        try {
            JcxfPartyMember partyMember = BsinServiceContext.getReqBodyDto(JcxfPartyMember.class, (Map<String, Object>) requestMap);
            String tenantId = (String) requestMap.get("bizTenantId");
            if (StringUtils.isNotBlank(partyMember.getIdcard()) && !partyMember.getIdcard().contains("*")) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("idcard", AESUtil2.encrypt(partyMember.getIdcard()));
                queryWrapper.eq("del_flag", false);
                if (partyMember.getId() != null) {
                    queryWrapper.ne("id", partyMember.getId());
                }
                int count = jcxfPartyMemberMapper.selectCount(queryWrapper);
                if (count > 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "身份证号重复");
                }
            }
            if (StringUtils.isNotBlank(partyMember.getPhone()) && !partyMember.getPhone().contains("*")) {
                QueryWrapper queryWrapper2 = new QueryWrapper();
                queryWrapper2.eq("phone", AESUtil2.encrypt(partyMember.getPhone()));
                queryWrapper2.eq("del_flag", false);
                if (partyMember.getId() != null) {
                    queryWrapper2.ne("id", partyMember.getId());
                }
                int count2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);
                if (count2 > 0) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "手机号重复");
                }
            }

            String isPartyWorker = partyMember.getIsPartyWorker();
            if ("0".equals(isPartyWorker)) {
                partyMember.setPartyWorkerTips(null);
            }

            JcxfSysDictionary dictionary = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyState", "已死亡");
            JcxfSysDictionary dictionary2 = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyState", "停止党籍");
            JcxfSysDictionary expelledParty = jcxfSysDictionaryMapper.getByCodeAndDetailName("partyState", "开除党籍");

            if (partyMember.getId() != null) {
                JcxfPartyMember partyMemberOld = jcxfPartyMemberMapper.selectById(partyMember.getId());
                String beforPassword = partyMemberOld.getPassword();
                if (partyMember.getPartyState() != null) {
                    if (dictionary != null && partyMember.getPartyState().toString().equals(dictionary.getDetail())) {
                        //党籍状态为已死亡，设置状态为已删除
                        partyMember.setDelFlag(true);
                        //如果改党员已关联了管理员，直接删除关联关系
                        deleteSysUserParty(partyMember.getId());
                    }
                    if (dictionary2 != null && partyMember.getPartyState().toString().equals(dictionary2.getDetail())) {
                        //党籍状态为停止党籍，删除管理员和党员关联关系
                        deleteSysUserParty(partyMember.getId());
                    }
                    //czw 编写开除党籍逻辑
                    if (expelledParty != null && partyMember.getPartyState().toString().equals(expelledParty.getDetail())) {
                        //党籍状态为已死亡，设置状态为已删除
                        partyMember.setDelFlag(true);
                        //如果改党员已关联了管理员，直接删除关联关系
                        deleteSysUserParty(partyMember.getId());
                    }
                }

                if ("1".equals(partyMember.getIsLost())) {
                    //改为失联党员，如果已关联党员，取消关联关系
                    deleteSysUserParty(partyMember.getId());
                }

                partyMember.setUpdateDate(new Date());
                partyMember.setPassword(beforPassword);
                partyMember.setUpdateDate(new Date());
                if (partyMember.getIdcard().contains("*")) {
                    partyMember.setIdcard(null);
                }
                if (partyMember.getPhone().contains("*")) {
                    partyMember.setPhone(null);
                }
                boolean res = updateById(partyMember);
                if (res) {
                    updateFloatingMember(partyMember);
                    LambdaQueryWrapper<TzPayFeeDetail> queryWrapper = new LambdaQueryWrapper<>();
                    queryWrapper.eq(TzPayFeeDetail::getPartyMemberId, partyMember.getId());
                    queryWrapper.isNotNull(TzPayFeeDetail::getPaymentBase);
                    queryWrapper.orderByDesc(TzPayFeeDetail::getId);
                    queryWrapper.last("LIMIT 1");
                    TzPayFeeDetail tzPayFeeDetail = tzPayFeeDetailMapper.selectOne(queryWrapper);
                    if (tzPayFeeDetail != null) {
                        //更新党员的基数
                        String workPosition = partyMember.getWorkPosition();
                        Map<String, Object> hashMap = summary(tzPayFeeDetail.getPaymentBase(), workPosition);
                        LambdaUpdateWrapper<TzPayFeeDetail> updateWrapper = new LambdaUpdateWrapper<>();
                        updateWrapper.eq(TzPayFeeDetail::getPartyMemberId, partyMember.getId());
                        updateWrapper.eq(TzPayFeeDetail::getStatus, 0);
                        updateWrapper.ge(TzPayFeeDetail::getPayMonth, DateUtil.format(new Date(), "yyyy-MM"));
                        updateWrapper.set(TzPayFeeDetail::getPaymentRatio, hashMap.get("proportion").toString());
                        updateWrapper.set(TzPayFeeDetail::getShouldPay, new BigDecimal(hashMap.get("shouldPay").toString()));
                        updateWrapper.set(TzPayFeeDetail::getActuallyPay, new BigDecimal(hashMap.get("shouldPay").toString()));
                        tzPayFeeDetailMapper.update(null, updateWrapper);
                    }
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());

                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
                }
            } else {
                partyMember.setCreateDate(new Date());

                if (StringUtils.isNotBlank(partyMember.getIdcard())) {
                    //默认密码
                    String idcard = partyMember.getIdcard();
                    String substring = null;
                    //初始密码默认为身份证号后6位
                    if (idcard != null && idcard != "") {
                        substring = idcard.substring(12);
                    }
                    //密码加密
                    String result = passwordManager.encryptPassword(substring);
                    partyMember.setPassword(result);
                }

                partyMember.setUsername(partyMember.getPhone());

                if (dictionary != null && partyMember.getPartyState() != null && partyMember.getPartyState().toString().equals(dictionary.getDetail())) {
                    partyMember.setDelFlag(true);//如果党籍状态为已死亡，设置状态为已删除
                } else {
                    partyMember.setDelFlag(false);
                }
                partyMember.setTenantId(tenantId);
                boolean res = save(partyMember);
                if (res) {
                    updateFloatingMember(partyMember);
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                } else {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "保存失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), e.getMessage());
        }
    }

    private Map<String, Object> summary(String paymentBase, String workPosition) {
        if (StringUtils.isEmpty(paymentBase)) {
            paymentBase = "0.0";
        }
        BigDecimal num1 = new BigDecimal(paymentBase);
        Double shouldPay = 0.00;
        String radio = "";
        String proportion = "";
        if (Objects.equals("502", workPosition) || Objects.equals("501", workPosition)
                || Objects.equals("503", workPosition) || Objects.equals("504", workPosition)) {
            //退休人员 5000及5000以下 0.5%  5000以上1%
            if (num1.doubleValue() > 5000) {
                radio = "0.01";
                proportion = "1%";
            } else {
                radio = "0.005";
                proportion = "0.5%";
            }
        } else {
            //在职人员 3000及3000以下 0.5% 3000以上至5000 1% 5000至1万 1.5%  1万以上2%
            if (num1.doubleValue() <= 3000) {
                radio = "0.005";
                proportion = "0.5%";
            } else if (num1.doubleValue() > 3000 && num1.doubleValue() <= 5000) {
                radio = "0.01";
                proportion = "1%";
            } else if (num1.doubleValue() > 5000 && num1.doubleValue() <= 10000) {
                radio = "0.015";
                proportion = "1.5%";
            } else {
                radio = "0.02";
                proportion = "2%";
            }
        }
        BigDecimal num2 = new BigDecimal(radio);
        BigDecimal multiply = num1.multiply(num2);
        BigDecimal bigDecimal = multiply.setScale(1, BigDecimal.ROUND_UP);
        shouldPay = bigDecimal.doubleValue();
        if (shouldPay < 0.1) {
            shouldPay = 0.1;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("shouldPay", shouldPay);
        map.put("proportion", proportion);
        return map;
    }

    public void deleteSysUserParty(Long partyId) {
        QueryWrapper updateWrapper = new QueryWrapper();
        updateWrapper.eq("party_id", partyId);
        jcxfSysUserPartyMapper.delete(updateWrapper);
    }

    private int updateFloatingMember(JcxfPartyMember partyMember) {
        int result = 0;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("party_id", partyMember.getId());
        queryWrapper.eq("dept_id", partyMember.getDeptId());
        queryWrapper.eq("del_flag", false);

        List<JcxfPartyFloatingMember> members = jcxfPartyFloatingMemberMapper.selectList(queryWrapper);

        if ("1".equals(partyMember.getIsFlow())) {
            if (null == members || (null != members && members.size() == 0)) {
                JcxfPartyFloatingMember member = new JcxfPartyFloatingMember();
                member.setFloatingType(2);
                member.setPartyId(partyMember.getId());
                member.setDeptId(partyMember.getDeptId());
                member.setRealName(partyMember.getRealName());
                member.setSex(String.valueOf(partyMember.getSex()));
                member.setNational(String.valueOf(partyMember.getNational()));
                member.setIdcard(partyMember.getIdcard());
                member.setPhone(partyMember.getPhone());
                member.setFloatingStatus("0");
                member.setFloatingPlace(partyMember.getFlowPlace());
                member.setFloatingBack("0");
                member.setDelFlag(false);
                member.setCreateDate(new Date());
                result = jcxfPartyFloatingMemberMapper.insert(member);
            }
        } else {
            if (null != members && members.size() > 0) {
                for (JcxfPartyFloatingMember member : members) {
                    result += jcxfPartyFloatingMemberMapper.deleteById(member);
                }
            }
        }
        return result;
    }

    /**
     * 删除单个党员
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> deletePartyMemberInfo(Map<String, Object> requestMap) {
        List<String> ids = (List<String>) requestMap.get("ids");

        if (ids == null || ids.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请选择需要删除的数据");
        }
        try {
            QueryWrapper<JcxfPartyMember> updateWrapper = new QueryWrapper<>();
            updateWrapper.in("party_member.id", ids);
            int count = jcxfPartyMemberMapper.delete(updateWrapper);
            if (count > 0) {
                //删除后需要把党费数据也删除了
                LambdaQueryWrapper<TzPayFeeDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.in(TzPayFeeDetail::getPartyMemberId, ids);
                tzPayFeeDetailMapper.delete(lambdaQueryWrapper);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "删除异常");
        }
    }

    @Override
    public Map<String, Object> queryPartyMemberList(Map<String, Object> requestMap) {
        try {

            JcxfPartyMember partyMember = BsinServiceContext.getReqBodyDto(JcxfPartyMember.class, (Map<String, Object>) requestMap.get("data"));
            SearchVo searchVo = BsinServiceContext.getReqBodyDto(SearchVo.class, (Map<String, Object>) requestMap.get("data"));
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            String today = partyMember.getToday();

            // 是否只查询当前部门的人员
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String onlyThisDept = (String) map.get("onlyThisDept");

            List<Long> listAll = new ArrayList<>();

            if (StringUtils.isBlank(onlyThisDept)) {
                //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
                if (partyMember.getDeptId() != null) {
                    listAll = jcxfSysDeptMapper.selectChildrenIdsById(partyMember.getDeptId());
                    listAll.add(partyMember.getDeptId());
                }
            } else {
                listAll.add(partyMember.getDeptId());
            }
            if (partyMember != null) {
                partyMember.setDelFlag(false);
            } else {
                partyMember = new JcxfPartyMember();
                partyMember.setDelFlag(false);
            }
            IPage<JcxfPartyMember> result = queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARTYMEMBER_PAGE_EXCEPTION);
        }

    }

    @Override
    public Map<String, Object> getPartyMemberList(Map<String, Object> requestMap) {
        try {

            JcxfPartyMember partyMember = BsinServiceContext.getReqBodyDto(JcxfPartyMember.class, (Map<String, Object>) requestMap.get("data"));
            SearchVo searchVo = BsinServiceContext.getReqBodyDto(SearchVo.class, (Map<String, Object>) requestMap.get("data"));
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            String today = partyMember.getToday();

            // 是否只查询当前部门的人员
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            String onlyThisDept = (String) map.get("onlyThisDept");

            List<Long> listAll = new ArrayList<>();

            if (StringUtils.isBlank(onlyThisDept)) {
                //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
                if (partyMember.getDeptId() != null) {
                    listAll = jcxfSysDeptMapper.selectChildrenIdsById(partyMember.getDeptId());
                    listAll.add(partyMember.getDeptId());
                }
            } else {
                listAll.add(partyMember.getDeptId());
            }
            if (partyMember != null) {
                partyMember.setDelFlag(false);
            } else {
                partyMember = new JcxfPartyMember();
                partyMember.setDelFlag(false);
            }
            IPage<JcxfPartyMemberVo> result = getPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            for (JcxfPartyMemberVo partyMemberVo : result.getRecords()) {
                partyMemberVo.setPhone(AESUtil2.decrypt(partyMemberVo.getPhone()));
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARTYMEMBER_PAGE_EXCEPTION);
        }

    }

    /**
     * 重置密码
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> resetPassword(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("id");
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数不能为空");
        }
        try {
            JcxfPartyMember partyMember = getById(id);
            if (partyMember != null) {
                String idcard = AESUtil2.decrypt(partyMember.getIdcard());
                String substring = null;
                if (idcard != null && idcard != "") {
                    substring = idcard.substring(idcard.length() - 6);
                }
                String result = passwordManager.encryptPassword(substring);

                UpdateWrapper<JcxfPartyMember> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("party_member.id", id);
                updateWrapper.set("password", result);
                boolean update = update(null, updateWrapper);
                if (!update) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "重置失败");
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "党员数据为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "重置异常");
        }
    }

    @Override
    @DS("jcxf")
    @DSTransactional
    public Map<String, Object> resetDeptTreePartyMemberPassword(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("deptId");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请选择部门");
            }

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(id));
            deptIds.add(Long.valueOf(id));

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.in("dept_id", deptIds);
            List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.selectList(queryWrapper);

            for (JcxfPartyMember partyMember : memberList) {
                String idcard = partyMember.getIdcard();
                String substring = null;
                if (idcard != null && idcard != "") {
                    substring = idcard.substring(12);
                }
                long time = new Date().getTime();
                String result = passwordManager.encryptPassword(time + substring);

                UpdateWrapper updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("party_member.id", partyMember.getId());
                updateWrapper.set("password", result);
                int count = jcxfPartyMemberMapper.update(null, updateWrapper);
                if (count == 0) {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "重置出错");
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "重置异常");
        }
    }

    @Override
    public Map<String, Object> queryDeletePartyMemberList(Map<String, Object> requestMap) {
        JcxfPartyMember partyMember = BsinServiceContext.getReqBodyDto(JcxfPartyMember.class, (Map<String, Object>) requestMap.get("data"));
        SearchVo searchVo = BsinServiceContext.getReqBodyDto(SearchVo.class, (Map<String, Object>) requestMap.get("data"));
        PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
        String today = (String) requestMap.get("today");
        try {
            //QueryWrapper<TzSysDept> queryWrapper = new QueryWrapper<TzSysDept>();
            List<Long> listAll = new ArrayList<>();
            partyMember.setDelFlag(true);
            //根据党组织id查询该组织下辖的所有组织的id，在业务层根据这些id集合查询相对应的党员
            if (partyMember.getDeptId() != null) {
                listAll = jcxfSysDeptMapper.selectChildrenIdsById(partyMember.getDeptId());
                listAll.add(partyMember.getDeptId());
            }
            IPage<JcxfPartyMember> result = queryPartyMemberListByPage(partyMember, searchVo, pageVo, listAll, today);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARTYMEMBER_INFO_EXCEPTION);
        }
    }

    /**
     * 党员信息恢复
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> recoverDeletePartyMemberInfo(Map<String, Object> requestMap) {
        String[] ids = (String[]) requestMap.get("ids");
        if (ids == null || ids.length == 0) {
            throw new BusinessException(ResponseCode.PARAMS_EMPTY_EXCEPTION);
        }
        try {
            UpdateWrapper<JcxfPartyMember> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("party_member.del_flag", 0).set("party_member.update_date", new Date()).in("party_member.id", ids);
            boolean update = update(updateWrapper);
            //boolean res = partyMemberService.removeByIds(Arrays.asList(ids));
            if (update) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(update));
            } else {
                throw new BusinessException(ResponseCode.PARTYMEMBER_REVOVER_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARTYMEMBER_REVOVER_EXCEPTION);
        }
    }

    /**
     * 头像上传
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> uploadImages(Map<String, Object> requestMap) {
        String imgUrl = (String) requestMap.get("imgUrl");
        //base64 转文件
        MultipartFile imgFile = BASE64DecodedMultipartFile.base64ToMultipart(imgUrl);
        //文件存储在nginx代理路径下
        String fileName = UploadFileUtils.uploadFile(imgFile);
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(fileName));


    }

    @Override
    public Map<String, Object> getPartyInfo(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            if (StringUtils.isEmpty(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取失败");
            }

            JcxfPartyMember res = getById(id);
            if (res != null) {
                // 根据result拿到的deptId去查询对应的所属部门
                if (res.getDeptId() != null) {
                    JcxfSysDept tzSysDept = jcxfSysDeptService.getById(res.getDeptId());
                    res.setDeptName(tzSysDept.getName());
                }
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));

            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "获取异常");
        }
    }

    @Override
    public Map<String, Object> getPartyInfoByApp(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            if (StringUtils.isEmpty(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数不能为空");
            }

            JcxfPartyMember res = getById(id);
            if (res != null) {
                if (res.getDelFlag() == true) {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
                }

                // 根据result拿到的deptId去查询对应的所属部门
                if (res.getDeptId() != null) {
                    JcxfSysDept tzSysDept = jcxfSysDeptService.getById(res.getDeptId());
                    res.setDeptName(tzSysDept.getName());
                }
                // 不返回密码信息
                res.setPassword(null);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(res));

            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(null));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    public IPage<JcxfPartyMember> queryPartyMemberListByPage(JcxfPartyMember partyMember, SearchVo searchVo, PageVo pageVo, List<Long> listAll, String today) {
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

        Page<JcxfPartyMember> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();

        if (partyMember != null) {
            queryWrapper = LikeAllField(partyMember, searchVo);
        }

        //查询当日新增党员
        if (StringUtils.isNotEmpty(today)) {
            queryWrapper.and(i -> i.like("party_member.create_date", today));
        }
        if (listAll == null || listAll.size() == 0) {
            return null;
        }
        //根据所属组织id集合查询所有党员
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("party_member.dept_id", listAll));
        }

        IPage<JcxfPartyMember> result = jcxfPartyMemberMapper.getJcxfPartyMemberByPage(pageData, queryWrapper);
        return result;
    }

    public IPage<JcxfPartyMemberVo> getPartyMemberListByPage(JcxfPartyMember partyMember, SearchVo searchVo, PageVo pageVo, List<Long> listAll, String today) {
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

        Page<JcxfPartyMember> pageData = new Page<>(page, limit);
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();

        if (partyMember != null) {
            queryWrapper = LikeAllField(partyMember, searchVo);
        }

        //查询当日新增党员
        if (StringUtils.isNotEmpty(today)) {
            queryWrapper.and(i -> i.like("party_member.create_date", today));
        }
        if (listAll == null || listAll.size() == 0) {
            return null;
        }
        //根据所属组织id集合查询所有党员
        if (listAll != null && listAll.size() > 0) {
            queryWrapper.and(i -> i.in("party_member.dept_id", listAll));
        }

        IPage<JcxfPartyMemberVo> result = jcxfPartyMemberMapper.getPartyMemberListByPage(pageData, queryWrapper);
        return result;
    }

    public QueryWrapper<JcxfPartyMember> LikeAllField(JcxfPartyMember partyMember, SearchVo searchVo) {
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
        if (partyMember.getId() != null) {
            queryWrapper.and(i -> i.eq("party_member.id", partyMember.getId()));
        }
        if (StringUtils.isNotEmpty(partyMember.getUsername())) {
            queryWrapper.and(i -> i.like("party_member.username", partyMember.getUsername()));
        }
        if (StringUtils.isNotEmpty(partyMember.getPassword())) {
            queryWrapper.and(i -> i.like("party_member.password", partyMember.getPassword()));
        }
      /*  if (partyMember.getDeptId() != null) {
            queryWrapper.and(i -> i.eq("party_member.dept_id", partyMember.getDeptId()));
        }*/
        if (StringUtils.isNotEmpty(partyMember.getDeptOutId())) {
            queryWrapper.and(i -> i.like("party_member.dept_out_id", partyMember.getDeptOutId()));
        }
        if (StringUtils.isNotEmpty(partyMember.getRealName())) {
            queryWrapper.and(i -> i.like("party_member.real_name", partyMember.getRealName()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIdcard())) {
            queryWrapper.and(i -> i.eq("party_member.idcard", AESUtil2.encrypt(partyMember.getIdcard())));
        }
        if (partyMember.getSex() != null) {
            queryWrapper.and(i -> i.like("party_member.sex", partyMember.getSex()));
        }
        if (StringUtils.isNotEmpty(partyMember.getNational())) {
            queryWrapper.and(i -> i.eq("party_member.national", partyMember.getNational()));
        }
        if (partyMember.getBirthday() != null) {
            queryWrapper.and(i -> i.like("party_member.birthday", partyMember.getBirthday()));
        }
        if (StringUtils.isNotEmpty(partyMember.getEducation())) {
            queryWrapper.and(i -> i.like("party_member.education", partyMember.getEducation()));
        }
        if (partyMember.getPartyTime() != null) {
            queryWrapper.and(i -> i.like("party_member.party_time", partyMember.getPartyTime()));
        }
        if (partyMember.getTransferTime() != null) {
            queryWrapper.and(i -> i.like("party_member.transfer_time", partyMember.getTransferTime()));
        }

        if (partyMember.getButtonRole() != null && "cancel_role".equals(partyMember.getButtonRole())) {
            queryWrapper.and(i -> i.isNull("party_member.button_role").or(i2 -> i2.eq("party_member.button_role", partyMember.getButtonRole())));
        } else if (StringUtils.isNotEmpty(partyMember.getButtonRole())) {
            queryWrapper.and(i -> i.eq("party_member.button_role", partyMember.getButtonRole()));
        }

        if (partyMember.getPosition() != null) {
            queryWrapper.and(i -> i.eq("party_member.position", partyMember.getPosition()));
        }
        if (partyMember.getPersonType() != null) {
            queryWrapper.and(i -> i.eq("party_member.person_type", partyMember.getPersonType()));
        }
        if (StringUtils.isNotEmpty(partyMember.getPhone())) {
            queryWrapper.and(i -> i.eq("party_member.phone", AESUtil2.encrypt(partyMember.getPhone())));
        }
        if (StringUtils.isNotEmpty(partyMember.getTelephone())) {
            queryWrapper.and(i -> i.like("party_member.telephone", partyMember.getTelephone()));
        }
        if (StringUtils.isNotEmpty(partyMember.getAddress())) {
            queryWrapper.and(i -> i.like("party_member.address", partyMember.getAddress()));
        }
        if (partyMember.getPartyState() != null) {
            queryWrapper.and(i -> i.eq("party_member.party_state", partyMember.getPartyState()));
        }
        if (partyMember.getPartyStateDate() != null) {
            queryWrapper.and(i -> i.like("party_member.party_state_date", partyMember.getPartyStateDate()));
        }
        if (partyMember.getReserveTime() != null) {
            queryWrapper.and(i -> i.like("party_member.reserve_time", partyMember.getReserveTime()));
        }
        if (partyMember.getRecoverTime() != null) {
            queryWrapper.and(i -> i.like("party_member.recover_time", partyMember.getRecoverTime()));
        }
        if (partyMember.getStopTime() != null) {
            queryWrapper.and(i -> i.like("party_member.stop_time", partyMember.getStopTime()));
        }
        if (partyMember.getDismissTime() != null) {
            queryWrapper.and(i -> i.like("party_member.dismiss_time", partyMember.getDismissTime()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsLost())) {
            queryWrapper.and(i -> i.like("party_member.is_lost", partyMember.getIsLost()));
        }
        if (partyMember.getLostTime() != null) {
            queryWrapper.and(i -> i.like("party_member.lost_time", partyMember.getLostTime()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsFlow())) {
            queryWrapper.and(i -> i.eq("party_member.is_flow", partyMember.getIsFlow()));
        }
        if (StringUtils.isNotEmpty(partyMember.getFlowPlace())) {
            queryWrapper.and(i -> i.like("party_member.flow_place", partyMember.getFlowPlace()));
        }
        if (StringUtils.isNotEmpty(partyMember.getWorkPosition())) {
            queryWrapper.and(i -> i.like("party_member.work_position", partyMember.getWorkPosition()));
        }
        if (StringUtils.isNotEmpty(partyMember.getAvatar())) {
            queryWrapper.and(i -> i.like("party_member.avatar", partyMember.getAvatar()));
        }
        if (StringUtils.isNotEmpty(partyMember.getProfilePhoto())) {
            queryWrapper.and(i -> i.like("party_member.profile_photo", partyMember.getProfilePhoto()));
        }
        if (partyMember.getCreateBy() != null) {
            queryWrapper.and(i -> i.like("party_member.create_by", partyMember.getCreateBy()));
        }
        if (partyMember.getCreateDate() != null) {
            queryWrapper.and(i -> i.like("party_member.create_date", partyMember.getCreateDate()));
        }
        if (partyMember.getUpdateBy() != null) {
            queryWrapper.and(i -> i.like("party_member.update_by", partyMember.getUpdateBy()));
        }
        if (partyMember.getUpdateDate() != null) {
            queryWrapper.and(i -> i.like("party_member.update_date", partyMember.getUpdateDate()));
        }
        if (partyMember.getDelFlag() != null) {
            queryWrapper.and(i -> i.eq("party_member.del_flag", partyMember.getDelFlag()));
        }
        if (StringUtils.isNotEmpty(partyMember.getPartyMemberLabel())) {
            queryWrapper.and(i -> i.like("party_member.party_member_label", partyMember.getPartyMemberLabel()));
        }
        if (partyMember.getIsIgnore() != null) {
            queryWrapper.and(i -> i.like("party_member.is_ignore", partyMember.getIsIgnore()));
        }
        if (partyMember.getAppLoginDate() != null) {
            queryWrapper.and(i -> i.like("party_member.app_login_date", partyMember.getAppLoginDate()));
        }
        if (StringUtils.isNotEmpty(partyMember.getOpenid())) {
            queryWrapper.and(i -> i.like("party_member.openid", partyMember.getOpenid()));
        }
        if (partyMember.getAppModifyLoginDate() != null) {
            queryWrapper.and(i -> i.like("party_member.app_modify_login_date", partyMember.getAppModifyLoginDate()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsFirstSecretary())) {
            queryWrapper.and(i -> i.like("party_member.is_first_secretary", partyMember.getIsFirstSecretary()));
        }
        if (StringUtils.isNotEmpty(partyMember.getNickName())) {
            queryWrapper.and(i -> i.like("party_member.nick_name", partyMember.getNickName()));
        }
        if (partyMember.getAreaId() != null) {
            queryWrapper.and(i -> i.like("party_member.area_id", partyMember.getAreaId()));
        }
        if (StringUtils.isNotEmpty(partyMember.getAreaName())) {
            queryWrapper.and(i -> i.like("party_member.area_name", partyMember.getAreaName()));
        }
        if (StringUtils.isNotEmpty(partyMember.getWorkUnit())) {
            queryWrapper.and(i -> i.like("party_member.work_unit", partyMember.getWorkUnit()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsFloatIn())) {
            queryWrapper.and(i -> i.like("party_member.is_float_in", partyMember.getIsFloatIn()));
        }
        if (StringUtils.isNotEmpty(partyMember.getFloatInPlace())) {
            queryWrapper.and(i -> i.like("party_member.float_in_place", partyMember.getFloatInPlace()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsSuffer())) {
            queryWrapper.and(i -> i.like("party_member.is_suffer", partyMember.getIsSuffer()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsPoverty())) {
            queryWrapper.and(i -> i.like("party_member.is_poverty", partyMember.getIsPoverty()));
        }
        if (StringUtils.isNotEmpty(partyMember.getIsOutsideCity())) {
            queryWrapper.and(i -> i.like("party_member.is_outside_city", partyMember.getIsOutsideCity()));
        }
        //if (partyMember.getPayMoney() != null) {
        //    queryWrapper.and(i -> i.like("party_member.pay_money", partyMember.getPayMoney()));
        //}
        if (partyMember.getProxyId() != null) {
            queryWrapper.and(i -> i.like("party_member.proxy_id", partyMember.getProxyId()));
        }
        if (partyMember.getFamilySort() != null) {
            queryWrapper.and(i -> i.like("party_member.family_sort", partyMember.getFamilySort()));
        }

        if (partyMember.getIsActive() != null) {
            if (partyMember.getIsActive() == 1) {
                queryWrapper.isNotNull("party_member.app_modify_login_date");
            } else {
                queryWrapper.isNull("party_member.app_modify_login_date");
            }
        }

        /*if (StringUtils.isNotEmpty(partyMember.getFloatingOutType())) {
            queryWrapper.and(i -> i.like("party_member.floating_out_type", partyMember.getFloatingOutType()));
        }
        if (StringUtils.isNotEmpty(partyMember.getFloatingStatus())) {
            queryWrapper.and(i -> i.like("party_member.floating_status", partyMember.getFloatingStatus()));
        }
        if (StringUtils.isNotEmpty(partyMember.getFlowType())) {
            queryWrapper.and(i -> i.eq("party_member.flow_type", partyMember.getFlowType()));
        }*/
        if (searchVo != null) {
            if (searchVo.getStartDate() != null && searchVo.getEndDate() != null) {
                queryWrapper.and(i -> i.between("party_member.create_date", searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }

        return queryWrapper;

    }

    /**
     * 根据组织id查询激活党员数量
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> queryCountActive(Map<String, Object> requestMap) {
        try {
            JcxfPartyMember partyMember = BsinServiceContext.getReqBodyDto(JcxfPartyMember.class, (Map<String, Object>) requestMap.get("partyMember"));

            HashMap<String, Integer> map = new HashMap<>();
            //根据党员所属组织id，查询出对应的组织以及组织下面的子组织，并把这些组织id放在list集合中
            if (partyMember.getDeptId() == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "部门id为空，请联系管理员");
            }

            List<Long> listAll = jcxfSysDeptMapper.selectChildrenIdsById(partyMember.getDeptId());
            listAll.add(partyMember.getDeptId());

            QueryWrapper<JcxfPartyMember> wrapper = new QueryWrapper<>();
            QueryWrapper<JcxfPartyMember> wrapper2 = new QueryWrapper<>();
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
            int unActive = count(wrapper);
            int active = count(wrapper2);
            map.put("active", active);
            map.put("unActive", unActive);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.PARTYMEMBER_ACTIVE_EXCEPTION);
        }
    }

    @Override
    public Map<String, Object> queryOneselfPartyMemberList(Map<String, Object> requestMap) {
        JcxfPartyMember partyMember = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfPartyMember.class);
        if (partyMember.getDeptId() == null) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空，请联系管理员");
        }
        try {
            List<JcxfPartyMember> result = queryOneselfPartyMemberList(partyMember);
            if (result != null && result.size() > 0) {
                for (JcxfPartyMember member : result) {
                    member.setKey(member.getId().toString());
                    member.setLabel(member.getRealName());
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    public List<JcxfPartyMember> queryOneselfPartyMemberList(JcxfPartyMember partyMember) {
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
        partyMember.setDelFlag(false);
        partyMember.setIsFlow("0");
        String deptId = partyMember.getDeptId().toString();
        // queryWrapper.and(i -> i.eq("party_member.is_develop", "0").or(i2 -> i2.isNull("party_member.is_develop")));
        queryWrapper.and(i -> i.eq("party_member.dept_id", deptId));
        // queryWrapper.and(i -> i.eq("party_member.is_flow", partyMember.getIsFlow()).or(i2 -> i2.isNull("party_member.is_flow")).or(i2 -> i2.eq("party_member.is_flow", "")));
        queryWrapper.and(i -> i.eq("party_member.del_flag", partyMember.getDelFlag()));
        List<JcxfPartyMember> partyMembers = jcxfPartyMemberMapper.selectList(queryWrapper);
        return partyMembers;
    }

    public Map<String, Object> getInfo(List<Long> deptIds) {
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_member.del_flag", 0);
        //根据所属组织id集合查询所有党员
        if (deptIds != null && deptIds.size() > 0) {
            queryWrapper.and(i -> i.in("party_member.dept_id", deptIds));
        } else {
            // 设置默认数据---------------
            Map<String, Object> map = new HashMap<>();
            map.put("man", 0);
            map.put("woMan", 0);
            map.put("unKnownSex", 0);
            map.put("lost", 0);
            map.put("unLost", 0);
            map.put("poverty`", 0);
            map.put("unPoverty", 0);
            map.put("flux", 0);
            map.put("unFlux", 0);
            map.put("total", 0);
            map.put("partyWork", 0);
            map.put("unPartyWork", 0);

            Integer[] partyAges = {0, 0, 0, 0, 0, 0};
            map.put("partyAges", partyAges);

            Integer[] ages = {0, 0, 0, 0, 0, 0};
            map.put("ages", ages);

            Integer[] education = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            map.put("education", education);

            //Integer[] positionList = {0, 0, 0, 0, 0, 0, 0};
            List<Map<String, Object>> positionList = new ArrayList<>();
            HashMap<String, Object> hashMap1 = new HashMap();
            hashMap1.put("name", "其他");
            hashMap1.put("value", 0);
            positionList.add(hashMap1);
            HashMap<String, Object> hashMap2 = new HashMap();
            hashMap2.put("name", "无");
            hashMap2.put("value", 0);
            positionList.add(hashMap2);
            HashMap<String, Object> hashMap3 = new HashMap();
            hashMap3.put("name", "宣传委员");
            hashMap3.put("value", 0);
            positionList.add(hashMap3);
            HashMap<String, Object> hashMap4 = new HashMap();
            hashMap4.put("name", "纪检委员 ");
            hashMap4.put("value", 0);
            positionList.add(hashMap4);
            HashMap<String, Object> hashMap5 = new HashMap();
            hashMap5.put("name", "组织委员");
            hashMap5.put("value", 0);
            positionList.add(hashMap5);
            HashMap<String, Object> hashMap6 = new HashMap();
            hashMap6.put("name", "副书记");
            hashMap6.put("value", 0);
            positionList.add(hashMap6);
            HashMap<String, Object> hashMap7 = new HashMap();
            hashMap7.put("name", "书记");
            hashMap7.put("value", 0);
            positionList.add(hashMap1);

            map.put("position", positionList);
            map.put("office", 0);
            map.put("pre", 0);
            return map;
        }

        QueryWrapper q = new QueryWrapper();

        List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.getInfo(deptIds);
        Integer unKnownSex = 0;
        Map<String, Object> map = new HashMap<>();
        if (partyMemberList != null && partyMemberList.size() > 0) {

            //性别统计
            Integer nullList = 0;
            Integer manList = 0;
            Integer woManList = 0;
            Integer unKnowList = 0;

            // 失联人数统计
            Integer lostList = 0;
            Integer unLostList = 0;

            //预备党员统计
            Integer office = 0;
            Integer pre = 0;

            // 贫困党员统计
            Integer povertyList = 0;
            Integer unPovertyList = 0;

            // 贫困党员统计
            Integer partyWorkList = 0;
            Integer unPartyWorkList = 0;

            // 是否是流动党员统计
            Integer fluxList = 0;
            Integer unfluxList = 0;
            for (JcxfPartyMember i : partyMemberList) {

                //性别统计
                if (i != null && i.getSex() == null) {
                    nullList++;
                }
                if (i != null && i.getSex() != null && i.getSex() == 1) {
                    manList++;
                }
                if (i != null && i.getSex() != null && i.getSex() == 2) {
                    woManList++;
                }
                if (i != null && i.getSex() != null && i.getSex() == 0) {
                    unKnowList++;
                }

                // 失联人数统计
                if (i != null && StringUtils.isNotBlank(i.getIsLost()) && i.getIsLost().equals("1")) {
                    lostList++;
                }
                if (i != null && StringUtils.isNotBlank(i.getIsLost()) && i.getIsLost().equals("0")) {
                    unLostList++;
                }

                // 预备党员统计
                if (i != null && i.getPersonType() != null && i.getPersonType().equals(1)) {
                    office++;
                }
                if (i != null && i.getPersonType() != null && i.getPersonType().equals(2)) {
                    pre++;
                }

                // 贫困党员统计
                if (i != null && StringUtils.isNotBlank(i.getIsPoverty()) && i.getIsPoverty().equals("1")) {
                    povertyList++;
                }
                if (i != null && StringUtils.isNotBlank(i.getIsPoverty()) && i.getIsPoverty().equals("0")) {
                    unPovertyList++;
                }
                // 党务工作者统计
                if (i != null && StringUtils.isNotBlank(i.getIsPartyWorker()) && i.getIsPartyWorker().equals("1")) {
                    partyWorkList++;
                }
                if ((i != null && StringUtils.isNotBlank(i.getIsPartyWorker()) && i.getIsPartyWorker().equals("0")) || (i != null && i.getIsPartyWorker() == null)) {
                    unPartyWorkList++;
                }

                // 是否是流动党员统计
                if (i != null && (StringUtils.isNotBlank(i.getIsFlow()) && i.getIsFlow().equals("1")) || (StringUtils.isNotBlank(i.getIsFloatIn()) && i.getIsFloatIn().equals("1"))) {
                    fluxList++;
                }
                if (i != null && (StringUtils.isNotBlank(i.getIsFlow()) && i.getIsFlow().equals("0")) || (StringUtils.isNotBlank(i.getIsFloatIn()) && i.getIsFloatIn().equals("0"))) {
                    unfluxList++;
                }

            }

            map.put("man", manList);
            map.put("woMan", woManList);
            unKnownSex = nullList + unKnowList;
            map.put("unKnownSex", unKnownSex);

            map.put("lost", lostList);
            map.put("unLost", unLostList);

            map.put("poverty", povertyList);
            map.put("unPoverty", unPovertyList);

            map.put("partyWork", partyWorkList);
            map.put("unPartyWork", unPartyWorkList);

            map.put("flux", fluxList);
            map.put("unFlux", unfluxList);
            map.put("total", partyMemberList.size());

            // 党龄统计
            LocalDate now = LocalDate.now();//获取当前日期
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Long> partyAgeList = new ArrayList<>();

            List<Long> ageList = new ArrayList<>();

            // 文化程度统计
            List<Integer> educationList = new ArrayList<>();
            Integer boshi = 0;
            Integer suoshi = 0;
            Integer yanjiusheng = 0;
            Integer zhongyangdangxiao = 0;
            Integer shengweidangxiao = 0;
            Integer daxue = 0;
            Integer dazhuan = 0;
            Integer daxueputongban = 0;
            Integer dierxueshi = 0;
            Integer zhongyangdangxiaodaxue = 0;
            Integer shengweidangxiaodaxue = 0;
            Integer zhongyangdangxiaodzhuan = 0;
            Integer shengweidangxiaodzhuan = 0;
            Integer zhongzhuan = 0;
            Integer zhongji = 0;
            Integer jigongh = 0;
            Integer pugao = 0;
            Integer chuzhong = 0;
            Integer xiaoxue = 0;
            Integer qita = 0;

            // 所在党支部职务统计
            List<Map<String, Object>> positionList = new ArrayList<>();
            Integer qitaweiyuan = 0;
            Integer wu = 0;
            Integer xuanchuanweiyuan = 0;
            Integer jijianweiyuan = 0;
            Integer zuzhitaweiyuan = 0;
            Integer fushuju = 0;
            Integer shuji = 0;

            for (JcxfPartyMember partyMember : partyMemberList) {
                if (partyMember != null && partyMember.getPartyTime() != null) {
                    //Long betweenYear = DateUtil.betweenYear(partyMember.getPartyTime(), now, true);
                    LocalDate startDate = LocalDate.parse(simpleDateFormat.format(partyMember.getPartyTime()), formatter);
                    Long year = (Long) startDate.until(now, ChronoUnit.YEARS);
                    if (year != null) {
                        partyAgeList.add(year);
                    }
                }

                if (partyMember != null && partyMember.getPartyTime() != null) {
                    LocalDate startDate = LocalDate.parse(simpleDateFormat.format(partyMember.getBirthday()), formatter);
                    Long year = (Long) startDate.until(now, ChronoUnit.YEARS);
                    if (year != null) {
                        ageList.add(year);
                    }
                }

                if (partyMember != null && partyMember.getEducation() != null) {
                    String education = partyMember.getEducation();
                    if (education.trim().equals("11")) {
                        boshi = boshi + 1;
                    } else if (education.trim().equals("12")) {
                        suoshi = suoshi + 1;
                    } else if (education.trim().equals("13")) {
                        yanjiusheng = yanjiusheng + 1;
                    } else if (education.trim().equals("1A")) {
                        zhongyangdangxiao = zhongyangdangxiao + 1;
                    } else if (education.trim().equals("1B")) {
                        shengweidangxiao = shengweidangxiao + 1;
                    } else if (education.trim().equals("21")) {
                        daxue = daxue + 1;
                    } else if (education.trim().equals("22")) {
                        dazhuan = dazhuan + 1;
                    } else if (education.trim().equals("23")) {
                        daxueputongban = daxueputongban + 1;
                    } else if (education.trim().equals("24")) {
                        dierxueshi = dierxueshi + 1;
                    } else if (education.trim().equals("2A")) {
                        zhongyangdangxiaodaxue = zhongyangdangxiaodaxue + 1;
                    } else if (education.trim().equals("2B")) {
                        shengweidangxiaodaxue = shengweidangxiaodaxue + 1;
                    } else if (education.trim().equals("2C")) {
                        zhongyangdangxiaodzhuan = zhongyangdangxiaodzhuan + 1;
                    } else if (education.trim().equals("2D")) {
                        shengweidangxiaodzhuan = shengweidangxiaodzhuan + 1;
                    } else if (education.trim().equals("41")) {
                        zhongzhuan = zhongzhuan + 1;
                    } else if (education.trim().equals("44")) {
                        zhongji = zhongji + 1;
                    } else if (education.trim().equals("47")) {
                        jigongh = jigongh + 1;
                    } else if (education.trim().equals("6")) {
                        pugao = pugao + 1;
                    } else if (education.trim().equals("7")) {
                        chuzhong = chuzhong + 1;
                    } else if (education.trim().equals("8")) {
                        xiaoxue = xiaoxue + 1;
                    } else if (education.trim().equals("9")) {
                        qita = qita + 1;
                    }
                }

                if (partyMember != null && partyMember.getPosition() != null) {
                    Integer position = partyMember.getPosition();
                    if (position.intValue() == 20) {
                        qitaweiyuan = qitaweiyuan + 1;
                    } else if (position.intValue() == 99 || position.intValue() == 7) {
                        wu = wu + 1;
                    } else if (position.intValue() == 5) {
                        xuanchuanweiyuan = xuanchuanweiyuan + 1;
                    } else if (position.intValue() == 4) {
                        jijianweiyuan = jijianweiyuan + 1;
                    } else if (position.intValue() == 3) {
                        zuzhitaweiyuan = zuzhitaweiyuan + 1;
                    } else if (position.intValue() == 2) {
                        fushuju = fushuju + 1;
                    } else if (position.intValue() == 1) {
                        shuji = shuji + 1;
                    }
                }
            }

            Integer ten = 0;
            Integer tenToTwenty = 0;
            Integer twentyOneToThirty = 0;
            Integer thirtyOneToForty = 0;
            Integer fortyOneToFifty = 0;
            Integer fiftyAfter = 0;
            if (partyAgeList != null && partyAgeList.size() > 0) {
                for (Long integer : partyAgeList) {
                    if (integer <= 10) {
                        ten = ten + 1;
                    } else if (integer > 10 && integer <= 20) {
                        tenToTwenty = tenToTwenty + 1;
                    } else if (integer > 20 && integer <= 30) {
                        twentyOneToThirty = twentyOneToThirty + 1;
                    } else if (integer > 30 && integer <= 40) {
                        thirtyOneToForty = thirtyOneToForty + 1;
                    } else if (integer > 40 && integer <= 50) {
                        fortyOneToFifty = fortyOneToFifty + 1;
                    } else if (integer > 50) {
                        fiftyAfter = fiftyAfter + 1;
                    }
                }
            }

            List<Integer> partyAges = new ArrayList<>();
            partyAges.add(ten);
            partyAges.add(tenToTwenty);
            partyAges.add(twentyOneToThirty);
            partyAges.add(thirtyOneToForty);
            partyAges.add(fortyOneToFifty);
            partyAges.add(fiftyAfter);
            map.put("partyAges", partyAges);

            // 年龄统计

            Integer beforeThirtyFive = 0;
            Integer thirtySixToForty = 0;
            Integer fortyOneToFortyFIve = 0;
            Integer fortySixToFifty = 0;
            Integer fiftyOneToFiftyFour = 0;
            Integer afterFiftyFive = 0;
            if (ageList != null && ageList.size() > 0) {
                for (Long aLong : ageList) {
                    if (aLong <= 35) {
                        beforeThirtyFive = beforeThirtyFive + 1;
                    } else if (aLong > 35 && aLong <= 40) {
                        thirtySixToForty = thirtySixToForty + 1;
                    } else if (aLong > 40 && aLong <= 45) {
                        fortyOneToFortyFIve = fortyOneToFortyFIve + 1;
                    } else if (aLong > 45 && aLong <= 50) {
                        fortySixToFifty = fortySixToFifty + 1;
                    } else if (aLong > 50 && aLong <= 55) {
                        fiftyOneToFiftyFour = fiftyOneToFiftyFour + 1;
                    } else if (aLong > 55) {
                        afterFiftyFive = afterFiftyFive + 1;
                    }
                }
            }
            List<Integer> ages = new ArrayList<>();
            ages.add(beforeThirtyFive);
            ages.add(thirtySixToForty);
            ages.add(fortyOneToFortyFIve);
            ages.add(fortySixToFifty);
            ages.add(fiftyOneToFiftyFour);
            ages.add(afterFiftyFive);
            map.put("ages", ages);


            educationList.add(boshi);
            educationList.add(suoshi);
            educationList.add(yanjiusheng);
            educationList.add(zhongyangdangxiao);
            educationList.add(shengweidangxiao);
            educationList.add(daxue);
            educationList.add(dazhuan);
            educationList.add(daxueputongban);
            educationList.add(dierxueshi);
            educationList.add(zhongyangdangxiaodaxue);
            educationList.add(shengweidangxiaodaxue);
            educationList.add(zhongyangdangxiaodzhuan);
            educationList.add(shengweidangxiaodzhuan);
            educationList.add(zhongzhuan);
            educationList.add(zhongji);
            educationList.add(jigongh);
            educationList.add(pugao);
            educationList.add(chuzhong);
            educationList.add(xiaoxue);
            educationList.add(qita);
            map.put("education", educationList);


            HashMap<String, Object> hashMap1 = new HashMap();
            hashMap1.put("name", "其他");
            hashMap1.put("value", qita);
            positionList.add(hashMap1);
            HashMap<String, Object> hashMap2 = new HashMap();
            hashMap2.put("name", "无");
            hashMap2.put("value", wu);
            positionList.add(hashMap2);
            HashMap<String, Object> hashMap3 = new HashMap();
            hashMap3.put("name", "宣传委员");
            hashMap3.put("value", xuanchuanweiyuan);
            positionList.add(hashMap3);
            HashMap<String, Object> hashMap4 = new HashMap();
            hashMap4.put("name", "纪检委员 ");
            hashMap4.put("value", jijianweiyuan);
            positionList.add(hashMap4);
            HashMap<String, Object> hashMap5 = new HashMap();
            hashMap5.put("name", "组织委员");
            hashMap5.put("value", zuzhitaweiyuan);
            positionList.add(hashMap5);
            HashMap<String, Object> hashMap6 = new HashMap();
            hashMap6.put("name", "副书记");
            hashMap6.put("value", fushuju);
            positionList.add(hashMap6);
            HashMap<String, Object> hashMap7 = new HashMap();
            hashMap7.put("name", "书记");
            hashMap7.put("value", shuji);
            positionList.add(hashMap7);
            //positionList.put("无",wu);
            //positionList.add(xuanchuanweiyuan);
            //positionList.add(jijianweiyuan);
            //positionList.add(zuzhitaweiyuan);
            //positionList.add(fushuju);
            //positionList.add(shuji);
            map.put("position", positionList);

            return map;
        } else {
            map.put("man", 0);
            map.put("woMan", 0);
            map.put("unKnownSex", 0);
            map.put("lost", 0);
            map.put("unLost", 0);
            map.put("poverty", 0);
            map.put("unPoverty", 0);
            map.put("partyWork", 0);
            map.put("unPartyWork", 0);
            map.put("flux", 0);
            map.put("unFlux", 0);
            map.put("total", 0);

            Integer[] partyAges = {0, 0, 0, 0, 0, 0};
            map.put("partyAges", partyAges);

            Integer[] ages = {0, 0, 0, 0, 0, 0};
            map.put("ages", ages);

            Integer[] education = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            map.put("education", education);

            //Integer[] positionList = {0, 0, 0, 0, 0, 0, 0};
            List<Map<String, Object>> positionList = new ArrayList<>();
            HashMap<String, Object> hashMap1 = new HashMap();
            hashMap1.put("name", "其他");
            hashMap1.put("value", 0);
            positionList.add(hashMap1);
            HashMap<String, Object> hashMap2 = new HashMap();
            hashMap2.put("name", "无");
            hashMap2.put("value", 0);
            positionList.add(hashMap2);
            HashMap<String, Object> hashMap3 = new HashMap();
            hashMap3.put("name", "宣传委员");
            hashMap3.put("value", 0);
            positionList.add(hashMap3);
            HashMap<String, Object> hashMap4 = new HashMap();
            hashMap4.put("name", "纪检委员 ");
            hashMap4.put("value", 0);
            positionList.add(hashMap4);
            HashMap<String, Object> hashMap5 = new HashMap();
            hashMap5.put("name", "组织委员");
            hashMap5.put("value", 0);
            positionList.add(hashMap5);
            HashMap<String, Object> hashMap6 = new HashMap();
            hashMap6.put("name", "副书记");
            hashMap6.put("value", 0);
            positionList.add(hashMap6);
            HashMap<String, Object> hashMap7 = new HashMap();
            hashMap7.put("name", "书记");
            hashMap7.put("value", 0);
            positionList.add(hashMap1);

            map.put("position", positionList);
            map.put("office", 0);
            map.put("pre", 0);
            return map;
        }
    }


    /**
     * 统计性别、是否失联、贫困、流动党员数据
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> getStatisticsInfo(Map<String, Object> requestMap) {
        String deptId = (String) requestMap.get("deptId");
        try {
            if (StringUtils.isEmpty(deptId)) {
                deptId = "1";
            }

            List<Long> listAll = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(deptId));
            listAll.add(Long.valueOf(deptId));
            if (listAll != null && listAll.size() > 0) {
                Map<String, Object> map = getInfo(listAll);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                List<Long> tmp = new ArrayList<>();
                Map<String, Object> map = getInfo(tmp);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    /**
     * 民族分布
     *
     * @param requestMap
     * @return
     */
    @Override
    public Map<String, Object> nationDistribution(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (StringUtils.isEmpty(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "部门id为空，请联系管理员");
            }

            List<Long> listAll = jcxfSysDeptMapper.selectChildrenIdsById(Long.valueOf(id));
            listAll.add(Long.valueOf(id));

            List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.getInfo(listAll);
            Map<String, Object> map = new HashMap<>();
            if (partyMemberList == null || partyMemberList.size() == 0) {
                map.put("han", 0);
                map.put("menggu", 0);
                map.put("huizu", 0);
                map.put("zangzu", 0);
                map.put("miaozu", 0);
                map.put("yizu", 0);
                map.put("zhuangzu", 0);
                map.put("baizu", 0);
                map.put("manzu", 0);
                map.put("daizu", 0);
                map.put("qita", 0);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            } else {
                Integer han = 0;
                Integer menggu = 0;
                Integer huizu = 0;
                Integer zangzu = 0;
                Integer miaozu = 0;
                Integer yizu = 0;
                Integer zhuangzu = 0;
                Integer baizu = 0;
                Integer manzu = 0;
                Integer daizu = 0;
                Integer qita = 0;
                for (JcxfPartyMember partyMember : partyMemberList) {
                    if (partyMember.getNational().equals("1")) {
                        han = ++han;
                    } else if (partyMember.getNational().equals("2")) {
                        menggu = ++menggu;
                    } else if (partyMember.getNational().equals("3")) {
                        huizu = ++huizu;
                    } else if (partyMember.getNational().equals("4")) {
                        zangzu = ++zangzu;
                    } else if (partyMember.getNational().equals("6")) {
                        miaozu = ++miaozu;
                    } else if (partyMember.getNational().equals("7")) {
                        yizu = ++yizu;
                    } else if (partyMember.getNational().equals("8")) {
                        zhuangzu = ++zhuangzu;
                    } else if (partyMember.getNational().equals("14")) {
                        baizu = ++baizu;
                    } else if (partyMember.getNational().equals("11")) {
                        manzu = ++manzu;
                    } else if (partyMember.getNational().equals("18")) {
                        daizu = ++daizu;
                    } else if (partyMember.getNational().equals("91")) {
                        qita = ++qita;
                    }

                }
                map.put("han", han);
                map.put("menggu", menggu);
                map.put("huizu", huizu);
                map.put("zangzu", zangzu);
                map.put("miaozu", miaozu);
                map.put("yizu", yizu);
                map.put("zhuangzu", zhuangzu);
                map.put("baizu", baizu);
                map.put("manzu", manzu);
                map.put("daizu", daizu);
                map.put("qita", qita);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> educationDistribution(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("deptId");
            Long deptId = Long.valueOf(id);
            if (deptId == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "部门id为空，请联系管理员");
            }
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(deptId);
            deptIds.add(deptId);
            QueryWrapper<JcxfPartyMember> q = new QueryWrapper<>();
            q.eq("party_member.education", 21).eq("party_member.del_flag", 0).in("party_member.dept_id", deptIds);
            Integer benke = jcxfPartyMemberMapper.selectCount(q);
            QueryWrapper<JcxfPartyMember> q2 = new QueryWrapper<>();
            q2.eq("party_member.education", 12).eq("party_member.del_flag", 0).in("party_member.dept_id", deptIds);
            Integer suoshi = jcxfPartyMemberMapper.selectCount(q2);
            QueryWrapper<JcxfPartyMember> q3 = new QueryWrapper<>();
            q3.eq("party_member.education", 11).eq("party_member.del_flag", 0).in("party_member.dept_id", deptIds);
            Integer boshi = jcxfPartyMemberMapper.selectCount(q3);

            QueryWrapper<JcxfPartyMember> q4 = new QueryWrapper<>();
            q4.notIn("party_member.education", 21, 12, 11).eq("party_member.del_flag", 0).in("party_member.dept_id", deptIds);
            Integer qita = jcxfPartyMemberMapper.selectCount(q4);

            Map<String, Integer> map = new HashMap<>();
            map.put("大学本科", benke);
            map.put("硕士研究生", suoshi);
            map.put("博士研究生", boshi);
            map.put("其他", qita);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> validNewPhone(Map<String, Object> requestMap) {
        String phone = (String) requestMap.get("phone");
        if (StringUtils.isEmpty(phone)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "号码为空");
        }
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("party_member.phone", phone);
        Integer selectCount = jcxfPartyMemberMapper.selectCount(queryWrapper);
        if (selectCount > 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该号码已被绑定");
        }
        return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
    }

    @Override
    public Map<String, Object> changePhone(Map<String, Object> requestMap) {
        try {
            String phone = (String) requestMap.get("phone");
            String id = (String) requestMap.get("id");
            if (StringUtils.isEmpty(phone)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "手机号不能为空");
            }
            QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member.phone", phone);
            queryWrapper.ne("id", id);
            queryWrapper.eq("del_flag", false);
            int selectCount = jcxfPartyMemberMapper.selectCount(queryWrapper);
            if (selectCount > 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "该号码已被使用");
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", id);
            updateWrapper.set("phone", phone);
            int count = jcxfPartyMemberMapper.update(null, updateWrapper);
            if (count > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改手机号失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改手机号异常");
        }
    }

    @Override
    public Map<String, Object> changeNewPhone(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String ipCode = (String) requestMap.get("code");
            String newPhone = (String) requestMap.get("newPhone");

            String redisCode = (String) redisUtils.get(newPhone);
            if (!redisCode.equals(ipCode)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码错误");
            }
            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectById(id);
            if (partyMember == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "人员id有误，请联系管理员");
            }
            partyMember.setPhone(newPhone);
            int i = jcxfPartyMemberMapper.updateById(partyMember);
            if (i == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "修改失败");
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("修改成功"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberCounts(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(deptId);

            Integer searchDeptId = null;
            List<Integer> allIds = new ArrayList<>();
            List<Integer> veteranIds = new ArrayList<>();

            if (tzSysDept.getParentId() == 0) {
                searchDeptId = tzSysDept.getId();
            } else {
                searchDeptId = Integer.parseInt(tzSysDept.getParentIds().split(",")[1]);
            }

            /*List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);*/

            allIds = jcxfSysDeptMapper.getChildrenIdsByVeteran(searchDeptId, "", null);
            veteranIds = jcxfSysDeptMapper.getChildrenIdsByVeteran(searchDeptId, "1", null);

            allIds.add(searchDeptId);

            // 查询党员总数
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.in("dept_id", allIds);
            queryWrapper.eq("del_flag", 0);
            int num = jcxfPartyMemberMapper.selectCount(queryWrapper);

            // 查询退休党员数量
            QueryWrapper q2 = new QueryWrapper();
            q2.in("dept_id", veteranIds);
            q2.eq("del_flag", 0);
            int num2 = jcxfPartyMemberMapper.selectCount(q2);

            Map<String, Integer> map = new HashMap<>();
            map.put("all", num);
            map.put("retire", num2);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberFlowCount(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(deptId);

            Long searchDeptId = null;
            // 记录退休支部的id
            List<Integer> veteranDeptIds = new ArrayList<>();

            List<Long> ids = new ArrayList<>();
            if (tzSysDept.getParentId() == 0) {
                searchDeptId = Long.valueOf(tzSysDept.getId());
            } else {
                searchDeptId = Long.valueOf(tzSysDept.getParentIds().split(",")[1]);
            }
            ids = jcxfSysDeptMapper.selectChildrenIdsById(searchDeptId);
            ids.add(Long.valueOf(tzSysDept.getId()));

            // 流出党员总数
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("del_flag", 0);
            queryWrapper.eq("is_flow", "1"); // 是否流出党员(0:否,1:是)
            queryWrapper.in("dept_id", ids);
            int num1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

            // 流入党员总数
            /*QueryWrapper queryWrapper2 = new QueryWrapper();
            queryWrapper2.eq("del_flag", 0);
            queryWrapper2.eq("is_float_in", "1");
            queryWrapper2.in("dept_id", ids);
            int num2 = jcxfPartyMemberMapper.selectCount(queryWrapper2);*/

            Map<String, Integer> map = new HashMap<>();
            map.put("flow", num1);
            //map.put("flowIn", num2);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberCountByDeptIdsAndVeteran(Map<String, Object> requestMap) {
        try {
            String veteran = (String) requestMap.get("veteran");
            String deptId = (String) requestMap.get("deptId");

            String searchDeptId = null;

            JcxfSysDept thisTzSysDept = jcxfSysDeptMapper.selectById(deptId);
            if (thisTzSysDept.getParentId() == 0) {
                searchDeptId = deptId;
            } else {
                searchDeptId = thisTzSysDept.getParentIds().split(",")[1];
            }

            /*List<Integer> typeList = new ArrayList<>();
            typeList.add(631);
            typeList.add(632);
            typeList.add(931);
            typeList.add(932);*/

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.select("id, name, veteran");
            queryWrapper.eq("parent_id", searchDeptId);
            queryWrapper.eq("del_flag", false);
            // queryWrapper.ne("id", 21116); // 酒城先锋运维党委
            List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(queryWrapper);

            if (deptList == null || deptList.size() == 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new JSONArray()));
            }

            CountDownLatch latch = new CountDownLatch(deptList.size());
            ExecutorService executorService = Executors.newFixedThreadPool(4);

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < deptList.size(); i++) {
                GetMemberCount count = new GetMemberCount(deptList.get(i).getId(), deptList.get(i).getVeteran(), veteran, null, i, deptList.get(i).getName(), jsonArray, latch);
                executorService.submit(count);
            }

            //写入部门数据
            executorService.shutdown();//for循环结束后停止ExecutorService
            latch.await();

            jsonArray.sort(Comparator.comparing(obj -> ((JSONObject) obj).getInteger("index")));

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jsonArray));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    class GetMemberCount implements Runnable {
        private Integer id;

        private String thisDeptVeteran;
        private String veteran;

        private List<Integer> typeList;

        private int i;

        private String name;

        private JSONArray jsonArray;

        private CountDownLatch latch;

        GetMemberCount(Integer id, String thisDeptVeteran, String veteran, List<Integer> typeList, int i, String name, JSONArray jsonArray, CountDownLatch latch) {
            this.id = id;
            this.thisDeptVeteran = thisDeptVeteran;
            this.veteran = veteran;
            this.typeList = typeList;
            this.i = i;
            this.name = name;
            this.jsonArray = jsonArray;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("index", i + 1);
                jsonObject.put("name", name);

                List<Integer> ids = jcxfSysDeptMapper.getChildrenIdsByVeteran(id, veteran, typeList);
                if (StringUtils.isBlank(this.veteran) || this.thisDeptVeteran.equals(this.veteran)) {
                    ids.add(id);
                }
                if (ids.size() > 0) {
                    QueryWrapper queryWrapper1 = new QueryWrapper();
                    queryWrapper1.in("dept_id", ids);
                    queryWrapper1.eq("del_flag", 0);
                    jsonObject.put("dycount", jcxfPartyMemberMapper.selectCount(queryWrapper1));
                } else {
                    jsonObject.put("dycount", 0);
                }

                jsonArray.add(jsonObject);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
                latch.countDown();
            }

        }
    }

    @Override
    public Map<String, Object> getPartyMemberCountFlowAndInFlow(Map<String, Object> requestMap) {
        try {
            String deptId = (String) requestMap.get("deptId");
            JcxfSysDept thisTzSysDept = jcxfSysDeptMapper.selectById(deptId);

            String searchDeptId = null;
            if (thisTzSysDept.getParentId() == 0) {
                searchDeptId = deptId;
            } else {
                searchDeptId = thisTzSysDept.getParentIds().split(",")[1];
            }

            QueryWrapper q1 = new QueryWrapper();
            q1.select("id,name");
            q1.eq("parent_id", searchDeptId);
            q1.eq("del_flag", 0);
            List<JcxfSysDept> deptList = jcxfSysDeptMapper.selectList(q1);

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < deptList.size(); i++) {
                //qxAndOtherDeptIds.add(deptList.get(i).getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("index", i + 1);
                jsonObject.put("id", deptList.get(i).getId());
                jsonObject.put("name", deptList.get(i).getName());

                jsonArray.add(jsonObject);
            }


            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById((Long) jsonObject.get("id"));
                deptIds.add((Long) jsonObject.get("id"));

                // 流出党员总数
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("del_flag", 0);
                queryWrapper.eq("is_flow", "1"); // 是否流出党员(0:否,1:是)
                queryWrapper.in("dept_id", deptIds);
                int num1 = jcxfPartyMemberMapper.selectCount(queryWrapper);

                jsonObject.put("index", i + 1);
                jsonObject.put("flow", num1);
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jsonArray));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> getPartyMemberCountDetail(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");

            QueryWrapper q1 = new QueryWrapper();
            q1.eq("user_id", id);
            int studyCount = jcxfStudyUserMapper.selectCount(q1);

            /*QueryWrapper q2 = new QueryWrapper();
            q2.like("joins", "," + id + ",");
            q2.or();
            q2.like("joins", "," + id);
            q2.or();
            q2.like("joins", id + ",");
            int meetingCount = tzPersonMeetingMapper.selectCount(q2);*/

            QueryWrapper q2 = new QueryWrapper();
            q2.eq("user_id", id);
            q2.eq("del_flag", false);
            q2.eq("user_status", Constant.MEETING_USER_STATUS_6);
            int meetingCount = jcxfPartyMeetingUserMapper.selectCount(q2);

            QueryWrapper<JcxfOrganizationLife> q22 = new QueryWrapper();
            q22.eq("del_flag", false);
            q22.eq("host", id);
//            q22.and(i -> i.eq("host", id).or(i2 -> i2.eq("recorder", id)));
            meetingCount += jcxfOrganizationLifeMapper.selectCount(q22);
            q22 = new QueryWrapper();
            q22.eq("del_flag", false);
            q22.eq("recorder", id);
            meetingCount += jcxfOrganizationLifeMapper.selectCount(q22);


            QueryWrapper q3 = new QueryWrapper();
            q3.select("sum(actually_pay) as actually_pay");
            q3.eq("del_flag", 0);
            q3.eq("party_member_id", id);
            q3.eq("status", "1"); // 缴纳状态（0未缴纳，1已缴纳）
            TzPayFeeDetail payFeeDetail = tzPayFeeDetailMapper.selectOne(q3);
            BigDecimal actuallyPay = new BigDecimal("0.00");
            if (payFeeDetail != null) {
                actuallyPay = payFeeDetail.getActuallyPay();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("studyCount", studyCount);
            map.put("meetingCount", meetingCount);
            map.put("actuallyPay", actuallyPay);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> setRole(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String role = (String) requestMap.get("role");
            QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member.id", id).eq("party_member.del_flag", 0);
            JcxfPartyMember partyMember = jcxfPartyMemberMapper.selectOne(queryWrapper);
            partyMember.setButtonRole(role);
            int i = jcxfPartyMemberMapper.updateById(partyMember);
            if (i > 0) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> queryOneselfFlowPartyMemberList(Map<String, Object> requestMap) {
        try {
            JcxfPartyMember partyMember = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), JcxfPartyMember.class);
            QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper<>();
            partyMember.setDelFlag(false);
            partyMember.setIsFlow("1");
            partyMember.setIsFloatIn("1");
            Long deptId = partyMember.getDeptId();
            queryWrapper.and(i -> i.eq("party_member.dept_id", deptId));
            queryWrapper.and(i -> i.eq("party_member.is_flow", partyMember.getIsFlow()));
            queryWrapper.and(i -> i.eq("party_member.is_float_in", partyMember.getIsFloatIn()));
            queryWrapper.and(i -> i.eq("party_member.del_flag", partyMember.getDelFlag()));
            List<JcxfPartyMember> partyMembers = jcxfPartyMemberMapper.selectList(queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(partyMembers));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    @Override
    public Map<String, Object> getPartyMemberNoEncrypt(Map<String, Object> requestMap) {
        List<JcxfPartyMember> partyMemberNoEncrypt = jcxfPartyMemberMapper.getPartyMemberNoEncrypt();
        for (JcxfPartyMember jcxfPartyMember : partyMemberNoEncrypt) {
            jcxfPartyMemberMapper.updateById(jcxfPartyMember);
        }
        return null;
    }

    @Override
    public Map<String, Object> updatePartyMemberPassWord(Map<String, Object> requestMap) {
        List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.getPartyMemberInfo();
        for (JcxfPartyMember jcxfPartyMember : partyMemberList) {
            JcxfPartyMember p = new JcxfPartyMember();
            p.setId(jcxfPartyMember.getId());
            if (StringUtils.isNotBlank(jcxfPartyMember.getIdcard())) {
                String substring = "";
                String idcard = jcxfPartyMember.getIdcard();
                if (idcard != null && idcard != "") {
                    substring = idcard.substring(idcard.length() - 6);
                }
                String result = passwordManager.encryptPassword(substring);
                p.setPassword(result);
                jcxfPartyMemberMapper.updateById(p);
            }
        }
        return null;
    }

    @Override
    public Map<String, Object> getPartyMemberManagepower(Map<String, Object> requestMap) {
        String id = (String) requestMap.get("partyMemberId");
        JcxfPartyMemberVo jcxfPartyMember = jcxfPartyMemberVoMapper.selectById(id);
        if (jcxfPartyMember != null) {
            QueryWrapper<JcxfSysDept> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("manage_phone", AESUtil2.decrypt(jcxfPartyMember.getPhone()));
            JcxfSysDept jcxfSysDept = jcxfSysDeptMapper.selectOne(queryWrapper);
            if (jcxfSysDept != null) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(jcxfSysDept));
            } else {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
            }
        } else {
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        }
    }
}

package com.yami.shop.multishop.dj.controller.app;


import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yami.shop.bean.model.AttachFile;
import com.yami.shop.bean.model.dj.*;
import com.yami.shop.bean.vo.dj.PageVo;
import com.yami.shop.bean.vo.dj.SearchVo;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PrincipalUtil;
import com.yami.shop.dj.service.*;
import com.yami.shop.multishop.uitls.TokenKey;
import com.yami.shop.security.common.bo.UserInfoInTokenBO;
import com.yami.shop.security.common.enums.SysTypeEnum;
import com.yami.shop.security.common.manager.PasswordManager;
import com.yami.shop.security.common.manager.TokenStore;
import com.yami.shop.security.multishop.model.YamiShopUser;

import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.AttachFileService;
import com.yami.shop.service.ShopDetailService;
import com.yami.shop.sys.common.service.ShopEmployeeService;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 具体投票项 前端控制器
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/app/dept")
@Scope("prototype")
public class appController {
    /**
     * 用于aes签名的key，16位
     */
    @Value("${auth.token.signKey:-mall4j--mall4j-}")
    public String tokenSignKey;
    @Autowired
    private ITzOrganizationLifeService tzOrganizationLifeService;
    @Autowired
    private ITzSysDeptService tzSysDeptService;
    @Autowired
    private IPartyMemberService partyMemberService;
    @Autowired
    private IDevelopPartyService developPartyService;

    @Autowired
    private ITzTermDeptService tzTermDeptService;

    @Autowired
    private AttachFileService attachFileService;
    @Autowired
    private ITzActiveDeptService activeDeptService;


    @Autowired
    private ITzMeetingRegisterService iTzMeetingRegisterService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ITzPayFeeService tzPayFeeService;
    @Autowired
    private ITzPayFeeDetailService iTzPayFeeDetailService;

    @Autowired
    private TokenStore tokenStore;

    private static java.lang.String globalUserId;

    //设置全局变量globalUserId
    @PostConstruct
    public void init() {
        // 获取HttpServletRequest对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader(TokenKey.HEADER);
        java.lang.String token = tokenStore.decryptToken(authorization);
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
              .get(TokenKey.TOKEN_KEY + token);
        globalUserId = userInfoInTokenBO.getUserId();
    }
    /**
     * 根据用户id获取所属部门id
     * @param id
     * @return
     */
    @ApiOperation("根据用户id获取所属部门id")
    @GetMapping("getDeptIdByUserId")
    public ServerResponseEntity<Object> getDeptIdByUserId( String id) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if (partyMember == null || partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        QueryWrapper<TzSysDept> deptQueryWrapper = new QueryWrapper<>();
        deptQueryWrapper.eq("tz_sys_dept.id" ,partyMember.getDeptId().toString()).eq("tz_sys_dept.del_flag", 0);
        TzSysDept dept = tzSysDeptService.getOne(deptQueryWrapper);
        if (dept == null || StringUtils.isBlank(dept.getName())) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        Map<String,String> map = new HashMap<>();
        map.put("deptId",partyMember.getDeptId().toString());
        map.put("deptName",dept.getName());
        map.put("realName",partyMember.getRealName());
        return ServerResponseEntity.success(map);
    }
    private int getExpiresIn(int sysType) {
        // 3600秒(1小时)
        int expiresIn = 3600;

        // 普通用户token过期时间 1小时 * 24 * 30 = 30天
        if (Objects.equals(sysType, SysTypeEnum.ORDINARY.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        // 系统管理员的token过期时间 1小时 * 24 * 30 = 30天
        if (Objects.equals(sysType, SysTypeEnum.MULTISHOP.value()) || Objects.equals(sysType, SysTypeEnum.PLATFORM.value()) || Objects.equals(sysType, SysTypeEnum.STATION.value())) {
            expiresIn = expiresIn * 24 * 30;
        }
        return expiresIn;
    }
    private String decryptToken(String data) {
        AES aes = new AES(tokenSignKey.getBytes(StandardCharsets.UTF_8));
        String decryptStr;
        String decryptToken;
        try {
            decryptStr = aes.decryptStr(data);
            decryptToken = decryptStr.substring(0,32);
            // 创建token的时间，token使用时效性，防止攻击者通过一堆的尝试找到aes的密码，虽然aes是目前几乎最好的加密算法
            long createTokenTime = Long.parseLong(decryptStr.substring(32,45));
            // 系统类型
            int sysType = Integer.parseInt(decryptStr.substring(45));
            // token的过期时间
            int expiresIn = getExpiresIn(sysType);
            long second = 1000L;
            if (System.currentTimeMillis() - createTokenTime > expiresIn * second) {
                throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"token error");
            }
        }
        catch (Exception e) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"token error");
        }

        // 防止解密后的token是脚本，从而对redis进行攻击，uuid只能是数字和小写字母
        if (!PrincipalUtil.isSimpleChar(decryptToken)) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED,"token error");
        }
        return decryptToken;
    }
    /**
     * 根据党员id获取组织概况信息
     * @param id
     * @return
     */
    @ApiOperation("根据党员id获取组织概况信息")
    @GetMapping("getDeptByUserId")
    public ServerResponseEntity<Object> getDeptByUserId( String id,HttpServletRequest request) {
        /*SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();*/
        YamiShopUser shopUser = SecurityUtils.getShopUser();
        String authorization = request.getHeader("authorization");
        //String s = passwordManager.decryptPassword(authorization);


        String token = decryptToken(authorization);
        UserInfoInTokenBO userInfoInTokenBO = (UserInfoInTokenBO) redisTemplate.opsForValue()
              .get("mall4j_oauth:token:access:"+token);

        String userId = shopUser.getUserId();
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id",id).eq("party_member.del_flag",0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if(partyMember==null||partyMember.getDeptId()==null){
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        Long deptId = partyMember.getDeptId();
        Map<String, Object> map = new HashMap<>();

        try {
            // 查询支部简介
            TzSysDept sysDept = tzSysDeptService.getById(deptId);
            String deptIntroduction = sysDept.getDeptIntroduction();
            map.put("deptIntroduction", deptIntroduction);
            // 查询阵地图片
            QueryWrapper<AttachFile> wrapper = new QueryWrapper<>();
            wrapper.eq("tz_attach_file.foreign_key", deptId);
            wrapper.eq("tz_attach_file.table_type", "tz_sys_dept");
            List<AttachFile> attachFiles = attachFileService.list(wrapper);

            map.put("deptAttachFiles", attachFiles);

            // 本部所有党员信息查询
            QueryWrapper<PartyMember> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("party_member.dept_id", deptId);
            queryWrapper.eq("party_member.del_flag", 0);
            List<PartyMember> list = partyMemberService.list(queryWrapper);
            if (list != null && list.size() > 0) {
                //党员头像查询
                for (PartyMember partyMember1 : list) {
                    QueryWrapper<AttachFile> attachFileQueryWrapper = new QueryWrapper<>();
                    attachFileQueryWrapper.eq("tz_attach_file.foreign_key", partyMember1.getId());
                    List<AttachFile> attachFileList = attachFileService.list(attachFileQueryWrapper);
                    if (attachFileList != null && attachFileList.size() > 0) {
                        partyMember1.setAttachFiles(attachFileList);
                    }
                }
            }
            map.put("partyInfoList", list);


            //预备党员人数查询
            QueryWrapper<PartyMember> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("party_member.dept_id", deptId);
            queryWrapper2.eq("party_member.del_flag", 0);
            queryWrapper2.eq("party_member.person_type", 0);

            int preCount = partyMemberService.count(queryWrapper2);
            map.put("preCount", preCount);
            // 入党积极分子人数查询
            QueryWrapper<DevelopParty> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("develop_party.dept_id", deptId);
            queryWrapper3.eq("develop_party.del_flag", 0);
            queryWrapper3.eq("develop_party.develop_state", "2");

            int activeCount = developPartyService.count(queryWrapper3);
            map.put("activeCount", activeCount);

            //换届时间查询
            QueryWrapper<TzTermDept> termDeptQueryWrapper = new QueryWrapper<>();
            termDeptQueryWrapper.eq("tz_term_dept.del_flag", 0);
            termDeptQueryWrapper.eq("tz_term_dept.dept_id", deptId);
            termDeptQueryWrapper.and(qw -> {
                qw.or(i2 -> i2.inSql("leader_time", "select max(`leader_time`) from  tz_term_dept where tz_term_dept.dept_id = " + deptId));
            });
            TzTermDept one1 = tzTermDeptService.getOne(termDeptQueryWrapper);
            if (one1 != null) {
                map.put("thisChangeTime", one1.getThisChangeTime());
                map.put("thisFinishTime", one1.getThisFinishTime());
            } else {
                map.put("thisChangeTime", null);
                map.put("thisFinishTime", null);
            }
            // 活动阵地信息查询
            QueryWrapper<TzActiveDept> queryWrapper4 = new QueryWrapper<>();
            queryWrapper4.eq("tz_active_dept.dept_id", deptId);
            queryWrapper4.eq("tz_active_dept.del_flag", 0);

            TzActiveDept one = activeDeptService.getOne(queryWrapper4);
            map.put("activeDept", one);

            //活动阵地照片查询
            if (one != null) {
                QueryWrapper<AttachFile> wrapper5 = null;
                wrapper5 = new QueryWrapper<AttachFile>();
                wrapper5.and(i -> i.eq("tz_attach_file.foreign_key", one.getId()));
                wrapper5.and(i -> i.eq("tz_attach_file.table_type", "tz_active_dept"));
                List<AttachFile> activeAttachFiles = attachFileService.list(wrapper5);
                map.put("activeAttachFiles", activeAttachFiles);
            } else {
                map.put("activeAttachFiles", null);
            }

            //党内表彰查询
            TzSysDept byId = tzSysDeptService.getById(deptId);
            map.put("deptInfo", byId);
            return ServerResponseEntity.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 功能描述：实现分页查询
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("根据部门id查询三会一课列表")
    @GetMapping("queryTzOrganizationLifeList")
    public ServerResponseEntity<Object> queryTzOrganizationLifeList(TzOrganizationLife tzOrganizationLife, SearchVo searchVo, PageVo pageVo,String status) {
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if (partyMember == null ||  partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("部门id为空，查询失败");
        }
        tzOrganizationLife.setDeptId(Integer.parseInt(partyMember.getDeptId().toString()));
        String[] split = status.split(",");

        List<String>list=new ArrayList<>();
        //list.add()
        try {
            IPage<TzOrganizationLife> result =tzOrganizationLifeService.queryTzOrganizationLifeListByapp(tzOrganizationLife, searchVo, pageVo,Arrays.asList(split));
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    @ApiOperation("根据会议id查询会议详细信息")
    @GetMapping("getMeetingDetail")
    public ServerResponseEntity<Object> getMeetingDetail(String meetingId) {
        if(StringUtils.isBlank(meetingId)){
            return ServerResponseEntity.showFailMsg("会议id为空，请联系管理员");
        }
        TzOrganizationLife organizationLife = tzOrganizationLifeService.getById(meetingId);
        QueryWrapper<AttachFile> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("tz_attach_file.foreign_key", meetingId);
        queryWrapper1.eq("tz_attach_file.table_type", "tz_organization_life");
        queryWrapper1.eq("tz_attach_file.type", 1);
        AttachFile attachFile = attachFileService.getOne(queryWrapper1);
        QueryWrapper<TzMeetingRegister> registerQueryWrapper = new QueryWrapper<>();
        registerQueryWrapper.eq("tz_meeting_register.meeting_id",meetingId).eq("tz_meeting_register.type","0");
        int count = iTzMeetingRegisterService.count(registerQueryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("mainImg",attachFile);
        map.put("main",organizationLife);
        map.put("count",count);
        return ServerResponseEntity.success(map);
    }

    /**
     * 根据部门id获取党员列表
     * @param deptId
     * @return
     */
    @ApiOperation("根据部门id获取党员列表")
    @GetMapping("queryPartyListByDeptId")
    public ServerResponseEntity<Object> queryPartyListByDeptId(String deptId) {
        if(StringUtils.isBlank(deptId)){
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        try {
            QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
            partyMemberQueryWrapper.eq("party_member.dept_id", deptId).eq("party_member.del_flag", 0);
            List<PartyMember> list = partyMemberService.list(partyMemberQueryWrapper);
            return ServerResponseEntity.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }
    /**
     * 根据党员id和会议id签到
     * @param
     * @return
     */
    @ApiOperation("根据党员id和会议id签到")
    @GetMapping("registerByUserId")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> registerByUserId( String userId,String meetingId,String type) {
        if(StringUtils.isBlank(meetingId)){
            return ServerResponseEntity.showFailMsg("id为空，请联系管理员");
        }
        if(StringUtils.isBlank(type)){
            return ServerResponseEntity.showFailMsg("签到类型为空，请联系管理员");
        }
        QueryWrapper<PartyMember> partyMemberQueryWrapper = new QueryWrapper<>();
        partyMemberQueryWrapper.eq("party_member.id", globalUserId).eq("party_member.del_flag", 0);
        PartyMember partyMember = partyMemberService.getOne(partyMemberQueryWrapper);
        if (partyMember == null || partyMember.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("查询失败");
        }
        TzOrganizationLife byId = tzOrganizationLifeService.getById(meetingId);
        if (byId == null) {
            return ServerResponseEntity.showFailMsg("会议不存在");
        }
        QueryWrapper<TzMeetingRegister> meetingRegisterQueryWrapper = new QueryWrapper<>();
        meetingRegisterQueryWrapper.eq("tz_meeting_register.user_id",globalUserId);
        meetingRegisterQueryWrapper.eq("tz_meeting_register.meeting_id",meetingId);
        TzMeetingRegister one = iTzMeetingRegisterService.getOne(meetingRegisterQueryWrapper);
        if(one!=null){
            if(one.getType().equals("0")){
            return ServerResponseEntity.showFailMsg("您已签到，不可更改！");}
            else  if(one.getType().equals("1")){
                return ServerResponseEntity.showFailMsg("您已请假，不可更改！");}
        }
        String joins = byId.getJoins();
        if(StringUtils.isBlank(joins)){
            joins = partyMember.getRealName() + ",";
        }else {
            joins = joins + partyMember.getRealName() + ",";
        }
        byId.setJoins(joins);
        QueryWrapper<TzOrganizationLife> queryWrapper = new QueryWrapper<TzOrganizationLife>();
        queryWrapper.eq("tz_organization_life.id", meetingId);
        boolean update = tzOrganizationLifeService.update(byId, queryWrapper);
        if(update){
            TzMeetingRegister tzMeetingRegister= new TzMeetingRegister();
            tzMeetingRegister.setMeetingId(meetingId);
            tzMeetingRegister.setUserId(globalUserId);
            tzMeetingRegister.setType(type);
            boolean save = iTzMeetingRegisterService.save(tzMeetingRegister);
            if(save){
                return ServerResponseEntity.success(save);
            }else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ServerResponseEntity.showFailMsg("签到异常");
            }
        }else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ServerResponseEntity.showFailMsg("签到异常");
        }

    }
    @ApiOperation("新增会议")
    @PostMapping("addMeeting")
    @Transactional(rollbackFor = Exception.class)
    public ServerResponseEntity<Object> addMeeting(@RequestBody TzOrganizationLife organizationLife) {
        if (organizationLife.getDeptId() == null) {
            return ServerResponseEntity.showFailMsg("部门id为空");
        }
        try {
            organizationLife.setDelFlag(0);
            organizationLife.setMeetingStatus("0");
            organizationLife.setPostTime(new Date());
            organizationLife.setCreateTime(new Date());
            boolean res = tzOrganizationLifeService.save(organizationLife);
            if (res) {
                return ServerResponseEntity.success(res);
            } else {
                return ServerResponseEntity.showFailMsg("保存失败");
            }
        } catch (Exception e) {
            return ServerResponseEntity.showFailMsg("保存失败" + e.getMessage());
        }

    }


    /**
     * 功能描述：分页查询支部的缴费记录
     *
     * @param searchVo 需要模糊查询的信息
     * @param pageVo   分页参数
     * @return 返回获取结果
     */
    @ApiOperation("分页查询支部的缴费记录")
    @GetMapping("queryTzPayFeeList")
    public ServerResponseEntity<Object> queryTzPayFeeList(String searchMoth, TzPayFee tzPayFee, SearchVo searchVo, PageVo pageVo) throws ParseException {
        List<Integer> listAll = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (searchMoth != null && searchMoth != "") {
            tzPayFee.setPayMonth(sdf.parse(searchMoth));
            //tzPayFee.setPayMonth(new Date(searchMoth));
        }
        listAll.add(tzPayFee.getDeptId());
        try {
            IPage<TzPayFee> result = tzPayFeeService.queryTzPayFeeListByPage(tzPayFee, searchVo, pageVo, listAll);
            return ServerResponseEntity.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponseEntity.showFailMsg("查询异常:" + e.getMessage());
        }
    }

    /**
     * 根据用户id（即党员id）和deptId查询党员待缴月份和金额
     */
    @ApiOperation("根据用户id（即党员id）和deptId查询党员待缴月份和金额")
    @GetMapping("queryPartyMemberPayDetailList")
    public ServerResponseEntity<Object> queryPartyMemberPayDetailList(TzPayFeeDetail tzPayFeeDetail, PageVo pageVo,SearchVo searchVo){
        if(tzPayFeeDetail.getPartyMemberId()==null){
            return ServerResponseEntity.showFailMsg("党员id为空，请联系管理员");
        }
        IPage<TzPayFeeDetail> result = iTzPayFeeDetailService.queryPartyMemberPayDetailList(tzPayFeeDetail,pageVo,searchVo);
        return ServerResponseEntity.success(result);
    }
    /**
     * 查询我参加的会议
     */
    @ApiOperation("查询我参加的会议")
    @GetMapping("meetingMyJoin")
    public ServerResponseEntity<Object> meetingMyJoin(PageVo pageVo){
        QueryWrapper<TzMeetingRegister> registerQueryWrapper=new QueryWrapper<>();
        registerQueryWrapper.eq("tz_meeting_register.user_id",globalUserId);
        IPage<TzMeetingRegister> registerPage = iTzMeetingRegisterService.meetingMyJoin(globalUserId,pageVo);
        List<TzMeetingRegister> records = registerPage.getRecords();
        if (records==null||records.size()==0){
            return ServerResponseEntity.success(records);
        }
        List<String> meetIds=new ArrayList<>();
        for (TzMeetingRegister tzMeetingRegister : records) {
            meetIds.add(tzMeetingRegister.getMeetingId());
        }
        QueryWrapper<TzOrganizationLife> organizationLifeQueryWrapper=new QueryWrapper<>();
        organizationLifeQueryWrapper.in("tz_organization_life.id",meetIds).eq("tz_organization_life.del_flag",0);
        List<TzOrganizationLife> organizationLives = tzOrganizationLifeService.list(organizationLifeQueryWrapper);

        return ServerResponseEntity.success(organizationLives);
    }

}

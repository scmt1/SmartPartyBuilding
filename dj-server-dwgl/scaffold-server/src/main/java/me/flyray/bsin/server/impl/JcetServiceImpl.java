package me.flyray.bsin.server.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import me.flyray.bsin.facade.service.JcetService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.OneaccessProperties;
import me.flyray.bsin.server.domain.TzLoginLog;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzLoginLogMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JcetServiceImpl implements JcetService {

    @Autowired
    private final  OneaccessProperties oneaccessProperties;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzLoginLogMapper tzLoginLogMapper;

    public JcetServiceImpl() {
        oneaccessProperties = new OneaccessProperties();
    }

    @Override
    public Map<String, Object> jcetLogin(Map<String, Object> requestMap) {
        try {
            String token = (String) requestMap.get("token");
            // String idCard = (String) requestMap.get("idCard");
            if (StringUtils.isBlank(token)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到登录凭证");
            }
            String oauthCode = getOauthCode(token);
            String idcard = OauthCodeToken(oauthCode);

            if (StringUtils.isBlank(idcard)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"酒城e通授权码登录失败");
            }

            QueryWrapper q = new QueryWrapper();
            q.eq("idcard", idcard);
            q.eq("del_flag", 0);
            List<JcxfPartyMember> partyMemberList = jcxfPartyMemberMapper.selectList(q);
            if (partyMemberList.size() > 1) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"存在多个相同身份证账号");
            }

            if (partyMemberList.size() != 0) {
                JcxfPartyMember partyMember = partyMemberList.get(0);
                JcxfSysDept tzSysDept = jcxfSysDeptMapper.selectById(partyMember.getDeptId());
                partyMember.setDeptName(tzSysDept.getName());
                partyMember.setTenantId(tzSysDept.getTenantId());
                partyMember.setPassword("");

                // 登录日志
                TzLoginLog log = new TzLoginLog();
                log.setDeptId(partyMember.getDeptId());
                log.setPartyMemberId(partyMember.getId());
                log.setRealName(partyMember.getRealName());
                log.setIdCard(partyMember.getIdcard());
                log.setPhone(partyMember.getPhone());
                log.setCreateTime(new Date());
                log.setType("3"); //   1、密码登录 2、短信验证码登录 3、统一认证登录
                log.setResult("1"); // 1、成功 2、失败
                tzLoginLogMapper.insert(log);

                UpdateWrapper updateWrapper = new UpdateWrapper();
                updateWrapper.eq("id", partyMember.getId());
                updateWrapper.set("app_login_date", new Date());
                jcxfPartyMemberMapper.update(null, updateWrapper);

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(partyMember));

            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL),"未获取到账号信息");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL),"登录异常");
        }
    }

    /**
     * 根据token获取授权码
     *
     * @param token
     * @return
     */
    private String getOauthCode(String token) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("appZoneId", oneaccessProperties.getAppZoneId());
        String url = oneaccessProperties.getOauthUrl() + "/authCode";
        //log.info("--------获取酒城e通授权码开始 token:{}--------", token);
        String result = HttpRequest.post(url)
                .header(Header.AUTHORIZATION, "Bearer " + token)//头信息，多个头信息多次调用此方法即可
                .form(param)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();

        R<Map<String, String>> res = JSONUtil.toBean(result, R.class);
        if (res.getCode() != 200) {
            //log.info("--------获取酒城e通授权码失败！ token:{}--------", token);
            throw new Exception("获取酒城e通授权码失败！" + res.getMsg());
        }
        String oauthCode = res.getData().get("oauthCode");
        //log.info("--------获取酒城e通授权码成功！ oauthCode:{}--------", oauthCode);
        return oauthCode;
    }


    /**
     * 根据授权码换取用户信息
     *
     * @param oauthCode
     * @return
     */
    private String OauthCodeToken(String oauthCode) throws Exception {
        //是否推送系统id
        boolean isPush = false;
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("grant_type", oneaccessProperties.getGrantType());
        param.put("oauthCode", oauthCode);
        String url = oneaccessProperties.getOauthUrl() + "/token";
        //log.info("--------用酒城e通授权码登录开始: oauthCode:{}--------", oauthCode);
        String auth = oneaccessProperties.getAppSecret();
        String result = HttpRequest.post(url)
                .header(Header.AUTHORIZATION, "Basic " + auth)//头信息，多个头信息多次调用此方法即可
                .form(param)//表单内容
                .timeout(20000)//超时，毫秒
                .execute().body();

        R<Map<String, Object>> res = JSONUtil.toBean(result, R.class);
        if (res.getCode() != 200) {
            throw new Exception("酒城e通授权码登录失败！" + res.getMsg());
        }
        Map<String, String> detailMap = null;
        if (ObjectUtil.isNotNull(res.getData()) && ObjectUtil.isNotNull(res.getData().get("detail"))) {
            detailMap = (Map) res.getData().get("detail");
        } else {
            throw new Exception("酒城e通授权码登录失败");
        }

        System.out.println("获取登录信息：" + detailMap);

        String idcard = detailMap.get("idCard");

        // idcard = "510521199510231010";
        return idcard;
    }
}

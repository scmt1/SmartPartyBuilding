package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzMessageAutoRoleService;
import me.flyray.bsin.facade.service.TzMessageAutoService;
import me.flyray.bsin.facade.service.TzMessageService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.*;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */

public class TzMessageAutoServiceImpl extends ServiceImpl<TzMessageAutoMapper, TzMessageAuto> implements TzMessageAutoService {

    @Autowired
    private TzMessageAutoMapper tzMessageAutoMapper;

    @Autowired
    private TzMessageAutoRoleMapper tzMessageAutoRoleMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;

    @Autowired
    private TzMessageAutoContentMapper tzMessageAutoContentMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private TzMessageMapper tzMessageMapper;

    @Autowired
    private TzMessageServiceImpl tzMessageService;

    @Autowired
    private TzMessageDetailMapper tzMessageDetailMapper;

    @Override
    public Map<String, Object> queryMessageAuto(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            TzMessageAuto tzMessageAuto = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessageAuto")), TzMessageAuto.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessageAuto")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
            String tenantId = (String) requestMap.get("bizTenantId");
            String deptId = (String) dataMap.get("deptId");

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

            Page<TzMessageAuto> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessageAuto> queryWrapper = new QueryWrapper<>();
            /*if (tzMessageAuto != null) {
                queryWrapper = LikeAllField(tzMessageAuto, searchVo);
            }*/

            // queryWrapper.eq("tenant_id", tenantId);

            Map<Integer, TzMessageAutoRole> map = new HashMap<>();
            if (StringUtils.isNotBlank(deptId)) {
                JcxfSysDept dept = jcxfSysDeptMapper.selectById(deptId);
                String[] deptIds = dept.getParentIds().split(",");
                List<String> deptIdList = new ArrayList<>(Arrays.asList(deptIds));
                deptIdList.add(deptId);

                QueryWrapper q1 = new QueryWrapper();
                q1.in("dept_id", deptIdList);
                List<TzMessageAutoRole> roles = tzMessageAutoRoleMapper.selectList(q1);
                if (roles != null && roles.size() > 0) {
                    List<Integer> ids = new ArrayList<>();
                    for (TzMessageAutoRole role : roles) {
                        ids.add(role.getAutoId());
                        map.put(role.getAutoId(), role);
                    }
                    queryWrapper.in("id", ids);
                } else {
                    return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(new Page<TzMessageAuto>()));
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未获取到部门信息");
            }

            queryWrapper.eq("is_open", 1);

            IPage<TzMessageAuto> result = tzMessageAutoMapper.selectPage(pageData, queryWrapper);

            List<Integer> ids = new ArrayList<>();
            for (TzMessageAuto record : result.getRecords()) {
                if (StringUtils.isNotBlank(record.getTemId())) {
                    TzMessageAuto tzMessageAuto1 = ShortMessageUtil.queryTemplateById(record.getTemId());
                    if (tzMessageAuto1 != null && "200".equals(tzMessageAuto1.getCode())) {
                        record.setTemContent(tzMessageAuto1.getTemContent());
                        record.setTemName(tzMessageAuto1.getTemName());
                        record.setRemark(tzMessageAuto1.getRemark());
                    }
                } else {
                    TzMessageAutoRole autoRole = map.get(record.getId());
                    if (autoRole != null) {
                        record.setRole(autoRole);
                        record.setIsOpen(Integer.parseInt(autoRole.getStatus()));
                    }
                    ids.add(record.getId());
                }
            }

            if (ids.size() > 0) {
                QueryWrapper queryWrapper1 = new QueryWrapper();
                queryWrapper1.select("auto_id, tem_content");
                queryWrapper1.eq("dept_id", deptId);
                queryWrapper1.in("auto_id", ids);
                List<TzMessageAutoContent> contentList = tzMessageAutoContentMapper.selectList(queryWrapper1);
                for (TzMessageAuto record : result.getRecords()) {
                    for (TzMessageAutoContent content : contentList) {
                        if (record.getId().equals(content.getAutoId())) {
                            record.setTemContent(content.getTemContent());
                            break;
                        }
                    }
                }
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
        }

    }

    @Override
    public Map<String, Object> getMessageAutoPageByAdmin(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(map.get("pageVo")), PageVo.class);

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
            QueryWrapper<TzMessageAuto> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId);
            Page<TzMessageAuto> pageData = new Page<>(page, limit);
            IPage<TzMessageAuto> result = tzMessageAutoMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(result));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
        }
    }

    @Override
    public Map<String, Object> updateAutoMessageOpenStatus(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String openStatus = (String) requestMap.get("openStatus");

            UpdateWrapper up = new UpdateWrapper();
            up.eq("id", id);
            up.set("is_open", openStatus);

            int count = tzMessageAutoMapper.update(null, up);
            if (count > 0) {
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
    public Map<String, Object> queryMessageAutoById(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            String deptId = (String) requestMap.get("deptId");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
            }
            TzMessageAuto tzMessageAuto = tzMessageAutoMapper.selectById(id);

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("dept_id", deptId);
            queryWrapper.eq("auto_id", id);
            TzMessageAutoContent content = tzMessageAutoContentMapper.selectOne(queryWrapper);
            if (content != null) {
                tzMessageAuto.setContent(content);
            }
            /*TzMessageAutoRole role = tzMessageAutoRoleMapper.selectOne(queryWrapper);
            if (role != null) {
                tzMessageAuto.setRemark(role.getRemark());
                tzMessageAuto.setTemContent(role.getTemContent());
                tzMessageAuto.setIsOpen(Integer.parseInt(role.getStatus()));
            }*/

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageAuto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
        }
    }

    @Override
    public Map<String, Object> queryMessageAutoByAdmin(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("id");
            if (id == null) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "参数为空");
            }
            TzMessageAuto tzMessageAuto = tzMessageAutoMapper.selectById(id);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageAuto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
        }
    }

    @Override
    public Map<String, Object> updateMessageAutoById(Map<String, Object> requestMap) {
        try {
            TzMessageAuto tzMessageAuto = JSON.parseObject(JSON.toJSONString(requestMap.get("tzMessageAuto")), TzMessageAuto.class);

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("id", tzMessageAuto.getId());
            updateWrapper.set("mission_name", tzMessageAuto.getMissionName());
            updateWrapper.set("tem_name", tzMessageAuto.getTemName());
            updateWrapper.set("tem_content", tzMessageAuto.getTemContent());
            updateWrapper.set("remark", tzMessageAuto.getRemark());

            // int i = tzMessageAutoMapper.updateById(tzMessageAuto);
            int i = tzMessageAutoMapper.update(null, updateWrapper);
            if (i > 0) {
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
    public Map<String, Object> getMessageAutoList(Map<String, Object> requestMap) {
        try {
            QueryWrapper queryWrapper = new QueryWrapper();
            // queryWrapper.eq("mission_type", "1"); // 1、自动任务 2、手动任务
            queryWrapper.eq("is_open", 1); //是否开启0否1是
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageAutoMapper.selectList(queryWrapper)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public void queryPolicyBirthdayToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 注意月份是从0开始计数的
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
        autoQueryWrapper.eq("tz_message_auto.type", "1");//1代表政治生日的短信模板，已经写入字典
        TzMessageAuto tzMessageAuto = tzMessageAutoMapper.selectOne(autoQueryWrapper);

        // 总任务启用
        if (tzMessageAuto.getIsOpen() == 1) {
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(8009L);
            QueryWrapper q = new QueryWrapper();
            q.select("dept_id, real_name ,phone AS mobile, party_time");
            q.in("dept_id", deptIds);
            q.eq("del_flag", 0);
            q.lt("YEAR(party_time)", year);
            q.eq("MONTH(party_time)", month);
            q.eq("DAY(party_time)", day);
            q.orderByAsc("YEAR(party_time)");
            List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(q);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sendObject = "";
            String sign = tzMessageAuto.getSign();
            if (list != null && list.size() > 0) {
                for (JcxfPartyMember partyMember : list) {
                    String[] data = simpleDateFormat.format(partyMember.getPartyTime()).split("-");
                    partyMember.setPoliticsAge(year - Integer.parseInt(data[0]));

                    //解密手机号
                    partyMember.setPhone(AESUtil2.decrypt(partyMember.getMobile()));

                    sendObject = sendObject + partyMember.getPhone() + ",";
                }
            }
            TzMessage tzMessage = new TzMessage();
            tzMessage.setMissionName("政治生日短信" + year + month + day);
            tzMessage.setSendTimeType(1);
            tzMessage.setSendMod(1);
            tzMessage.setSendType(1);

            String temContent = tzMessageAuto.getTemContent();
            tzMessage.setContent(sign + temContent);
            tzMessage.setSign(sign);

            if (sendObject.length() > 0) {
                sendObject = sendObject.substring(0, sendObject.length() - 1);
                tzMessage.setSendObject(sendObject);
            }
            tzMessage.setSendTime(new Date());
            tzMessage.setCreateTime(new Date());
            tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）

            tzMessageMapper.insert(tzMessage);

            int success = 0;
            int error = 0;
            List<TzMessageDetail> detailList = new ArrayList<>();
            for (JcxfPartyMember partyMember : list) {
                String content = temContent.replace("{name}", partyMember.getRealName())
                        .replace("{year}", partyMember.getPoliticsAge().toString());
                System.out.println("content==" + content);
                System.out.println("phone==" + partyMember.getPhone());
                ShortMessageResult shortMessageResult = ShortMessageUtil.sendMessageBySign(partyMember.getPhone(), content, sign);
                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    success++;
                    status = "1";
                } else {
                    error++;
                    status = "2";
                }

//                boolean b = tzMessageService.saveMessageDetail(partyMember.getPhone(), content, sign, tzMessage.getMissionName(), tzMessage.getId(), null);
//                if (b) {
//                    success = success + 1;
//                } else {
//                    error = error + 1;
//                }

                TzMessageDetail detail = new TzMessageDetail();
                detail.setMissionName(tzMessage.getMissionName());
                if (shortMessageResult != null) {
                    detail.setMsgId(shortMessageResult.getMsgId());
                    detail.setFeeCount(shortMessageResult.getContNum());
                    detail.setResultCode(shortMessageResult.getCode());
                    detail.setResultMsg(shortMessageResult.getMsg());
                }
                detail.setPhone(partyMember.getPhone());
                detail.setSendStatus(status);
                detail.setSendContent(tzMessage.getSign() + content);
                detail.setPostTime(new Date());
                detail.setDeptId(partyMember.getDeptId());
                detail.setSendType("1");
                detail.setTemplateId(String.valueOf(tzMessageAuto.getId()));
                detailList.add(detail);
            }
            tzMessage.setSuccessCount(success);
            tzMessage.setErrorCount(error);
            tzMessageMapper.updateById(tzMessage);

            //保存短信详情
            for (TzMessageDetail detail : detailList) {
                detail.setMessageId(tzMessage.getId());
                tzMessageDetailMapper.insert(detail);
            }
        }
    }


    @Override
    public void queryBirthdayToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 注意月份是从0开始计数的
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
        autoQueryWrapper.eq("tz_message_auto.type", "2");//2代表党员生日的短信模板，已经写入字典
        TzMessageAuto tzMessageAuto = tzMessageAutoMapper.selectOne(autoQueryWrapper);

        // 总任务启用
        if (tzMessageAuto.getIsOpen() == 1) {
            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(8009L);
            QueryWrapper q = new QueryWrapper();
            q.select("dept_id, real_name ,phone AS mobile, birthday");
            q.in("dept_id", deptIds);
            q.eq("del_flag", 0);
            q.lt("YEAR(birthday)", year);
            q.eq("MONTH(birthday)", month);
            q.eq("DAY(birthday)", day);
            q.orderByAsc("YEAR(birthday)");
            List<JcxfPartyMember> list = jcxfPartyMemberMapper.selectList(q);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sendObject = "";
            String sign = tzMessageAuto.getSign();
            if (list != null && list.size() > 0) {
                for (JcxfPartyMember partyMember : list) {
                    String[] data = simpleDateFormat.format(partyMember.getBirthday()).split("-");
                    partyMember.setAge(year - Integer.parseInt(data[0]));
                    sendObject = sendObject + partyMember.getPhone() + ",";

                    //解密手机号
                    partyMember.setPhone(AESUtil2.decrypt(partyMember.getMobile()));
                }
            }
            TzMessage tzMessage = new TzMessage();
            tzMessage.setMissionName("党员生日短信" + year + month + day);
            tzMessage.setSendTimeType(1);
            tzMessage.setSendMod(1);
            tzMessage.setSendType(1);

            String temContent = tzMessageAuto.getTemContent();
            tzMessage.setContent(sign + temContent);
            tzMessage.setSign(sign);

            if (sendObject.length() > 0) {
                sendObject = sendObject.substring(0, sendObject.length() - 1);
                tzMessage.setSendObject(sendObject);
            }

            tzMessage.setSendTime(new Date());
            tzMessage.setCreateTime(new Date());
            tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
            int count2 = tzMessageMapper.insert(tzMessage);
            if (count2 == 0) {
                return;
            }

            int success = 0;
            int error = 0;
            List<TzMessageDetail> detailList = new ArrayList<>();
            for (JcxfPartyMember partyMember : list) {
                String content = temContent.replace("{name}", partyMember.getRealName())
                        .replace("{age}", partyMember.getAge().toString());
                System.out.println("content==" + content);
                System.out.println("phone==" + partyMember.getPhone());

                ShortMessageResult shortMessageResult = ShortMessageUtil.sendMessageBySign(partyMember.getPhone(), content, sign);
                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    success++;
                    status = "1";
                } else {
                    error++;
                    status = "2";
                }
                TzMessageDetail detail = new TzMessageDetail();
                detail.setMissionName(tzMessage.getMissionName());
                if (shortMessageResult != null) {
                    detail.setMsgId(shortMessageResult.getMsgId());
                    detail.setFeeCount(shortMessageResult.getContNum());
                    detail.setResultCode(shortMessageResult.getCode());
                    detail.setResultMsg(shortMessageResult.getMsg());
                }
                detail.setPhone(partyMember.getPhone());
                detail.setSendStatus(status);
                detail.setSendContent(tzMessage.getSign() + content);
                detail.setPostTime(new Date());
                detail.setDeptId(partyMember.getDeptId());
                detail.setSendType("1");
                detail.setTemplateId(String.valueOf(tzMessageAuto.getId()));
                detailList.add(detail);
            }
            tzMessage.setSuccessCount(success);
            tzMessage.setErrorCount(error);
            tzMessageMapper.updateById(tzMessage);
            if (count2 > 0) {
                //保存短信详情
                for (TzMessageDetail detail : detailList) {
                    detail.setMessageId(tzMessage.getId());
                    tzMessageDetailMapper.insert(detail);
                }
            }
        }
    }

    @Override
    public Map<String, Object> queryBirthdayTemplate(Map<String, Object> requestMap) {
        try {
            List<String> typeList = new ArrayList<>();
            typeList.add("1");
            typeList.add("2");

            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.in("tz_message_auto.type", typeList);//2代表党员生日的短信模板，已经写入字典
            List<TzMessageAuto> tzMessageAutoList = tzMessageAutoMapper.selectList(autoQueryWrapper);

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageAutoList));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }


    public QueryWrapper<TzMessageDetail> LikeAllField(TzMessageDetail tzMessageDetail, SearchVo searchVo) {
        QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(tzMessageDetail.getPhone())) {
            queryWrapper.and(i -> i.like("tz_message_detail.phone", tzMessageDetail.getPhone()));
        }
        if (StringUtils.isNotEmpty(tzMessageDetail.getMsgId())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.msg_id", tzMessageDetail.getMsgId()));
        }
        if (StringUtils.isNotEmpty(tzMessageDetail.getTemplateId())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.template_id", tzMessageDetail.getTemplateId()));
        }
        if (searchVo != null) {
            if (StringUtils.isNotEmpty(searchVo.getStartDate()) && StringUtils.isNotEmpty(searchVo.getEndDate())) {
                queryWrapper.lambda().and(i -> i.between(TzMessageDetail::getPostTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        return queryWrapper;

    }


}

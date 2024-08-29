package me.flyray.bsin.server.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.flyray.bsin.facade.service.TzMessageService;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.*;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;
import me.flyray.bsin.server.mapper.TzLoginLogMapper;
import me.flyray.bsin.server.mapper.TzMessageAutoMapper;
import me.flyray.bsin.server.mapper.TzMessageDetailMapper;
import me.flyray.bsin.server.mapper.TzMessageMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfPartyMemberMapper;
import me.flyray.bsin.server.mapper.jcxf.JcxfSysDeptMapper;
import me.flyray.bsin.server.utils.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * 具体投票项 服务实现类
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */

public class TzMessageServiceImpl extends ServiceImpl<TzMessageMapper, TzMessage> implements TzMessageService {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired(required = true)
    private RedisUtils redisUtils;
    @Autowired
    private TzMessageMapper tzMessageMapper;
    @Autowired
    private TzMessageDetailMapper tzMessageDetailMapper;

    @Autowired
    private JcxfPartyMemberMapper jcxfPartyMemberMapper;

    @Autowired
    private TzMessageAutoMapper tzMessageAutoMapper;

    @Autowired
    private JcxfSysDeptMapper jcxfSysDeptMapper;


    @Override
    public Map<String, Object> sendMessage(Map<String, Object> requestMap) {
        try {
            TzMessage tzMessage = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), TzMessage.class);
            if (tzMessage == null || StringUtils.isEmpty(tzMessage.getSendObject())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送对象不能为空");
            }

            String tenantId = (String) requestMap.get("bizTenantId");

            //过滤无效的手机号
            Set<String> validPhoneNumbers = getValidPhones(tzMessage);
            if (validPhoneNumbers.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未存在有效手机号");
            }

            //立即发送
            Date nowDate = new Date();
            if (tzMessage.getSendTimeType().intValue() == 1) {
                tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
                tzMessage.setSendTime(nowDate);
            } else if (tzMessage.getSendTimeType().intValue() == 2) {
                if (tzMessage.getSendTime() == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "定时发送时间不能为空");
                }
                // 定时发送时间应大于当前时间
                if (tzMessage.getSendTime().getTime() < new Date().getTime()) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "定时发送时间应大于当前时间");
                }
                tzMessage.setStatus("3");
            }
            tzMessage.setTenantId(tenantId);
            tzMessage.setCreateTime(nowDate);
            int insert = tzMessageMapper.insert(tzMessage);
            if (insert == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送失败");
            }

            int successCount = 0;
            int errorCount = 0;
            List<TzMessageDetail> detailList = new ArrayList<>();

            for (String validPhoneNumber : validPhoneNumbers) {
                ShortMessageResult shortMessageResult = null;
                if (tzMessage.getSendTimeType().intValue() == 1) {
                    shortMessageResult = ShortMessageUtil.sendMessageBySign(validPhoneNumber, tzMessage.getContent(), tzMessage.getSign());
                } else {
                    shortMessageResult = ShortMessageUtil.sendMessageBySignByTime(validPhoneNumber, tzMessage.getContent(), tzMessage.getSign(), tzMessage.getSendTime());
                }

                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    successCount++;
                    status = "1";
                } else {
                    errorCount++;
                    status = "2";
                }

                TzMessageDetail tzMessageDetail = new TzMessageDetail();
                //tzMessageDetail.setTemplateId(tzMessage.getId().toString());
                tzMessageDetail.setMessageId(tzMessage.getId());
                tzMessageDetail.setMissionName(tzMessage.getMissionName());
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    tzMessageDetail.setMsgId(shortMessageResult.getMsgId());
                    tzMessageDetail.setFeeCount(shortMessageResult.getContNum());
                }
                tzMessageDetail.setPhone(validPhoneNumber);
                tzMessageDetail.setSendStatus(status);
                tzMessageDetail.setSendContent(tzMessage.getSign() + tzMessage.getContent());
                tzMessageDetail.setPostTime(tzMessage.getSendTime());
                tzMessageDetail.setTenantId(tenantId);
                tzMessageDetail.setSendType("1");

                if (shortMessageResult != null) {
                    tzMessageDetail.setResultCode(shortMessageResult.getCode());
                    tzMessageDetail.setResultMsg(shortMessageResult.getMsg());
                }

                detailList.add(tzMessageDetail);
                // tzMessageDetailMapper.insert(tzMessageDetail);
            }

            tzMessage.setSuccessCount(successCount);
            tzMessage.setErrorCount(errorCount);
            tzMessageMapper.updateById(tzMessage);
            if (insert > 0) {
                //保存短信详情
                for (TzMessageDetail detail : detailList) {
                    detail.setMessageId(tzMessage.getId());
                    tzMessageDetailMapper.insert(detail);
                }
            }

            Map<String, Integer> map = new HashMap<>();
            map.put("successCount", successCount);
            map.put("errorCount", errorCount);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送失败:" + e.getMessage());
        }

    }

    @Override
    public Map<String, Object> sendTemplateMessage(Map<String, Object> requestMap) {
        try {
            Map<String, Object> stringObjectMap = (Map<String, Object>) requestMap.get("data");
            TzMessage tzMessage = JSON.parseObject(JSON.toJSONString(stringObjectMap.get("tzMessage")), TzMessage.class);
            String tpId = (String) stringObjectMap.get("tpId");
            String tenantId = (String) requestMap.get("bizTenantId");

            if (tzMessage == null || StringUtils.isEmpty(tzMessage.getSendObject())) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送对象不能为空");
            }
            //立即发送
            Date nowDate = new Date();
            if (tzMessage.getSendTimeType().intValue() == 1) {
                tzMessage.setStatus("1"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
                tzMessage.setSendTime(nowDate);
            } else if (tzMessage.getSendTimeType().intValue() == 2) {
                if (tzMessage.getSendTime() == null) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "定时发送时间不能为空");
                }
                // 定时发送时间应大于当前时间
                if (tzMessage.getSendTime().getTime() < new Date().getTime()) {
                    throw new BusinessException(String.valueOf(ResponseCode.FAIL), "定时发送时间应大于当前时间");
                }
                tzMessage.setStatus("3"); // （1、发送结束，2、发送失败，3、待返回,4、已取消）
            }
            tzMessage.setCreateTime(nowDate);
            tzMessage.setTenantId(tenantId);
            int insert = tzMessageMapper.insert(tzMessage);
            int successCount = 0;
            int errorCount = 0;
            ShortMessageResult shortMessageResult = null;
            if (insert > 0) {
                if (tzMessage.getSendTimeType().intValue() == 1) {
                    shortMessageResult = ShortMessageUtil.sendMessageByTpId(tpId, tzMessage.getRecords(), tzMessage.getSign());
                } else {
                    shortMessageResult = ShortMessageUtil.sendMessageByTpIdByTime(tpId, tzMessage.getRecords(), tzMessage.getSign(), tzMessage.getSendTime());
                }
                List<String> listPhone = new ArrayList<>();
                String status = "0";
                if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                    if (shortMessageResult.getInvalidList() != null && shortMessageResult.getInvalidList().size() > 0) {
                        for (Object o : shortMessageResult.getInvalidList()) {
                            Map<String, Object> map = (Map<String, Object>) o;
                            String phone = (String) map.get("mobile");
                            listPhone.add(phone);
                            errorCount++;
                        }
                    }
                    successCount++;
                    status = "1";
                } else {
                    errorCount = tzMessage.getRecords().size();
                    status = "2";
                }

                for (MessageDto record : tzMessage.getRecords()) {
                    TzMessageDetail tzMessageDetail = new TzMessageDetail();
                    tzMessageDetail.setTemplateId(tpId);
                    tzMessageDetail.setMessageId(tzMessage.getId());
                    tzMessageDetail.setMissionName(tzMessage.getMissionName());
                    tzMessageDetail.setPhone(record.getMobile());
                    String mobile = record.getMobile();
                    if (listPhone != null && listPhone.size() > 0 && listPhone.contains(mobile)) {
                        tzMessageDetail.setSendStatus("2");
                    } else {
                        tzMessageDetail.setSendStatus(status);
                    }

                    // 记录详细发送内容
                    String content = tzMessage.getContent();
                    for (Map.Entry<String, Object> entry : record.getTpContent().entrySet()) {
                        content = content.replace("{" + entry.getKey() + "}", entry.getValue() != null ? " " + entry.getValue() + " " : " ");
                    }
                    tzMessageDetail.setSendContent(content);

                    tzMessageDetail.setPostTime(tzMessage.getSendTime());
                    tzMessageDetail.setTenantId(tenantId);
                    tzMessageDetail.setSendType("2");

                    if (shortMessageResult != null) {
                        tzMessageDetail.setResultCode(shortMessageResult.getCode());
                        tzMessageDetail.setResultMsg(shortMessageResult.getMsg());
                        tzMessageDetail.setMsgId(shortMessageResult.getMsgId());
                        tzMessageDetail.setFeeCount(shortMessageResult.getContNum());
                    }

                    tzMessageDetailMapper.insert(tzMessageDetail);
                }

            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送失败");
            }
            tzMessage.setSuccessCount(successCount);
            tzMessage.setErrorCount(errorCount);
            int i = tzMessageMapper.updateById(tzMessage);
            Map<String, Object> map = new HashMap<>();
            map.put("successCount", successCount);
            map.put("errorCount", errorCount);
            if (shortMessageResult != null && !"200".equals(shortMessageResult.getCode())) {
                map.put("errorCode", shortMessageResult.getCode());
                map.put("errorMsg", shortMessageResult.getMsg());
            }

            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "发送失败");
        }
    }

    public void saveMessageInfo(TzMessage tzMessage) {
        int insert = tzMessageMapper.insert(tzMessage);
        // System.out.println(insert);
    }

    public boolean saveMessageDetail(String phone, String content, String sign, String missionName, Long messageId, String tenantId) {
        ShortMessageResult shortMessageResult = ShortMessageUtil.sendMessageBySign(phone, content, sign);

        TzMessageDetail tzMessageDetail = new TzMessageDetail();
        tzMessageDetail.setMessageId(messageId);
        tzMessageDetail.setMsgId(shortMessageResult.getMsgId());
        tzMessageDetail.setPhone(phone);
        tzMessageDetail.setMissionName(missionName);
        tzMessageDetail.setFeeCount(shortMessageResult.getContNum());
        tzMessageDetail.setSendContent(sign + content);
        tzMessageDetail.setPostTime(new Date());
        if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
            tzMessageDetail.setSendStatus("1");
            int insert = tzMessageDetailMapper.insert(tzMessageDetail);
            return true;
        } else {
            tzMessageDetail.setSendStatus("2");
            tzMessageDetailMapper.insert(tzMessageDetail);
            return false;
        }
    }

    //过滤无效的手机号
    public Set<String> getValidPhones(TzMessage tzMessage) {
        String sendObject = tzMessage.getSendObject();
        Set<String> validPhoneNumbers = new HashSet<>();
        Set<String> unValidPhoneNumbers = new HashSet<>();
        String cleanedPhoneNumbers[] = sendObject.trim().replace("，", ",").split(",");

        // 正则表达式校验电话号码格式
        String phoneRegex = "^(\\+\\d{1,3})?1[3456789]\\d{9}$";
        for (String phoneNumber : cleanedPhoneNumbers) {
            String cleanedPhoneNumber = phoneNumber.trim(); // 去除空格
            // 校验电话号码格式
            if (cleanedPhoneNumber.matches(phoneRegex)) {
                validPhoneNumbers.add(cleanedPhoneNumber);
            } else {
                unValidPhoneNumbers.add(cleanedPhoneNumber);
            }
        }

        return validPhoneNumbers;
    }

    @Override
    public Map<String, Object> queryMessage(Map<String, Object> requestMap) {
        try {

            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            TzMessage tzMessage = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessage")), TzMessage.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessage")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
            String tenantId = (String) requestMap.get("bizTenantId");

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
            Page<TzMessage> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessage> queryWrapper = new QueryWrapper<>();
            if (tzMessage != null) {
                queryWrapper = LikeAllField(tzMessage, searchVo);
            }
            queryWrapper.eq("tenant_id", tenantId);
            queryWrapper.orderByDesc("send_time");
            IPage<TzMessage> tzMessageIPage = tzMessageMapper.selectPage(pageData, queryWrapper);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageIPage));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    @Override
    public Map<String, Object> queryMessageTemplate(Map<String, Object> requestMap) {
        try {
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(requestMap.get("data")), PageVo.class);
            Map<String, Object> map = (Map<String, Object>) requestMap.get("data");
            Integer status = (Integer) map.get("status");
            int pageNumber = pageVo.getPageNumber();
            int pageSize = pageVo.getPageSize();
            ShortMessageResult shortMessageResult = ShortMessageUtil.queryMessageTemplate(status, pageNumber, pageSize);
            if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(shortMessageResult));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    @Override
    public Map<String, Object> queryMessageDetail(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            TzMessageDetail tzMessageDetail = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessageDetail")), TzMessageDetail.class);
            SearchVo searchVo = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessageDetail")), SearchVo.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
            String tenantId = (String) requestMap.get("bizTenantId");

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
            Page<TzMessageDetail> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
            if (tzMessageDetail != null) {
                queryWrapper = LikeAllField2(tzMessageDetail, searchVo);
            }
            queryWrapper.eq("tenant_id", tenantId);
            queryWrapper.orderByDesc("post_time");
            IPage<TzMessageDetail> tzMessageIPage = tzMessageDetailMapper.selectPage(pageData, queryWrapper);
            /*for (TzMessageDetail detail: tzMessageIPage.getRecords()) {
                if (detail.getDeptId() != null) {
                    JcxfSysDept dept = jcxfSysDeptMapper.selectById(detail.getDeptId());
                    if (dept != null) {
                        detail.setDeptName(dept.getName());
                    }
                }
            }*/
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageIPage));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    @Override
    public Map<String, Object> sendVCode(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfPartyMember partyMember = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMember")), JcxfPartyMember.class);
        String phone = partyMember.getPhone();
        if (StringUtils.isEmpty(phone)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "手机号不能为空！");
        }
        // 查询该电话信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", AESUtil2.encrypt(phone));
        queryWrapper.eq("del_flag", false);
        List<JcxfPartyMember> memberList = jcxfPartyMemberMapper.selectList(queryWrapper);
        if (memberList.size() > 1) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在多个相同电话账户");
        } else if (memberList.size() == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请输入正确的电话号码");
        }

        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("验证码======" + code);
        //调用发送短信的功能
        try {
            ShortMessageResult shortMessageResult = ShortMessageUtil.sendVCode(code + "", phone);
            if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                //删除之前的验证码
                redisUtils.del(phone);
                //存储新的验证码
                redisUtils.set(phone, code + "", 5, TimeUnit.MINUTES);
                //RedisUtils.set(phone, code + "", 10000, TimeUnit.MINUTES);

                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("验证码发送成功！"));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码发送失败！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码发送失败！");
        }

    }

    @Override
    public Map<String, Object> recallSend(Map<String, Object> requestMap) {
        try {
            String id = (String) requestMap.get("data");
            if (StringUtils.isBlank(id)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "id不能为空");
            }
            List<String> statusList = new ArrayList<>();
            statusList.add("1");
            statusList.add("3");

            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tz_message_detail.message_id", id);
            queryWrapper.in("tz_message_detail.send_status", statusList); // 发送状态（1、成功，2、失败、3，待返回，4、已撤回）
            List<TzMessageDetail> tzMessageDetails = tzMessageDetailMapper.selectList(queryWrapper);

            if (tzMessageDetails.size() == 0) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "未查询到可撤回的发送记录");
            }

            List<String> msgIds = new ArrayList<>();
            //收集msgId集合，接口要用到改参数
            for (TzMessageDetail tzMessageDetail : tzMessageDetails) {
                msgIds.add(tzMessageDetail.getMsgId());
            }
            String[] toArrayMsgIds = msgIds.toArray(new String[msgIds.size()]);
            ShortMessageResult shortMessageResult = ShortMessageUtil.smsCancelTiming(toArrayMsgIds);
            if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                for (TzMessageDetail tzMessageDetail : tzMessageDetails) {
                    //修改状态为已取消
                    tzMessageDetail.setSendStatus("4");
                    int i = tzMessageDetailMapper.updateById(tzMessageDetail);
                }
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作失败");
            }
            TzMessage tzMessage = tzMessageMapper.selectById(id);
            tzMessage.setStatus("4");
            int i = tzMessageMapper.updateById(tzMessage);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("撤销成功！"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }

    }

    @Override
    public Map<String, Object> validCode(Map<String, Object> requestMap) {
        try {
            String phone = (String) requestMap.get("phone");
            String code = (String) requestMap.get("code");
            String redisCode = (String) redisUtils.get(phone);
            if (!redisCode.equals(code)) {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码错误");
            }
            redisUtils.del(phone);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> baseInfo(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            String deptId = (String) requestMap.get("deptId");

            //发送号码总数
            Integer phoneCount = 0;

            //发送短信总数
            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tenant_id", tenantId);
            Integer messageCount = tzMessageDetailMapper.selectCount(queryWrapper);

            //发送成功总数
            QueryWrapper qk = new QueryWrapper();
            qk.select("id");
            qk.eq("send_time_type", 2); // 发送时间方式（1、立即发送，2、定时发送）
            qk.eq("status", "3"); // （1、发送成功，2、发送失败，3、待返回,4、已取消
            qk.eq("tenant_id", tenantId);
            List<TzMessage> messageList = tzMessageMapper.selectList(qk);
            List<Long> messageIds = new ArrayList<>();
            for (TzMessage tzMessage : messageList) {
                messageIds.add(tzMessage.getId());
            }
            // 定时发送无返回结果，一并算入成功
            QueryWrapper<TzMessageDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.and(i -> i.eq("tz_message_detail.send_status", "1").eq("tenant_id", tenantId));
            if (messageIds.size() > 0) {
                queryWrapper2.or(i -> i.in("message_id", messageIds));
            }
            Integer successCount = tzMessageDetailMapper.selectCount(queryWrapper2);

            // 政治生日和党员生日无法定位到租户，因此单独统计
            List<String> typeList = new ArrayList<>();
            typeList.add("1"); //1代表政治生日的短信模板，已经写入字典
            typeList.add("2"); //2代表党员生日的短信模板，已经写入字典
            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.in("tz_message_auto.type", typeList);//
            List<TzMessageAuto> tzMessageAutoList = tzMessageAutoMapper.selectList(autoQueryWrapper);

            List<Integer> templateIds = new ArrayList();
            for (TzMessageAuto tzMessageAuto : tzMessageAutoList) {
                templateIds.add(tzMessageAuto.getId());
            }

            if (templateIds.size() > 0) {
                QueryWrapper q = new QueryWrapper();
                q.select("DISTINCT phone");
                q.eq("dept_id", deptId);
                q.eq("send_type", "1");
                q.in("template_id", templateIds);
                List<String> phoneList = new ArrayList<>();
                List<TzMessageDetail> detailList = tzMessageDetailMapper.selectList(q);

                if (detailList.size() > 0) {
                    phoneCount += detailList.size();
                    for (TzMessageDetail detail : detailList) {
                        phoneList.add(detail.getPhone());
                    }
                    QueryWrapper qn = new QueryWrapper();
                    qn.select("DISTINCT phone");
                    qn.eq("tenant_id", tenantId);
                    qn.notIn("phone", phoneList);
                    phoneCount += tzMessageDetailMapper.selectCount(qn);

                } else {
                    phoneCount += tzMessageDetailMapper.countByPhone(tenantId);
                }

                QueryWrapper q2 = new QueryWrapper();
                q2.eq("dept_id", deptId);
                q2.eq("send_type", "1");
                q2.in("template_id", templateIds);
                messageCount += tzMessageDetailMapper.selectCount(q2);

                QueryWrapper q3 = new QueryWrapper();
                q3.eq("dept_id", deptId);
                q3.eq("send_status", "1");
                q3.eq("send_type", "1");
                q3.in("template_id", templateIds);
                successCount += tzMessageDetailMapper.selectCount(q3);
            } else {
                phoneCount += tzMessageDetailMapper.countByPhone(tenantId);
            }


            //发送成功率
            double successRate = 0.0;
            if (messageCount != 0) {
                successRate = (double) successCount / messageCount * 100;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("phoneCount", phoneCount);
            map.put("messageCount", messageCount);
            map.put("successCount", successCount);
            map.put("successRate", successRate);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> baseInfoByToday(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            String deptId = (String) requestMap.get("deptId");

            LocalDate currentDate = LocalDate.now();
            //发送号码总数
            Integer phoneCount = 0;

            //发送短信总数
            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.apply("DATE(tz_message_detail.post_time) = {0}", currentDate);
            queryWrapper.eq("tenant_id", tenantId);
            Integer messageCount = tzMessageDetailMapper.selectCount(queryWrapper);

            //发送成功总数
            QueryWrapper qk = new QueryWrapper();
            qk.select("id");
            qk.eq("send_time_type", 2); // 发送时间方式（1、立即发送，2、定时发送）
            qk.eq("status", "3"); // （1、发送成功，2、发送失败，3、待返回,4、已取消
            qk.eq("tenant_id", tenantId);
            qk.apply("DATE(send_time) = {0}", currentDate);
            List<TzMessage> messageList = tzMessageMapper.selectList(qk);
            List<Long> messageIds = new ArrayList<>();
            for (TzMessage tzMessage : messageList) {
                messageIds.add(tzMessage.getId());
            }
            // 定时发送无返回结果，一并算入成功
            QueryWrapper<TzMessageDetail> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.and(i -> i.eq("tz_message_detail.send_status", "1")
                    .eq("tenant_id", tenantId)
                    .apply("DATE(tz_message_detail.post_time) = {0}", currentDate));
            if (messageIds.size() > 0) {
                queryWrapper2.or(i -> i.in("message_id", messageIds));
            }
            Integer successCount = tzMessageDetailMapper.selectCount(queryWrapper2);

            // 政治生日和党员生日无法定位到租户，因此单独统计
            List<String> typeList = new ArrayList<>();
            typeList.add("1"); //1代表政治生日的短信模板，已经写入字典
            typeList.add("2"); //2代表党员生日的短信模板，已经写入字典
            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.in("tz_message_auto.type", typeList);//
            List<TzMessageAuto> tzMessageAutoList = tzMessageAutoMapper.selectList(autoQueryWrapper);

            List<Integer> templateIds = new ArrayList();
            for (TzMessageAuto tzMessageAuto : tzMessageAutoList) {
                templateIds.add(tzMessageAuto.getId());
            }
            if (templateIds.size() > 0) {
                QueryWrapper q = new QueryWrapper();
                q.select("DISTINCT phone");
                q.eq("dept_id", deptId);
                q.eq("send_type", "1");
                q.in("template_id", templateIds);
                q.apply("DATE(tz_message_detail.post_time) = {0}", currentDate);
                List<String> phoneList = new ArrayList<>();
                List<TzMessageDetail> detailList = tzMessageDetailMapper.selectList(q);

                if (detailList.size() > 0) {
                    phoneCount += detailList.size();
                    for (TzMessageDetail detail : detailList) {
                        phoneList.add(detail.getPhone());
                    }
                    QueryWrapper qn = new QueryWrapper();
                    qn.select("DISTINCT phone");
                    qn.eq("tenant_id", tenantId);
                    qn.notIn("phone", phoneList);
                    qn.apply("DATE(tz_message_detail.post_time) = {0}", currentDate);
                    phoneCount += tzMessageDetailMapper.selectCount(qn);

                } else {
                    phoneCount += tzMessageDetailMapper.countByPhoneToday(tenantId);
                }

                QueryWrapper q2 = new QueryWrapper();
                q2.eq("dept_id", deptId);
                q2.eq("send_type", "1");
                q2.in("template_id", templateIds);
                q2.apply("DATE(tz_message_detail.post_time) = {0}", currentDate);
                messageCount += tzMessageDetailMapper.selectCount(q2);

                QueryWrapper q3 = new QueryWrapper();
                q3.eq("dept_id", deptId);
                q3.eq("send_status", "1");
                q3.eq("send_type", "1");
                q3.in("template_id", templateIds);
                q3.apply("DATE(tz_message_detail.post_time) = {0}", currentDate);
                successCount += tzMessageDetailMapper.selectCount(q3);
            } else {
                phoneCount += tzMessageDetailMapper.countByPhoneToday(tenantId);
            }

            //发送成功率
            double successRate = 0.0;
            if (messageCount != 0) {
                successRate = (double) successCount / messageCount * 100;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("phoneCount", phoneCount);
            map.put("messageCount", messageCount);
            map.put("successCount", successCount);
            map.put("successRate", successRate);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "操作异常");
        }
    }

    @Override
    public Map<String, Object> recentlySendCount(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);

            instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE), 23, 59, 59);
            String endTime = simpleDateFormat.format(instance.getTime());

            instance.add(Calendar.DATE, -9);
            instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE), 00, 00, 00);
            String startTime = simpleDateFormat.format(instance.getTime());

            // endTime = "2023-07-10 23:59:59";
            // startTime = "2023-07-01 00:00:00";

            // List<Map<Integer, String>> result = tzMessageDetailMapper.recentlySendCount(startTime, endTime, tenantId);


            // 政治生日和党员生日无法定位到租户，因此单独统计
            List<String> typeList = new ArrayList<>();
            typeList.add("1"); //1代表政治生日的短信模板，已经写入字典
            typeList.add("2"); //2代表党员生日的短信模板，已经写入字典
            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.in("tz_message_auto.type", typeList);
            List<TzMessageAuto> tzMessageAutoList = tzMessageAutoMapper.selectList(autoQueryWrapper);

            List<Integer> templateIds = new ArrayList();
            for (TzMessageAuto tzMessageAuto : tzMessageAutoList) {
                templateIds.add(tzMessageAuto.getId());
            }

            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper();
            queryWrapper.select("count(*) as num, DATE_FORMAT(post_time,'%Y-%m-%d') as time");
            if (templateIds.size() > 0) {
                queryWrapper.and(i -> i.eq("tenant_id", tenantId).or(i2 -> i2.in("template_id", templateIds)));
            } else {
                queryWrapper.eq("tenant_id", tenantId);
            }
            queryWrapper.ge("post_time", startTime);
            queryWrapper.le("post_time", endTime);
            queryWrapper.groupBy("DATE_FORMAT(post_time,'%Y-%m-%d')");
            List<Map<String, Object>> res = tzMessageDetailMapper.selectMaps(queryWrapper);

            Map<String, Object> map = new HashMap<>();
            map.put("data", res);
            map.put("startTime", startTime);
            map.put("endTime", endTime);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> recentlySuccessRate(Map<String, Object> requestMap) {
        try {
            String tenantId = (String) requestMap.get("bizTenantId");
            String deptId = (String) requestMap.get("deptId");

            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);


            instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE), 23, 59, 59);
            String endTime = simpleDateFormat.format(instance.getTime());

            instance.add(Calendar.DATE, -9);
            instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DATE), 00, 00, 00);
            String startTime = simpleDateFormat.format(instance.getTime());


            List<SendRateDto> rateDtos = tzMessageDetailMapper.recentlySendCountBySendStatus(startTime, endTime, tenantId);

            // 政治生日和党员生日无法定位到租户，因此单独统计
            List<String> typeList = new ArrayList<>();
            typeList.add("1"); //1代表政治生日的短信模板，已经写入字典
            typeList.add("2"); //2代表党员生日的短信模板，已经写入字典
            QueryWrapper<TzMessageAuto> autoQueryWrapper = new QueryWrapper<>();
            autoQueryWrapper.in("tz_message_auto.type", typeList);
            List<TzMessageAuto> tzMessageAutoList = tzMessageAutoMapper.selectList(autoQueryWrapper);
            if (tzMessageAutoList.size() > 0) {
                List<Integer> templateIds = new ArrayList();
                for (TzMessageAuto tzMessageAuto : tzMessageAutoList) {
                    templateIds.add(tzMessageAuto.getId());
                }

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.select("sum(case when send_status = '1' then 1 else 0 end ) as success," +
                        " sum(case when send_status != '1' then 1 else 0 end ) as fail," +
                        " DATE_FORMAT(post_time,'%Y-%m-%d') as time");
                queryWrapper.ge("post_time", startTime);
                queryWrapper.le("post_time", endTime);
                queryWrapper.eq("dept_id", deptId);
                queryWrapper.in("template_id", templateIds);
                queryWrapper.eq("send_type", "1"); // 发送类型 1、自定义模板 2、默认模板 3、自定义发送 4、组织生活 5、党建学习
                queryWrapper.groupBy("DATE_FORMAT(post_time,'%Y-%m-%d')");
                queryWrapper.orderBy(false, true, "DATE_FORMAT(post_time,'%Y-%m-%d')");
                List<Map<String, Object>> list = tzMessageDetailMapper.selectMaps(queryWrapper);
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> map : list) {
                        for (SendRateDto dto : rateDtos) {
                            if (map.get("time").equals(dto.getTime())) {
                                BigDecimal num = (BigDecimal) map.get("success");
                                if (num != null) {
                                    dto.setSuccess(dto.getSuccess() + num.intValue());
                                }
                                BigDecimal num2 = (BigDecimal) map.get("fail");
                                if (num != null) {
                                    dto.setFail(dto.getFail() + num2.intValue());
                                }
                            }
                        }
                    }
                }
            }

            // 定时任务默认为发送成功
            QueryWrapper qn = new QueryWrapper();
            qn.select("id");
            qn.ge("send_time", startTime);
            qn.le("send_time", endTime);
            qn.eq("tenant_id", tenantId);
            qn.eq("send_time_type", 2); // 发送时间方式（1、立即发送，2、定时发送）
            List<TzMessage> tzMessageList = tzMessageMapper.selectList(qn);
            if (tzMessageList != null && tzMessageList.size() > 0) {
                List<Long> ids = new ArrayList<>();
                for (TzMessage tzMessage : tzMessageList) {
                    ids.add(tzMessage.getId());
                }

                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.select("count(*) as num, " +
                        " DATE_FORMAT(post_time,'%Y-%m-%d') as time");
                queryWrapper.in("message_id", ids);
                queryWrapper.groupBy("DATE_FORMAT(post_time,'%Y-%m-%d')");
                queryWrapper.orderBy(false, true, "DATE_FORMAT(post_time,'%Y-%m-%d')");
                List<Map<String, Object>> list = tzMessageDetailMapper.selectMaps(queryWrapper);
                if (list != null && list.size() > 0) {
                    for (Map<String, Object> map : list) {
                        for (SendRateDto dto : rateDtos) {
                            if (map.get("time").equals(dto.getTime())) {
                                Long num = (Long) map.get("num");
                                if (num != null) {
                                    dto.setSuccess(dto.getSuccess() + num.intValue());
                                    dto.setFail(dto.getFail() - num.intValue());
                                }
                            }
                        }
                    }
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("startTime", startTime);
            map.put("result", rateDtos);
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(map));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }
    }

    @Override
    public Map<String, Object> queryBirthdayMessageDetail(Map<String, Object> requestMap) {
        try {
            Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
            TzMessageDetail tzMessageDetail = JSON.parseObject(JSON.toJSONString(dataMap.get("tzMessageDetail")), TzMessageDetail.class);
            PageVo pageVo = JSON.parseObject(JSON.toJSONString(dataMap.get("pageVo")), PageVo.class);
            String templateIdsStr = (String) dataMap.get("templateIdsStr");

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
            Page<TzMessageDetail> pageData = new Page<>(page, limit);
            QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();

            List<Long> deptIds = jcxfSysDeptMapper.selectChildrenIdsById(tzMessageDetail.getDeptId());
            deptIds.add(tzMessageDetail.getDeptId());
            //queryWrapper.eq("dept_id", tzMessageDetail.getDeptId());
            queryWrapper.in("dept_id", deptIds);
            if (StringUtils.isNotBlank(tzMessageDetail.getTemplateId())) {
                queryWrapper.eq("template_id", tzMessageDetail.getTemplateId());
            } else {
                queryWrapper.in("template_id", templateIdsStr.split(","));
            }
            if (StringUtils.isNotBlank(tzMessageDetail.getPhone())) {
                queryWrapper.eq("phone", tzMessageDetail.getPhone());
            }
            if (StringUtils.isNotBlank(tzMessageDetail.getSendStatus())) {
                queryWrapper.eq("send_status", tzMessageDetail.getSendStatus());
            }
            queryWrapper.eq("send_type", "1"); // 发送类型 1、自定义模板 2、默认模板 3、自定义发送 4、组织生活 5、党建学习
            queryWrapper.orderByDesc("post_time");

            IPage<TzMessageDetail> tzMessageIPage = tzMessageDetailMapper.selectPage(pageData, queryWrapper);
            for (TzMessageDetail detail : tzMessageIPage.getRecords()) {
                if (detail.getDeptId() != null) {
                    JcxfSysDept dept = jcxfSysDeptMapper.selectById(detail.getDeptId());
                    if (dept != null) {
                        detail.setDeptName(dept.getName());
                    }
                }
            }
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(tzMessageIPage));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "查询异常");
        }

    }

    @Override
    public Map<String, Object> sendBindVCode(Map<String, Object> requestMap) {
        Map<String, Object> dataMap = (Map<String, Object>) requestMap.get("data");
        JcxfPartyMember partyMember = JSON.parseObject(JSON.toJSONString(dataMap.get("partyMember")), JcxfPartyMember.class);
        String phone = partyMember.getPhone();
        if (StringUtils.isEmpty(phone)) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "手机号不能为空！");
        }
        // 查询该电话信息
        QueryWrapper<JcxfPartyMember> queryWrapper = new QueryWrapper();
        queryWrapper.eq("phone", AESUtil2.encrypt(phone));
        queryWrapper.eq("del_flag", 0);
        Integer integer = jcxfPartyMemberMapper.selectCount(queryWrapper);
        if (integer > 1) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "存在多个相同电话账户");
        } else if (integer == 0) {
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "请输入正确的电话号码");
        }
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        //调用发送短信的功能
        try {
            ShortMessageResult shortMessageResult = ShortMessageUtil.sendMessage(phone, "您的验证码是" + code + ",该验证码5分钟内有效，请勿泄露于他人！");
            if (shortMessageResult != null && "200".equals(shortMessageResult.getCode())) {
                //删除之前的验证码
                redisUtils.del("bind:" + phone);
                //存储新的验证码
                redisUtils.set("bind:" + phone, code + "", 5, TimeUnit.MINUTES);
                return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success("验证码发送成功！"));
            } else {
                throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码发送失败！");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "验证码发送失败！");
        }
    }

    /**
     * 功能描述：构建模糊查询
     *
     * @param tzMessage 需要模糊查询的信息
     * @return 返回查询
     */
    public QueryWrapper<TzMessage> LikeAllField(TzMessage tzMessage, SearchVo searchVo) {
        QueryWrapper<TzMessage> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(tzMessage.getMissionName())) {
            queryWrapper.and(i -> i.like("tz_message.mission_name", tzMessage.getMissionName()));
        }
        if (tzMessage.getSendType() != null) {
            queryWrapper.and(i -> i.eq("tz_message.send_type", tzMessage.getSendType()));
        }
        if (tzMessage.getSendTimeType() != null) {
            queryWrapper.and(i -> i.eq("tz_message.send_time_type", tzMessage.getSendTimeType()));
        }
        if (searchVo != null) {
            if (StringUtils.isNotEmpty(searchVo.getStartDate()) && StringUtils.isNotEmpty(searchVo.getEndDate())) {
                queryWrapper.lambda().and(i -> i.between(TzMessage::getCreateTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        if (StringUtils.isNotEmpty(tzMessage.getSendStartTime()) && StringUtils.isNotEmpty(tzMessage.getSendEndTime())) {
            queryWrapper.lambda().and(i -> i.between(TzMessage::getSendTime, tzMessage.getSendStartTime(), tzMessage.getSendEndTime()));
        }
        return queryWrapper;

    }

    public QueryWrapper<TzMessageDetail> LikeAllField2(TzMessageDetail tzMessageDetail, SearchVo searchVo) {
        QueryWrapper<TzMessageDetail> queryWrapper = new QueryWrapper<>();
        if (tzMessageDetail.getMessageId() != null) {
            queryWrapper.and(i -> i.eq("tz_message_detail.message_id", tzMessageDetail.getMessageId()));
        }
        if (StringUtils.isNotBlank(tzMessageDetail.getPhone())) {
            queryWrapper.and(i -> i.like("tz_message_detail.phone", tzMessageDetail.getPhone()));
        }
        if (StringUtils.isNotBlank(tzMessageDetail.getMsgId())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.msg_id", tzMessageDetail.getMsgId()));
        }
        if (StringUtils.isNotBlank(tzMessageDetail.getTemplateId())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.template_id", tzMessageDetail.getTemplateId()));
        }
        if (StringUtils.isNotBlank(tzMessageDetail.getSendStatus())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.send_status", tzMessageDetail.getSendStatus()));
        }
        if (tzMessageDetail.getDeptId() != null) {
            queryWrapper.and(i -> i.eq("tz_message_detail.dept_id", tzMessageDetail.getDeptId()));
        }
        if (StringUtils.isNotBlank(tzMessageDetail.getSendType())) {
            queryWrapper.and(i -> i.eq("tz_message_detail.send_type", tzMessageDetail.getSendType()));
        }
        if (searchVo != null) {
            if (StringUtils.isNotEmpty(searchVo.getStartDate()) && StringUtils.isNotEmpty(searchVo.getEndDate())) {
                queryWrapper.lambda().and(i -> i.between(TzMessageDetail::getPostTime, searchVo.getStartDate(), searchVo.getEndDate()));
            }
        }
        return queryWrapper;

    }
}

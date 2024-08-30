/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.task;

import cn.hutool.core.collection.CollectionUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.model.NotifyLog;
import com.yami.shop.service.NotifyLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author yami
 */
@Component
public class NotifyTask {


    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private NotifyLogService notifyLogService;


    @XxlJob("sendMessage")
    public void sendMessage(){
        logger.info("获取还未进行消息推送的通知。。。");
        // 获取还未进行消息推送的通知
        List<NotifyLog> logList = notifyLogService.listUnSendMsgList();
        if (CollectionUtil.isEmpty(logList)) {
            return;
        }
        // 修改成已发送
        logList.forEach(notifyLog -> notifyLog.setStatus(1));
        notifyLogService.updateBatchById(logList);
        // 推送消息
        notifyLogService.sendMessage(logList);
    }
}

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

import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.util.RedisUtil;
import com.yami.shop.service.FlowLogService;
import com.yami.shop.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author yami
 */
@Component
public class FlowAnalysisTask {


    @Autowired
    private FlowLogService flowLogService;
    @Autowired
    private FlowService flowService;


    /**
     * 根据设置的时间，将缓存中的记录插入到数据库
     */
    @XxlJob("insertFlowAnalysisLog")
    public void insertFlowAnalysisLog(){
        if (RedisUtil.hasKey(Constant.FLOW_ANALYSIS_LOG)){
            flowLogService.insertBatch();
        }
    }

    /**
     * 统计记录数据，储存到对应的数据表中
     */
    @XxlJob("statisticalFlowData")
    public void statisticalFlowData(){
        //更新数据再统计
        insertFlowAnalysisLog();
        flowService.statisticalData();
    }
}

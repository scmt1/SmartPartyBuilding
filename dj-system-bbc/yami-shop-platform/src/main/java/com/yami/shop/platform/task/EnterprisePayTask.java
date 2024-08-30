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

import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.entpay.EntPayQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.model.EnterprisePay;
import com.yami.shop.config.WxConfig;
import com.yami.shop.service.EnterprisePayService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author yami
 */
@Slf4j
@Component("enterprisePay")
@AllArgsConstructor
public class EnterprisePayTask {

    private final EnterprisePayService enterprisePayService;

    private final WxConfig wxConfig;
    /**
     * 企业支付
     */
    @XxlJob("sendEnterprisePay")
    public void sendEnterprisePay() {
        log.info("开始执行发送企业付款任务》》》》》》》》》》》》》》》》》》》》》");

        List<EnterprisePay> enterprisePayList = enterprisePayService.listApplyEnterprisePay();
        enterprisePayService.sendEnterprisePay(enterprisePayList);

        log.info("结束执行发送企业付款任务》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 查询支付情况
     */
    @XxlJob("queryAndUpdateEntPay")
    public void queryAndUpdateEntPay() {
        log.info("开始查询企业付款任务》》》》》》》》》》》》》》》》》》》》》》》");

        List<EnterprisePay> enterprisePayList = enterprisePayService.listApplyEnterprisePay();
        if (CollectionUtils.isNotEmpty(enterprisePayList)) {
            for (EnterprisePay enterprisePay : enterprisePayList) {
                try {
                    EntPayQueryResult entPayQueryResult = wxConfig.getEntPayService().queryEntPay(String.valueOf(enterprisePay.getEntPayOrderNo()));
                    // PROCESSING：处理中
                    if (StrUtil.equalsIgnoreCase(entPayQueryResult.getStatus(), "PROCESSING")) {
                    }
                    // SUCCESS: 转账成功
                    if (StrUtil.equalsIgnoreCase(entPayQueryResult.getStatus(), "SUCCESS")) {
                        enterprisePayService.paySuccess(enterprisePay);
                    }
                    // FAILED: 转账失败
                    if (StrUtil.equalsIgnoreCase(entPayQueryResult.getStatus(), "FAILED")) {
                        enterprisePayService.payFailed(enterprisePay);
                        log.error(entPayQueryResult.getReason());
                    }

                } catch (WxPayException e) {
                    log.error("WxPayException:", e);
                }
            }
        }
        log.info("结束查询企业付款任务》》》》》》》》》》》》》》》》》》》》》》》");
    }

}

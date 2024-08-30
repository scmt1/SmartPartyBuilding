/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.param.CustomerReqParam;
import com.yami.shop.bean.vo.AccountDetailVO;
import com.yami.shop.bean.vo.ShopAccountDetailVO;
import com.yami.shop.bean.vo.ShopAccountVO;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.common.util.PoiExcelUtil;
import com.yami.shop.service.PayInfoService;
import com.yami.shop.service.RefundInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author lhd
 * @date 2021/8/11 15:57
 */
@RestController("platformAccountDetailController")
@RequestMapping("/platform/accountDetail")
@Api(tags = "账户详情")
public class AccountDetailController {

    @Autowired
    private PayInfoService payInfoService;
    @Autowired
    private RefundInfoService refundInfoService;

    @GetMapping("/getIncomeAccountDetail")
    @ApiOperation(value = "获取收入账户详情", notes = "根据时间获取")
    public ServerResponseEntity<AccountDetailVO> getIncomeAccountDetail(CustomerReqParam accountSearchDTO){
        AccountDetailVO accountDetailVO = payInfoService.getIncomeAccountDetail(accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime(), accountSearchDTO.getShopName());
        return ServerResponseEntity.success(accountDetailVO);
    }

    @GetMapping("/getRefundAccountDetail")
    @ApiOperation(value = "获取退款账户详情", notes = "根据时间获取")
    public ServerResponseEntity<AccountDetailVO> getRefundAccountDetail(CustomerReqParam accountSearchDTO){
        AccountDetailVO accountDetailVO = refundInfoService.getRefundAccountDetail(accountSearchDTO.getStartTime(), accountSearchDTO.getEndTime(), accountSearchDTO.getShopName());
        return ServerResponseEntity.success(accountDetailVO);
    }

    @GetMapping("/getIncomeAccountDetailInfo")
    @ApiOperation(value = "分页获取收入账户详情", notes = "分页获取收入账户详情")
    public ServerResponseEntity<IPage<ShopAccountVO>> getIncomeAccountDetailInfo(PageParam<ShopAccountVO> page, CustomerReqParam accountSearchDTO) {
        IPage<ShopAccountVO> accountDetailPage = payInfoService.pageIncomeAccountDetail(page, accountSearchDTO);
        return ServerResponseEntity.success(accountDetailPage);
    }

    @GetMapping("/getRefundAccountDetailInfo")
    @ApiOperation(value = "分页获取退款账户详情", notes = "分页获取退款账户详情")
    public ServerResponseEntity<IPage<ShopAccountVO>> getRefundAccountDetailInfo(PageParam<ShopAccountVO> page, CustomerReqParam accountSearchDTO) {
        IPage<ShopAccountVO> accountDetailPage = refundInfoService.listRefundAccountDetail(page, accountSearchDTO);
        return ServerResponseEntity.success(accountDetailPage);
    }

    @GetMapping("/pageShopIncomeAccountDetail")
    @ApiOperation(value = "分页获取指定店铺的收入结算明细", notes = "根据店铺id，时间获取")
    public ServerResponseEntity<IPage<ShopAccountDetailVO>> pageShopIncomeAccountDetail(PageParam<ShopAccountDetailVO> page, CustomerReqParam customerReqParam) {
        IPage<ShopAccountDetailVO> accountDetailPage = payInfoService.pageShopIncomeAccountDetail(page, customerReqParam);
        return ServerResponseEntity.success(accountDetailPage);
    }

    @GetMapping("/pageShopRefundAccountDetail")
    @ApiOperation(value = "分页获取指定店铺的退款结算明细", notes = "根据店铺id，时间获取")
    public ServerResponseEntity<IPage<ShopAccountDetailVO>> pageShopRefundAccountDetail(PageParam<ShopAccountDetailVO> page, CustomerReqParam customerReqParam) {
        IPage<ShopAccountDetailVO> accountDetailPage = refundInfoService.pageShopRefundAccountDetail(page, customerReqParam);
        return ServerResponseEntity.success(accountDetailPage);
    }

    @GetMapping("/getPayAndRefundInfoForm")
    @ApiOperation(value = "导出报表", notes = "导出收入以及退款报表")
    @PreAuthorize("@pms.hasPermission('pay:refund:excel')")
    public void getPayAndRefundInfoForm(CustomerReqParam customerReqParam, HttpServletResponse response) {
        if (Objects.isNull(customerReqParam.getStartTime()) || Objects.isNull(customerReqParam.getEndTime())) {
            // 请选择导出报表的交易时间
            throw new YamiShopBindException("yami.finance.form.excel");
        }
        List<ShopAccountDetailVO> incomeAccountDetails = payInfoService.listIncomeAccountDetail(customerReqParam);
        List<ShopAccountDetailVO> refundAccountDetails = refundInfoService.listRefundAccountDetail(customerReqParam);
        Map<Integer, List<ShopAccountDetailVO>> shopAccountDetailMap = new HashMap<>(2);
        shopAccountDetailMap.put(0, incomeAccountDetails);
        shopAccountDetailMap.put(1, refundAccountDetails);
        excelMultiSheet(response, shopAccountDetailMap);
    }

    public void excelMultiSheet(HttpServletResponse response, Map<Integer, List<ShopAccountDetailVO>> shopAccountDetailMap) {
        ExcelWriter writer = ExcelUtil.getBigWriter();
        writer.renameSheet(0, I18nMessage.getMessage("yami.pay.info.reportTitle"));
        for (int i = 0; i < shopAccountDetailMap.size(); i++) {
            writer.setSheet(i == 0 ? I18nMessage.getMessage("yami.pay.info.reportTitle") : I18nMessage.getMessage("yami.refund.info.reportTitle"));
            handleSheet(writer);
            List<ShopAccountDetailVO> shopAccountDetails = shopAccountDetailMap.get(i);
            int row = 1;
            for (ShopAccountDetailVO shopAccountDetail : shopAccountDetails) {
                int firstRow = ++row;
                int col = -1;
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, row - 1);
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getShopName());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getPayTime());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getOrderNumber());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getPayNo());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getBizPayNo());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, I18nMessage.getMessage("yami.payType.pay" + shopAccountDetail.getPayType()));
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getAlipayAmount());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getWechatAmount());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getScoreCount());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getBalanceAmount());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getPaypalAmount());
                PoiExcelUtil.mergeIfNeed(writer, firstRow, firstRow, ++col, col, shopAccountDetail.getTotal());
            }
        }
        PoiExcelUtil.writeExcel(response, writer);
    }

    private void handleSheet(ExcelWriter writer) {
        String[] headerCn = {"序号","店铺名称","支付时间","订单编号","支付单号","外部流水号","支付方式","支付宝","微信","支付积分","余额支付","PayPal支付","合计"};
        String[] headerEn = {"Serial Number", "Store Name", "Payment Time", "Order Number", "Payment Order Number", "External Streaming Number", "Payment Method", "Alipay", "WeChat", "Payment Credits", "Balance Payment", "PayPal Payment", "Total"};
        List<String> header = Arrays.asList(Objects.equals(I18nMessage.getDbLang(), 0) ? headerCn : headerEn);
        writer.merge(header.size() - 1, I18nMessage.getMessage("yami.account.info.reportTitle"));
        writer.writeRow(header);
        Sheet sheet = writer.getSheet();
        for (int j = 1; j < header.size() - 1; j++) {
            sheet.setColumnWidth(j, 20 * 256);
        }
    }
}

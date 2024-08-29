/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.common.config;


import cn.hutool.core.util.StrUtil;


import me.flyray.bsin.facade.service.SysConfigService;
import me.flyray.bsin.server.common.bean.*;

import me.flyray.bsin.server.common.constant.PaypalConstant;
import me.flyray.bsin.server.common.exception.YamiShopBindException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 商城配置文件
 * 支付配置、文件上传配置、短信配置、快递配置、小程序配置、微信网页开发配置、公众号配置
 * @author yami
 */
@Component
public class ShopConfig {

    @Autowired
    private SysConfigService sysConfigService;

    public Qiniu getQiniu(){
        // 从数据库 / 缓存中获取到配置文件信息
        return sysConfigService.getSysConfigObject(Constant.QINIU_CONFIG, Qiniu.class);
    }

    public AliOss getAliOss() {
        return sysConfigService.getSysConfigObject(Constant.ALI_OSS_CONFIG, AliOss.class);
    }

    public QCloud getQCloud() {
        return sysConfigService.getSysConfigObject(Constant.Q_CLOUD_CONFIG,QCloud.class);
    }

    public HuaWeiOss getHuaWeiObs() {
        return sysConfigService.getSysConfigObject(Constant.HUAWEI_OBS_CONFIG, HuaWeiOss.class);
    }

    public Minio getMinio() {
        return sysConfigService.getSysConfigObject(Constant.MINIO_OSS_CONFIG, Minio.class);
    }

    public ALiDaYu getAliDaYu(){
        ALiDaYu aLiDaYu = sysConfigService.getSysConfigObject(Constant.ALIDAYU_CONFIG, ALiDaYu.class);
        if (aLiDaYu == null || StrUtil.isBlank(aLiDaYu.getAccessKeyId())) {
            // 无法获取短信配置，无法发送短信
            throw new YamiShopBindException("yami.sys.sms.no.exist");
        }
        return aLiDaYu;
    }


    public QuickBird getQuickBird() {
        return sysConfigService.getSysConfigObject(Constant.QUICKBIRD_CONFIG, QuickBird.class);
    }

    public Quick100 getQuick100() {
        return sysConfigService.getSysConfigObject(Constant.QUICK100_CONFIG, Quick100.class);
    }

    public QuickAli getAliQuick() {
        return sysConfigService.getSysConfigObject(Constant.ALI_QUICK_CONFIG, QuickAli.class);
    }


    public WxMiniApp getWxMiniApp() {
        WxMiniApp wxMiniApp = sysConfigService.getSysConfigObject(Constant.MA_CONFIG, WxMiniApp.class);
        if (wxMiniApp == null || StrUtil.isBlank(wxMiniApp.getAppId())) {
            // 请在后台页面设置微信小程序信息后，再进行操作
            throw new YamiShopBindException("yami.sys.wxMa.no.exist");
        }
        return wxMiniApp;
    }

    public WxMp getWxMp() {
        WxMp wxMp = sysConfigService.getSysConfigObject(Constant.MP_CONFIG, WxMp.class);
        if (wxMp == null || StrUtil.isBlank(wxMp.getAppId())) {
            // 请在后台页面设置微信公众号信息后，再进行操作
            throw new YamiShopBindException("yami.sys.wxMp.no.exist");
        }
        return wxMp;
    }

    public WxApp getWxApp() {
        WxApp wxApp = sysConfigService.getSysConfigObject(Constant.MX_APP_CONFIG, WxApp.class);
        if (wxApp == null || StrUtil.isBlank(wxApp.getAppId())) {
            // 请在后台页面设置微信开放平申请应用相关信息后，再进行操作
            throw new YamiShopBindException("yami.sys.wxApp.no.exist");
        }
        return wxApp;
    }

    public WxPay getWxPay() {
        WxPay wxPay = sysConfigService.getSysConfigObject(Constant.WXPAY_CONFIG, WxPay.class);
        if (wxPay == null || StrUtil.isBlank(wxPay.getMchId())) {
            // 请在后台页面设置微信支付信息后，再进行操作
            throw new YamiShopBindException("yami.sys.wxPay.no.exist");
        }
        return wxPay;
    }

    public Alipay getAlipay() {

        return sysConfigService.getSysConfigObject(Constant.ALIPAY_CONFIG,Alipay.class);
    }

    public Domain getDomain() {
        Domain domain = sysConfigService.getSysConfigObject(Constant.DOMAIN_CONFIG, Domain.class);
        if (domain == null || StrUtil.isBlank(domain.getApiDomainName())) {
            // 请在后台页面设置回调域名后，再进行操作
            throw new YamiShopBindException("yami.sys.wxDomain.no.exist");
        }
        return domain;
    }

    public PayPal getPayPal() {
        PayPal payPal = sysConfigService.getSysConfigObject(Constant.PAYPAL_CONFIG, PayPal.class);
        boolean is = Objects.isNull(payPal)
                || StrUtil.isBlank(payPal.getClientId())
                || StrUtil.isBlank(payPal.getClientSecret());
        if (is) {
            // 请在后台页面设置paypal相关信息后，再进行操作
            throw new YamiShopBindException("yami.sys.paypal.no.exist");
        }
        if (StrUtil.isBlank(payPal.getMode())) {
            payPal.setMode(PaypalConstant.PAYPAL_MODE);
        }
        return payPal;
    }

    public ExchangeRate getExchangeRate() {
        ExchangeRate exchangeRate = sysConfigService.getSysConfigObject(Constant.EXCHANGE_RATE_CONFIG, ExchangeRate.class);
        if (exchangeRate == null) {
            // 请在后台页面设置paypal相关信息后，再进行操作
            throw new YamiShopBindException("yami.sys.paypal.no.exist");
        }
        return exchangeRate;
    }


    private String getEbusinessIdByStr(String paramValue){
        String[] split = paramValue.split(",");
        for (String s : split) {
            if (s.contains("eBusinessID")){
                String[] split1 = s.split(":");
                String str = split1[1];
                String replace = str.replace("\"", "");
                return replace;
            }
        }
        return "";
    }

}

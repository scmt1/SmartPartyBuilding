/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.config;

import com.yami.shop.common.i18n.LanguageEnum;

/**
 * 常量
 * @author yami
 */
public class Constant {

    /** 超级管理员ID */
    public static final int SUPER_ADMIN_ID = 1;

    /**
     * 自营店id
     */
    public static final long MAIN_SHOP = 1L;

    /**
     * 如果把平台的数据也保存在店铺里面，如分类，热搜之类的，保存的店铺id
     */
    public static final long PLATFORM_SHOP_ID = 0L;
    /**
     * 平台条件标签上限
     */
    public static final long TAG_LIMIT_NUM = 20;
    /**
     * 平台店名称
     */
    public static final String PLATFORM_SHOP_NAME = "官方店";

    /**
     * 积分名称
     */
    public static final String SCORE_CONFIG = "SCORE_CONFIG";
    public static final String SCORE_EXPLAIN = "SCORE_EXPLAIN";
    public static final String LEVEL_SHOW = "LEVEL_SHOW";
    public static final String SCORE_EXPIRE = "SCORE_EXPIRE";
    public static final String SCORE_QUESTION = "SCORE_QUESTION";
    /**
     * 商城缺失sku属性时的字符描述
     */
    public static final String SKU_PREFIX = "规格:";

    public static final String DEFAULT_SKU = "规格";
    /**
     * 成长值名称
     */
    public static final String GROWTH_CONFIG = "GROWTH_CONFIG";

    /** 会员初始等级id*/
    public static final int USER_LEVEL_INIT = 1;

    /** 商家线下核销*/
    public static final int SERVICE_TYPE = 1;

    /** 系统菜单最大id */
    public static final int SYS_MENU_MAX_ID = 30;

    /** 订单超时时间 */
    public static final int ORDER_MAX_TIME = 30;

    /** 订单类型：积分订单 */
    public static final int ORDER_TYPE_SCORE = 3;

    /**
     * 最大确认收货退款时间7天
     */
    public static final int MAX_FINALLY_REFUND_TIME = 7;

    /**
     * 退款最长申请时间，当申请时间过了这个时间段之后，会取消退款申请
     */
    public static final int MAX_REFUND_APPLY_TIME = 7;
    /**
     * 离即将退款超时x小时时提醒
     */
    public static final int MAX_REFUND_HOUR = 12;
    /**
     * 直播间置顶上限个数
     */
    public static final int MAX_TOP_NUM = 10;

    /**
     * 分销佣金结算在确认收货后的时间，维权期过后（7+7+1）
     */
    public static final int DISTRIBUTION_SETTLEMENT_TIME = MAX_FINALLY_REFUND_TIME + MAX_REFUND_APPLY_TIME + 1;

    /**
     * 查询订单成功状态
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * 一级分类id
     */
    public static final Long CATEGORY_ID = 0L;

    /**
     * 配置名称
     */
    public static final String ALIPAY_CONFIG = "ALIPAY_CONFIG";
    public static final String WXPAY_CONFIG = "WXPAY_CONFIG";
    public static final String QINIU_CONFIG = "QINIU_CONFIG";
    public static final String ALI_OSS_CONFIG = "ALI_OSS_CONFIG";
    public static final String Q_CLOUD_CONFIG = "Q_CLOUD_CONFIG";
    public static final String MINIO_OSS_CONFIG = "MINIO_OSS_CONFIG";
    public static final String HUAWEI_OBS_CONFIG = "HUAWEI_OBS_CONFIG";
    public static final String QUICKBIRD_CONFIG = "QUICKBIRD_CONFIG";
    public static final String QUICK100_CONFIG = "QUICK100_CONFIG";
    public static final String ALI_QUICK_CONFIG = "ALI_QUICK_CONFIG";
    public static final String MA_CONFIG = "MA_CONFIG";
    public static final String MP_CONFIG = "MP_CONFIG";
    public static final String ALIDAYU_CONFIG = "ALIDAYU_CONFIG";
    public static final String DOMAIN_CONFIG = "DOMAIN_CONFIG";
    public static final String MX_APP_CONFIG = "MX_APP_CONFIG";
    public static final String PAYPAL_CONFIG = "PAYPAL_CONFIG";
    public static final String MERCHANT_REGISTER_PROTOCOL_CN = "MERCHANT_REGISTER_PROTOCOL_CN";
    public static final String MERCHANT_REGISTER_PROTOCOL_EN = "MERCHANT_REGISTER_PROTOCOL_EN";
    public static final String SHOP_PROTOCOL_CN = "SHOP_PROTOCOL_CN";
    public static final String SHOP_PROTOCOL_EN = "SHOP_PROTOCOL_EN";
    public static final String SENSITIVE_WORDS = "SENSITIVE_WORDS";

    /** 汇率配置 */
    public static final String EXCHANGE_RATE_CONFIG = "EXCHANGE_RATE_CONFIG";

    /**
     * 记录缓存名称
     */
    public static final String FLOW_ANALYSIS_LOG = "flowAnalysisLog";

    /**
     * 心跳字符串
     */
    public static final String HEART_BEAT = "HEART_BEAT";


    /**
     * 最大会员等级折扣
     */
    public static final double MAX_LEVEL_DISCOUNT = 10D;

    /**
     * 最小会员等级积分倍率
     */
    public static final double MIN_LEVEL_RATE_SCORE = 1D;
    /**
     * 短信套餐包最大数量
     */
    public static final int MAX_SMS_PACKAGE = 10;
    /**
     * 商品最低金额(非积分商品)
     */
    public static final Double MIN_PRODUCT_AMOUNT = 0.01D;

    /**
     * 店铺最多可以签约的分类数量
     */
    public static final int SIGNING_CATEGORY_LIMIT_NUM = 200;

    /**
     * 店铺签约的品牌数量上限
     */
    public static final int SIGNING_BRAND_LIMIT_NUM = 50;

    /**
     * 店铺可以绑定的银行卡上限
     */
    public static final int SHOP_BANK_CARD_LIMIT_NUM = 5;
    /**
     * 句号（英文符号）
     */
    public static final String PERIOD = ".";
    /**
     * 逗号
     */
    public static final String COMMA = ",";
    /**
     * 中文逗号
     */
    public static final String CN_COMMA = "，";
    /**
     * 冒号
     */
    public static final String COLON = ":";
    /**
     * 零
     */
    public static final Long ZERO_LONG = 0L;
    /**
     * 零
     */
    public static final Double ZERO_DOUBLE = 0D;
    /**
     *
     * 参考CacheKeyPrefix
     * cacheNames 与 key 之间的默认连接字符
     */
    public static final  String UNION = "::";
    /**
     * 默认语言
     */
    public static final Integer DEFAULT_LANG = LanguageEnum.LANGUAGE_ZH_CN.getLang();
    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 数据量大时，系统单次处理数据的数量
     */
    public static final Long MAX_DATA_HANDLE_NUM = 10000L;

    /**
     * 分销相关配置
     */
    public static final String DISTRIBUTION_CONFIG = "DISTRIBUTION_CONFIG";
    public static final String DISTRIBUTION_RECRUIT_CONFIG = "DISTRIBUTION_RECRUIT_CONFIG";
    public static final Double MAX_USER_BALANCE = 999999999.99D;
    public static String DISTRIBUTION_REMARKS = "分销配置";
    public static String DISTRIBUTION_RECRUIT_REMARKS = "分销招募推广配置";
    public static final String PURCHASES_ORDER = "PO";

    /**
     * 签名相关
     */
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";
    /**
     * 系统配置相关
     */
    public static final String PAY_SWITCH_CONFIG = "PAY_SWITCH_CONFIG";

    public static final String SERVICE_SWITCH_CONFIG = "SERVICE_SWITCH_CONFIG";

    public static final String PROD_SWITCH_CONFIG = "PROD_SWITCH_CONFIG";

    /**
     * 店铺最大银行卡数量
     */
    public static final int MAX_SHOP_BANK_CARD = 5;

    /**
     * 使用商品浏览记录进行推荐时，使用的数据数量
     */
    public static final int MAX_PROD_BROWSE_NUM = 50;

    /**
     * 成功状态码
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * xls文件
     */
    public static final String XLS_FILE = "xls";

    /**
     * xlsx文件
     */
    public static final String XLSX_FILE = "xlsx";

    public static final int LAST_FOUR_BY_MOBILE = 4;

    /**
     * 微信最大支付金额
     */
    public static final Double WECHAT_MAX_PAY_AMOUNT = 10000000.00D;

    public static final int MAX_NICK_NAME_LENGTH = 15;

    public static final int EXCEL_BEGIN_ROW = 2;

    /**
     * 统计的最大步骤数
     */
    public static final int MAX_ROUTE_STEP = 10;


    public static final int MAX_MYSQL_STRING_LENGTH = 255;

    /**
     * 秒杀sku缓存key
     */
    public static final String SECKILL_SKU_STOCKS_PREFIX = "SECKILL_SKU_STOCKS_";

    /**
     * 最大轮播图数量
     */
    public static final int MAX_INDEX_IMG_NUM = 10;

    public static final Integer ERROR1 = 475;

    public static final Integer ERROR2 = 476;

    public static final Integer ERROR3 = 477;
}

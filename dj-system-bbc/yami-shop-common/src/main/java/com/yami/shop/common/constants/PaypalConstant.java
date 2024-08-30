/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.constants;


/**
 * paypal支付常量
 * @author yami
 */
public class PaypalConstant {
    /**
     * PayPal 取消支付回调地址
     */
    public static final String PAYPAL_CANCEL_URL = "/notice/pay/paypal/cancel";
    /**
     * PayPal 取消支付回调地址
     */
    public static final String PAYPAL_SUCCESS_URL = "/notice/pay/paypal/success";

    /**
     * 当前货币币种简称, 默认为人名币的币种 EUR CNY USD
     * 默认为美元币种
     */
    public static final String CURRENTCY = "USD";
    /**
     * approval_url 验证url
     */
    public static final String APPROVAL_URL = "approval_url";

    /**
     * approved 获准
     */
    public static final String APPROVED = "approved";
    /**
     * sandbox 沙箱or live生产
     */
    public static final String PAYPAL_MODE = "live";

    public static final String CAPTURE = "CAPTURE";
    /**
     * 该标签将覆盖PayPal网站上PayPal帐户中的公司名称
     */
    public static final String BRANDNAME = "Supernote";
    /**
     * LOGIN。当客户单击PayPal Checkout时，客户将被重定向到页面以登录PayPal并批准付款。
     * BILLING。当客户单击PayPal Checkout时，客户将被重定向到一个页面，以输入信用卡或借记卡以及完成购买所需的其他相关账单信息
     * NO_PREFERENCE。当客户单击“ PayPal Checkout”时，将根据其先前的交互方式将其重定向到页面以登录PayPal并批准付款，或重定向至页面以输入信用卡或借记卡以及完成购买所需的其他相关账单信息使用PayPal。
     * 默认值：NO_PREFERENCE
     */
    public static final String LANDINGPAGE = "NO_PREFERENCE";
    /**
     * CONTINUE。将客户重定向到PayPal付款页面后，将出现“ 继续”按钮。当结帐流程启动时最终金额未知时，请使用此选项，并且您想将客户重定向到商家页面而不处理付款。
     * PAY_NOW。将客户重定向到PayPal付款页面后，出现“ 立即付款”按钮。当启动结帐时知道最终金额并且您要在客户单击“ 立即付款”时立即处理付款时，请使用此选项。
     */
    public static final String USERACTION = "PAY_NOW";
    /**
     * GET_FROM_FILE。使用贝宝网站上客户提供的送货地址。
     * NO_SHIPPING。从PayPal网站编辑送货地址。推荐用于数字商品
     * SET_PROVIDED_ADDRESS。使用商家提供的地址。客户无法在PayPal网站上更改此地址
     */
    public static final String SHIPPINGPREFERENCE = "SET_PROVIDED_ADDRESS";

    public static final String COMPLETED = "COMPLETED";

    /**
     * 交易异常
     */
    public static final String FAILURE = "failure";
    /**
     * 交易成功
     */
    public static final String SUCCESS = "success";

    /**
     * ipn回调。支付成功
     */
    public static final String PAYMENT_STATUS_COMPLETED = "Completed";
    /**
     * ipn回调。退款成功
     */
    public static final String PAYMENT_STATUS_REFUNDED = "Refunded";
    /**
     * ipn回调。待定
     */
    public static final String PAYMENT_STATUS_PENDING = "Pending";

    /**
     * ipn回调，付款因退款或其他类型的冲销而被冲销。资金已从您的帐户余额中删除，并退还给买方
     */
    public static final String PAYMENT_STATUS_REVERSED = "Reversed";

    /**
     *  ipn回调, 撤销已被取消。例如，您赢得了与客户的纠纷，并且撤回的交易资金已退还给您
     */
    public static final String PAYMENT_STATUS_CANCELED_REVERSAL = "Canceled_Reversal";

    /**
     *  ipn回调，付款被拒绝
     */
    public static final String PAYMENT_STATUS_DENIED = "Denied";

    /**
     *  ipn回调， 此授权已过期，无法捕获
     */
    public static final String PAYMENT_STATUS_EXPIRED = "Expired";

    /**
     *  ipn回调，  德国的ELV付款是通过Express Checkout进行的
     */
    public static final String PAYMENT_STATUS_CREATED = "Created";

    /**
     * ipn回调， 付款失败。仅当付款是通过您客户的银行帐户进行的。
     */
    public static final String PAYMENT_STATUS_FAILED = "Failed";

    /**
     *   ipn回调，付款已被接受
     */
    public static final String PAYMENT_STATUS_PROCESSED = "Processed";

    /**
     *   ipn回调，此授权已失效
     */
    public static final String PAYMENT_STATUS_VOIDED = "Voided";

    //订单状态
    /**
     * 1、支付完成;捕获的付款的资金已记入收款人的PayPal帐户
     * 2、退款完成;该交易的资金已记入客户的帐户
     */
    public static final String STATE_COMPLETED = "COMPLETED";
    /**
     * 部分退款；少于所捕获付款金额的金额已部分退还给付款人。
     */
    public static final String STATE_PARTIALLY_REFUNDED = "PARTIALLY_REFUNDED";


    /**
     * 1、支付待定;捕获的付款资金尚未记入收款人的PayPal帐户。有关更多信息请参见status.details。
     * 2、退款待定;有关更多信息，请参见status_details.reason。
     */
    /**
     * 支付待定：
     * capture_status_details
     * reason 枚举
     * 捕获的付款状态为PENDING或DENIED的原因。可能的值为：
     * BUYER_COMPLAINT。付款人与贝宝（PayPal）对此捕获的付款提出了争议。
     * CHARGEBACK。响应于付款人与用于支付此已捕获付款的金融工具的发行人对此已捕获的付款提出异议，已收回的资金被撤回。
     * ECHECK。由尚未结清的电子支票支付的付款人。
     * INTERNATIONAL_WITHDRAWAL。访问您的在线帐户。在您的“帐户概览”中，接受并拒绝此笔付款。
     * OTHER。无法提供其他特定原因。有关此笔付款的更多信息，请在线访问您的帐户或联系PayPal。
     * PENDING_REVIEW。捕获的付款正在等待人工审核。
     *（手动收取）RECEIVING_PREFERENCE_MANDATES_MANUAL_ACTION。收款人尚未为其帐户设置适当的接收首选项。有关如何接受或拒绝此付款的更多信息，请在线访问您的帐户。通常在某些情况下提供此原因，例如，当所捕获付款的货币与收款人的主要持有货币不同时。
     * REFUNDED。收回的资金已退还。
     * TRANSACTION_APPROVED_AWAITING_FUNDING。付款人必须将这笔付款的资金汇出。通常，此代码适用于手动EFT。
     * UNILATERAL。收款人没有PayPal帐户。
     * VERIFICATION_REQUIRED。收款人的PayPal帐户未通过验证。
     */
    /**
     * 退款待定
     * 退款具有“PENDING”或“FAILED”状态的原因。 可能的值为：
     * ECHECK。客户的帐户通过尚未结清的eCheck进行注资。
     */
    public static final String STATE_PENDING = "PENDING";
    /**
     * 退款;大于或等于此捕获的付款金额的金额已退还给付款人
     */
    public static final String STATE_REFUNDED = "REFUNDED";
    /**
     * 支付拒绝
     */
    public static final String STATE_DENIED = "DENIED";
    /**
     * 退款失败
     */
    public static final String STATE_FAILED = "FAILED";

    /**
     * 争议状态
     */
    public static final String BUYER_COMPLAINT = "BUYER_COMPLAINT";

    /**
     * 沙箱环境请求网关地址
     */
    public static final String SANDBOX = "https://api-m.sandbox.paypal.com";
    /**
     * 生产环境请求网关地址
     */
    public static final String LIVE = "https://api.paypal.com";
    /**
     * 添加物流信息请求路径
     */
    public static final String ADD_TRACK_URL = "/v1/shipping/trackers-batch";

    /**
     * 修改物流信息请求路径
     */
    public static final String UPDATE_TRACK_URL = "/v1/shipping/trackers/";

    public static final String REFUNDED = "Refunded";
    public static final String PENDING = "Pending";

    public static final String VERIFIED = "VERIFIED";
    /**
     * 验签地址
     * 沙箱： https://www.sandbox.paypal.com/cgi-bin/webscr
     * 正式： https://www.paypal.com/cgi-bin/webscr
     */
    public final static String PAY_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr";

    public final static String CMD_XCLICK = "_xclick";

    public final static String CMD_NOTIFY_VALIDATE = "_notify-validate";

    /** 生成发票二维码地址 */
    public final static String INVOICES_QR_CODE_URL = "/v2/invoicing/invoices/{invoice_id}/generate-qr-code";
    /** 生成发票二维码 的高度*/
    public final static Integer INVOICES_QR_CODE_HEIGHT = 400;
    /** 生成发票二维码 的宽度*/
    public final static Integer INVOICES_QR_CODE_WIDTH = 400;

    /** payPal网络钩子超出限制 */
    public final static String WEBHOOK_NUMBER_LIMIT_EXCEEDED = "WEBHOOK_NUMBER_LIMIT_EXCEEDED";


}

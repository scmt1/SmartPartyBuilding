package me.flyray.bsin.server.utils.payUtils;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
//import com.jeequan.jeepay.Jeepay;
//import com.jeequan.jeepay.JeepayClient;
//import com.jeequan.jeepay.exception.JeepayException;
//import com.jeequan.jeepay.model.PayOrderCreateReqModel;
//import com.jeequan.jeepay.request.PayOrderCreateRequest;
//import com.jeequan.jeepay.response.PayOrderCreateResponse;
//import com.ruoyi.common.config.JeepayProperties;
//import com.ruoyi.common.core.domain.R;
//import com.ruoyi.common.exception.ServiceException;
//import com.ruoyi.common.utils.ip.INetUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.jeequan.jeepay.Jeepay;
import com.jeequan.jeepay.JeepayClient;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.model.PayOrderCreateReqModel;
import com.jeequan.jeepay.request.PayOrderCreateRequest;
import com.jeequan.jeepay.response.PayOrderCreateResponse;
import com.jeequan.jeepay.util.JeepayKit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.flyray.bsin.server.common.ResponseCode;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.domain.bsin.BsinSysTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.util.Date;
import java.util.HashMap;


/**
 * @author zdj
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class JeepayHelper {
    /* public static String mchNo;
     public static String appId;
     public static String apiKey;
     public static String notifyUrl;
     public static String returnUrl;
     @Value("${jeepay.mchNo}")
     public void setMchNo(String mchNo) {
         this.mchNo = mchNo;
     }

     @Value("${jeepay.appId}")
     public void setAppId(String appId) {
         this.appId = appId;
     }

     @Value("${jeepay.apiKey}")
     public void setApiKey(String apiKey) {
         this.apiKey = apiKey;
     }

     @Value("${jeepay.notifyUrl}")
     public void setNotifyUrl(String notifyUrl) {
         this.notifyUrl = notifyUrl;
     }

     @Value("${jeepay.returnUrl}")
     public void setReturnUrl(String returnUrl) {
         this.returnUrl = returnUrl;
     }*/
    @Autowired
    private JeepayProperties jeepayProperties;

    @Autowired
    private static JeepayProperties staticJeepayProperties;

    @PostConstruct
    public void init() {
       /* jeepayProperties.setMchNo(mchNo);
        jeepayProperties.setAppId(appId);
        jeepayProperties.setApiKey(apiKey);
        jeepayProperties.setNotifyUrl(notifyUrl);
        jeepayProperties.setReturnUrl(returnUrl);*/
        staticJeepayProperties = jeepayProperties;
    }

    public static String getJeepayUrl(String orderNo, String body, String amount, String notifyUrl, String returnUrl, String extParam,BsinSysTenant bsinSysTenant) throws UnknownHostException {

        if(bsinSysTenant == null || StringUtils.isBlank(bsinSysTenant.getMchNo())
                || StringUtils.isBlank(bsinSysTenant.getMchAppId())
                || StringUtils.isBlank(bsinSysTenant.getApiKey())){
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "无支付商户信息，暂无法支付");
        }
        HashMap<String, Object> params = new HashMap<>();
        //商户号
        params.put("mchNo", bsinSysTenant.getMchNo());
        //应用id
        params.put("appId", bsinSysTenant.getMchAppId());
        //应用key
        params.put("apiKey", bsinSysTenant.getApiKey());
        //订单编号
        params.put("orderNo", orderNo);
        //ip
        //params.put("clientIp", INetUtil.getHostIp());
        params.put("clientIp", "192.168.3.115");
        //支付信息，如商品名称
        params.put("body", body);
        //通知地址
        params.put("notifyUrl", staticJeepayProperties.getNotifyUrl() + notifyUrl);
        //付款金额(元)
        params.put("amount", amount);
        //返回地址
        params.put("returnUrl", staticJeepayProperties.getReturnUrl() + returnUrl);
        //订单失效时间 单位秒
        params.put("expiredTime", 120);

        //其他参数
        params.put("extParam", extParam);

        String url = staticJeepayProperties.getJeepayURL();
        String result = HttpRequest.post(url)
                .header(Header.ACCEPT, "application/json;charset=utf-8")
                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.toJsonStr(params))
                .timeout(20000)//超时，毫秒
                .execute().body();

        if (result.indexOf("<!DOCTYPE html>") >= 0) {
            return result;
        } else {
            R<String> res = JSONUtil.toBean(result, R.class);
            throw new ServiceException(res.getMsg());
        }
    }

    public static JeepayResult payQuery(String payOrderId,BsinSysTenant bsinSysTenant) throws Exception{
        if(bsinSysTenant == null || StringUtils.isBlank(bsinSysTenant.getMchNo())
                || StringUtils.isBlank(bsinSysTenant.getMchAppId())
                || StringUtils.isBlank(bsinSysTenant.getApiKey())){
            throw new BusinessException(String.valueOf(ResponseCode.FAIL), "无支付商户信息，暂无法查询支付");
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("mchNo", bsinSysTenant.getMchNo());
        params.put("appId", bsinSysTenant.getMchAppId());
        params.put("mchOrderNo", payOrderId);
        // params.put("payOrderId", );
        params.put("reqTime", new Date().getTime());
        params.put("version", "1.0");
        params.put("signType", "MD5");


        HashMap<String, Object> signParams = new HashMap<>();
        signParams.put("version", "1.0");
        signParams.put("signType", "MD5");

        int requestTime = (int) (System.currentTimeMillis() / 1000);
        String reqTime = Integer.toString(requestTime);

        signParams.put("reqTime", reqTime);


        String signature = JeepayKit.getSign(params, bsinSysTenant.getApiKey());
        params.put("sign", signature);
        String url = staticJeepayProperties.getQueryURL();
        String result = HttpRequest.post(url)
                .header(Header.ACCEPT, "application/json;charset=utf-8")
                .header(Header.CONTENT_TYPE, "application/json")//头信息，多个头信息多次调用此方法即可
                .body(JSONUtil.toJsonStr(params))
                .timeout(20000)//超时，毫秒
                .execute().body();
        JeepayResult jeepayResult = JSON.parseObject(result, JeepayResult.class);
        return jeepayResult;
    }

    public static JSONObject getJeepayUrlWeiXin(String openId, String orderNo, String body, String amount, String notifyUrl,BsinSysTenant bsinSysTenant) throws UnknownHostException, JeepayException {
        // 创建客户端
        JeepayClient jeepayClient = JeepayClient.getInstance(bsinSysTenant.getMchAppId(), bsinSysTenant.getApiKey());
        // 构建请求数据
        PayOrderCreateRequest request = new PayOrderCreateRequest();
        PayOrderCreateReqModel model = new PayOrderCreateReqModel();
        // 商户号
        model.setMchNo(bsinSysTenant.getMchNo());
        // 应用ID
        model.setAppId(bsinSysTenant.getMchAppId());
        // 商户订单号
        model.setMchOrderNo(orderNo);
        //金额，单位分
        model.setAmount(Long.parseLong(convertDollar2Cent(String.valueOf(amount))));
        // 币种，目前只支持cny
        model.setCurrency("CNY");
        // 发起支付请求客户端的IP地址
        model.setClientIp("10.46.100.245");
        // 商品描述
        model.setBody(body);
        // 商品标题
        model.setSubject(body);
        // 异步通知地址
        model.setNotifyUrl(staticJeepayProperties.getNotifyUrl()+notifyUrl);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("openid", openId);
        model.setChannelExtra(JSONUtil.toJsonStr(hashMap));
        model.setWayCode("WX_JSAPI");
        request.setBizModel(model);
        //支付接口URL
        jeepayClient.setApiBase(staticJeepayProperties.getJeepayURLWeiXin());
        PayOrderCreateResponse response = jeepayClient.execute(request);
        JSONObject payData = response.getData().getJSONObject("payData");
        return payData;
    }


    /**
     * 元转分
     *
     * @param str
     * @return
     */
    private static String convertDollar2Cent(String str) {
        DecimalFormat df = new DecimalFormat("0.00");
        StringBuffer sb = df.format(Double.parseDouble(str),
                new StringBuffer(), new FieldPosition(0));
        int idx = sb.toString().indexOf(".");
        sb.deleteCharAt(idx);
        for (; sb.length() != 1; ) {
            if (sb.charAt(0) == '0') {
                sb.deleteCharAt(0);
            } else {
                break;
            }
        }
        return sb.toString();
    }


}

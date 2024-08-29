package me.flyray.bsin.server.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ycy
 */
@Component
@Slf4j
public class ShortMessageUtil2 {
    /**
     * 通知短信 未缴费人员
     * @return
     */
    public static ShortMessageResult2 sendToEndNotice(String phone){
        HttpClient httpClient = null;
        HttpPost httppost = null;
        try{
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost();

            Map map = new HashMap();
            String username = "szjchy";
            String pwd = "qe23g821";
            //此处加密后加盐再进行加密
            Long tKey = System.currentTimeMillis()/1000;
            String password = md5(md5(pwd)+tKey);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("tKey", tKey+"");
            jsonObject.put("signature", "【泸州置业补助平台】");
            jsonObject.put("tpId", "28652");
            jsonObject.put("ext", "");
            jsonObject.put("extend", "stjt_sms_0011");

            JSONArray records = new JSONArray();
            JSONObject record = new JSONObject();
            record.put("mobile", phone);
            records.add(record);
            jsonObject.put("records", records);
            String json = jsonObject.toJSONString();
            StringEntity formEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httppost.setEntity(formEntity);
            HttpResponse httpresponse = null;
            httpresponse = httpClient.execute(httppost);
            HttpEntity httpEntity = httpresponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "utf-8");
            ShortMessageResult2 shortMessageResult = JSONArray.parseObject(response, ShortMessageResult2.class);
            return shortMessageResult;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            if(httppost!=null){
                httppost.abort();
            }
            if(httpClient!=null){
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    private static String md5(String param) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5Byte = md5.digest(param.getBytes("utf8"));
        String result = byteToHex(md5Byte);
        return result;
    }

    private static String byteToHex(byte[] md5Byte) {
        String result = "";
        StringBuilder sb = new StringBuilder();
        for (byte each : md5Byte) {
            int value = each & 0xff;
            String hex = Integer.toHexString(value);
            if (value < 16) {
                sb.append("0");
            }
            sb.append(hex);
        }
        result = sb.toString();
        return result;
    }


    public static int byte4ToInteger(byte[] b, int offset) {
        return (0xff & b[offset]) << 24 | (0xff & b[offset + 1]) << 16 |
                (0xff & b[offset + 2]) << 8 | (0xff & b[offset + 3]);
    }

    /**
     * 发送模板消息
     *
     * @param phone
     * @return
     */
    public static ShortMessageResult2 sendTemplateMessage(String url, String phone, String content) {
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            String username = "szjchy";
            String pwd = "qe23g821";
            //此处加密后加盐再进行加密
            Long tKey = System.currentTimeMillis() / 1000;
            String password = md5(md5(pwd) + tKey);
            JSONObject json = new JSONObject();
            //账号
            json.put("username", username);
            //密码
            json.put("password", password);
            //tKey
            json.put("tKey", tKey + "");
            //手机号
            json.put("mobile", phone);

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("code","1234");
            String template = content;
            String str =  TemplateUtil.reString(template,map);
            //内容
            json.put("content", "【泸州市不动产】" + str);
            String result = HttpRequest.post(url)
                    .timeout(60000)
                    .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .execute()
                    .body();
            ShortMessageResult2 shortMessageResult = JSONArray.parseObject(result, ShortMessageResult2.class);
            return shortMessageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * 发送短信
     *
     * @return
     */
    public static ShortMessageResult2 sendMessage(String url, String phone, String content) {
        HttpClient httpClient = null;
        try {
            httpClient = new DefaultHttpClient();
            String username = "szjchy";
            String pwd = "qe23g821";
            //此处加密后加盐再进行加密
            Long tKey = System.currentTimeMillis() / 1000;
            String password = md5(md5(pwd) + tKey);
            JSONObject json = new JSONObject();
            //账号
            json.put("username", username);
            //密码
            json.put("password", password);
            //tKey
            json.put("tKey", tKey + "");
            //手机号
            json.put("mobile", phone);
            //内容
            json.put("content", "【泸州市不动产】" + content);
            String result = HttpRequest.post(url)
                    .timeout(60000)
                    .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .execute()
                    .body();
            ShortMessageResult2 shortMessageResult = JSONArray.parseObject(result, ShortMessageResult2.class);
            return shortMessageResult;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }
}

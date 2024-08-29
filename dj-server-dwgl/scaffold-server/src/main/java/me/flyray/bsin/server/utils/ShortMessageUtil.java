package me.flyray.bsin.server.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.flyray.bsin.server.domain.MessageDto;
import me.flyray.bsin.server.domain.TzMessageAuto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ycy
 */
@Component
@Slf4j
public class ShortMessageUtil {

    private static boolean sendMsg;

    private static String apiUrl;

    /**
     * 定时发送
     *
     * @param phone
     * @param content
     * @param sign
     * @param sendTime
     * @return
     */
    public static ShortMessageResult sendMessageBySignByTime(String phone, String content, String sign, Date sendTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sendMsg) {
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
                json.put("content", sign + content);
                json.put("time", sdf.format(sendTime));
                String result = HttpRequest.post(apiUrl + "/v2/sendSms")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }

    /**
     * 取消定时发送
     *
     * @param msgIds
     * @return
     */
    public static ShortMessageResult smsCancelTiming(String[] msgIds) {
        if (sendMsg) {
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
                //msgId集合
                json.put("msgIds", msgIds);
                String result = HttpRequest.post(apiUrl + "/v2/smsCancelTiming")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }


    @Value("${message.sendMsg}")
    public void setSendMsg(boolean sendMsg) {
        this.sendMsg = sendMsg;
    }

    @Value("${message.apiUrl}")
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * 发送验证码
     *
     * @param vCode
     * @param phone
     * @return
     */
    public static ShortMessageResult sendVCode(String vCode, String phone) {
        if (sendMsg) {
            HttpClient httpClient = null;
            HttpPost httppost = null;
            try {
                httpClient = new DefaultHttpClient();
                httppost = new HttpPost(apiUrl + "/v2/sendSmsTp");
                String username = "szjchy";
                String pwd = "qe23g821";
                //此处加密后加盐再进行加密
                Long tKey = System.currentTimeMillis() / 1000;
                String password = md5(md5(pwd) + tKey);
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("username", username);
                jsonObject.put("password", password);
                jsonObject.put("tKey", tKey + "");
                jsonObject.put("signature", "【酒城机关党建】");
                jsonObject.put("tpId", "70090");
                jsonObject.put("ext", "");
                jsonObject.put("extend", "stjt_sms_0011");

                JSONArray records = new JSONArray();
                JSONObject record = new JSONObject();
                JSONObject tpContent = new JSONObject();
                tpContent.put("valid_code", vCode);
                record.put("mobile", phone);
                record.put("tpContent", tpContent);
                records.add(record);
                jsonObject.put("records", records);
                String json = jsonObject.toJSONString();
                StringEntity formEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
                httppost.setEntity(formEntity);
                HttpResponse httpresponse = null;
                httpresponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpresponse.getEntity();
                String response = EntityUtils.toString(httpEntity, "utf-8");
                ShortMessageResult shortMessageResult = JSONArray.parseObject(response, ShortMessageResult.class);
                return shortMessageResult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httppost != null) {
                    httppost.abort();
                }
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return null;
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
     * 发送自定义短信
     *
     * @return
     */
    public static ShortMessageResult sendMessage(String phone, String content) {
        if (sendMsg) {
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
                json.put("content", "【酒城机关党建】" + content);
                String result = HttpRequest.post(apiUrl + "/v2/sendSms")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }


    /**
     * 发送自定义短信(自定义签名)
     *
     * @return
     */
    public static ShortMessageResult sendMessageBySign(String phone, String content, String sign) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sendMsg) {
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
                json.put("content", sign + content);
                String result = HttpRequest.post(apiUrl + "/v2/sendSms")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }

    /**
     * 模板短信发送
     *
     * @return
     */
    public static ShortMessageResult sendMessageByTpId(String tpId, List<MessageDto> records, String sign) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sendMsg) {
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
                json.put("tKey", tKey);
                //模板id
                json.put("tpId", Long.valueOf(tpId));
                //签名
                json.put("signature", sign);

                json.put("ext", "");

                json.put("extend", "");

                JSONArray jsonArray = new JSONArray();


                for (MessageDto messageDto : records) {
                    JSONObject record = new JSONObject();
                    record.put("mobile", messageDto.getMobile());
                    record.put("tpContent", messageDto.getTpContent());
                    jsonArray.add(record);
                }

                json.put("records", jsonArray);

                String result = HttpRequest.post(apiUrl + "/v2/sendSmsTp")
                        .timeout(60000)
                        .body(json.toString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }

    /**
     * 模板定时发送
     *
     * @param sign
     * @param sendTime
     * @return
     */
    public static ShortMessageResult sendMessageByTpIdByTime(String tpId, List<MessageDto> records, String sign, Date sendTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (sendMsg) {
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
                json.put("tKey", tKey);
                //模板id
                json.put("tpId", Long.valueOf(tpId));
                //签名
                json.put("signature", sign);

                json.put("ext", "");

                json.put("extend", "");

                JSONArray jsonArray = new JSONArray();

                JSONObject record = new JSONObject();

                for (MessageDto messageDto : records) {
                    record.put("mobile", messageDto.getMobile());
                    record.put("tpContent", messageDto.getTpContent());
                }
                jsonArray.add(record);
                json.put("records", jsonArray);

                json.put("time", sdf.format(sendTime));
                String result = HttpRequest.post(apiUrl + "/v2/sendSmsTp")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }


    /**
     * 发送自定义短信
     *
     * @return
     */
    public static ShortMessageResult queryMessageTemplate(Integer status, Integer page, Integer size) {
        if (sendMsg) {
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
                //审核状态 （1.待审核 2.审核通过 3.审核不通过）
                if (status == null) {
                    status = 2;
                }
                json.put("status", status);
                //内容
                if (page == null) {
                    page = 1;
                }
                if (size == null) {
                    size = 10;
                }
                json.put("page", page);
                json.put("size", size);
                String result = HttpRequest.post(apiUrl + "/sms/v2/template/list")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                ShortMessageResult shortMessageResult = JSONArray.parseObject(result, ShortMessageResult.class);
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
        return null;
    }

    /**
     * 发送自定义短信(自定义签名)
     *
     * @return
     */
    public static TzMessageAuto queryTemplateById(String temId) {
        if (sendMsg) {
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
                //模板id
                json.put("temId", temId);

                String result = HttpRequest.post(apiUrl + "/sms/v2/template/query")
                        .timeout(60000)
                        .body(json.toJSONString(), MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .execute()
                        .body();
                TzMessageAuto tzMessageAuto = JSONArray.parseObject(result, TzMessageAuto.class);
                return tzMessageAuto;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpClient != null) {
                    httpClient.getConnectionManager().shutdown();
                }
            }
        }
        return null;
    }

}

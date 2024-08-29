package me.flyray.bsin.server.utils;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Component
@Intercepts({ @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = { Statement.class }) })
public class ResultSetInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 取出查询的结果
        Object resultObject = invocation.proceed();
        if (Objects.isNull(resultObject)) {
            return null;
        }
        // 基于selectList
        if (resultObject instanceof ArrayList) {
            ArrayList resultList = (ArrayList) resultObject;
            if (!CollectionUtils.isEmpty(resultList)) {
                for (Object result : resultList) {
                    // 逐一解密
                    if(!Objects.isNull(result)){
                        this.decrypt(result);
                    }
                }
            }
            // 基于selectOne
        } else {
            this.decrypt(resultObject);
        }
        return resultObject;
    }

    public <T> T decrypt(T result) throws IllegalAccessException {
        // 取出resultType的类
        Class<?> resultClass = result.getClass();
        Field[] declaredFields = resultClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 取出所有被DecryptTransaction注解的字段
            DecryptTransaction decryptTransaction = field.getAnnotation(DecryptTransaction.class);
            if (!Objects.isNull(decryptTransaction)) {
                field.setAccessible(true);
                Object object = field.get(result);
                // String的解密
                if (object instanceof String) {
                    String value = (String) object;
                    // 对注解的字段进行逐一解密
                    try {
//                        System.out.println("解密："+AESUtil2.decrypt(value));
                        if("idcard".equals(field.getName())){
                            String idCard = AESUtil2.decrypt(value);
                            idCard = idCard.substring(0, 3) + "*************" + idCard.substring(12);
                            field.set(result, idCard);
                        }else if("phone".equals(field.getName())){
                            String phone = AESUtil2.decrypt(value);
                            phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            field.set(result, phone);
                        }else{
                            field.set(result, AESUtil2.decrypt(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

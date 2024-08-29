package me.flyray.bsin.server.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Component
@Intercepts({ @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)})
public class ParameterInterceptor implements Interceptor {

    // 更新时的参数名称，ParamMap的key值
    private static final String CRYPT = "et";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget()
        // 便是parameterHandler
        // 若指定ResultSetHandler ，这里则能强转为ResultSetHandler
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        // 取出实例
        Object parameterObject = parameterField.get(parameterHandler);
        if (parameterObject != null) {
            Object sensitiveObject = null;
            if (parameterObject instanceof MapperMethod.ParamMap) {
                // 更新操作被拦截
                Map paramMap = (Map) parameterObject;
                if (paramMap.containsKey(CRYPT)) {
                    sensitiveObject = paramMap.get(CRYPT);
                }
                // 插入操作被拦截，parameterObject即为待插入的实体对象
            } else {
                // 插入操作被拦截，parameterObject即为待插入的实体对象
                sensitiveObject = parameterObject;
            }
            // 获取不到数据就直接放行
            if (Objects.isNull(sensitiveObject)) {
                return invocation.proceed();
            }
            // 校验该实例的类是否被@EncryptTransaction所注解
            Class<?> sensitiveObjectClass = sensitiveObject.getClass();
            Field[] declaredFields = sensitiveObjectClass.getDeclaredFields();
            encrypt(declaredFields, sensitiveObject);
        }
        return invocation.proceed();
    }

    public <T> T encrypt(Field[] declaredFields, T paramsObject) throws IllegalAccessException {
        for (Field field : declaredFields) {
            // 取出所有被EncryptTransaction注解的字段
            EncryptTransaction encryptTransaction = field.getAnnotation(EncryptTransaction.class);
            if (!Objects.isNull(encryptTransaction)) {
                field.setAccessible(true);
                Object object = field.get(paramsObject);
                // 暂时只实现String类型的加密
                if (object instanceof String) {
                    String value = (String) object;
                    // 加密
                    try {
                        if(StringUtils.isNotBlank(value)){
                            field.set(paramsObject, AESUtil2.encrypt(value));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return paramsObject;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

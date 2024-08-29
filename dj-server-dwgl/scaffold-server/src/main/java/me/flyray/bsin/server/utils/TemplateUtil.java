package me.flyray.bsin.server.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
public class TemplateUtil {

    /**
     *
     * 写一个简单的 解析 方法
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     **/
    public static String  reString ( String model , Map< String, Object > map ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        model = model.replace("}", "} ");
        List< String > objModel = getModelViewFile(model);
        for( String model_obj  : objModel ) {
            String patch = model_obj.replace("{", "").replace("}", "");
            String obj_string = getObjString( map.get( patch.split("\\.")[0] ),  StringUtils.join( "." , removeListThis( patch.split("\\.") ) ) );
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;
            try{
                model = model.replace( "#$" + model_obj +" ", obj_string == null ? "" : sdf.format(new Date(Long.valueOf(obj_string) * 1000)) );
            }
            catch(Exception ex){
            }
            model = model.replace( "$" + model_obj +" ", obj_string == null ? "" : obj_string );
        }
        return model;
    }

    public static List<String> getModelViewFile(String s) {
        List<String> PString = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\$(\\S)*");
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String ss = matcher.group();
            String[] res = ss.split("\\$");
            String a = res[1];
            String b = a.split("\"")[0];
            b = b.split(" ")[0];
            b = b.split(">")[0];
            b = b.split(",")[0];
            PString.add(b);
        }
        return PString;
    }

    private static String getObjString( Object obj ,String objStr ) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
        if( obj == null ){
            return null;
        }
        //如果是字符串类型，则直接返回
        if( obj instanceof String ) {
            return (String) obj;
        }
        if( obj instanceof Map<?,?> ) {
            Map< ? , ? > map = (Map<?,?>)obj;
            return "" + map.get( objStr );
        }
        String objs [] = objStr.split("\\.");
        Object object__ = getValue( obj , objs[0] );
        String [] objs__ = removeListThis(objs);
        String objStr__ = join(".", objs__);
        if( objs.length == 1  ) {
            return object__.toString();
        }
        return getObjString( object__ , objStr__ );//递归
    }

    private static String [] removeListThis ( String [] objs ) {
        if( objs .length == 1 ) {
            return objs ;
        }
        else {
            String [] objs__ = new String [ objs.length -1 ];
            for( int i = 0 ; i< objs.length-1 ; i++ ) {
                objs__ [i] = objs [ i+1 ];
            }
            return objs__;
        }
    }

    /**
     * 连接字符串
     * @param joinAt 分隔符
     * @param str
     * @return
     */
    public static String join(  String joinAt,String[] str  ){
        String response = "";
        boolean first = true;
        for(String s : str ){
            if( first ){
                response += s;
                first = false;
            }else{
                response += ( joinAt + s );
            }
        }
        return response;
    }

    private static Object getValue( Object obj , String filedName ) throws IllegalArgumentException, InvocationTargetException, IllegalAccessException, IntrospectionException {
        PropertyDescriptor descriptor = new PropertyDescriptor(filedName, obj.getClass());
        return descriptor.getReadMethod().invoke(obj);
    }
}

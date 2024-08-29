package me.flyray.bsin.server.utils;

import cn.hutool.core.bean.BeanUtil;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：bolei
 * @date ：Created in 2022/1/14 18:12
 * @description：返回结果处理
 * @modified By：
 */

public class RespBodyHandler {

    public static Map<String, Object> RespBodyDto(){
        Map<String, Object> map = new HashMap<>();
        map.put("data","");
        return map;
    }


    public static Map<String, Object> setRespBodyDto(Object object) {
        Map<String, Object> map = new HashMap<>();
        if (null == object) {
            return new HashMap(16);
        } else {
            map.put("data", BeanUtil.beanToMap(object));
            return map;
        }
    }


    public static Map<String, Object> setRespPageInfoBodyDto(PageInfo pageInfo) {
        Pagination pagination = new Pagination();
        pagination.setPageNum(pageInfo.getPageNum());
        pagination.setPageSize(pageInfo.getPageSize());
        pagination.setTotalSize(pageInfo.getTotal());
        pagination.setTotalPages(pageInfo.getPages());
        Map<String, Object> map = new HashMap<>();
        map.put("data",pageInfo.getList());
        map.put("pagination",pagination);
        return map;
    }


    public static Map<String, Object> setRespBodyListDto(List<?> list) {
        Map<String, Object> map = new HashMap<>();
//        int j=1;
//        for (int i = 0; i < list.size(); i++) {
//            String jsonStr = JSON.toJSONString(list.get(i));
//            map.put("param"+(j++),JSONObject.parseObject(jsonStr,Map.class)) ;
//        }
        map.put("data",list);
        return map;
    }


    public static Map<String, Object> setRespBodySet(Set<?> set) {
        Map<String, Object> map = new HashMap<>();
//        int j=1;
//        for (int i = 0; i < list.size(); i++) {
//            String jsonStr = JSON.toJSONString(list.get(i));
//            map.put("param"+(j++),JSONObject.parseObject(jsonStr,Map.class)) ;
//        }
        map.put("data",set);
        return map;
    }

}

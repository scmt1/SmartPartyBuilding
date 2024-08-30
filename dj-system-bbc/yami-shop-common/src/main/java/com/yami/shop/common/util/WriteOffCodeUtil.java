/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * 获取核销码的工具方法
 * @author lhd
 * @date 2021/10/26
 */
@Component
public class WriteOffCodeUtil {

    private static final Integer LENGTH = 8;
    private static final Integer ROUND_LENGTH = (LENGTH-2)/2;
    private static final Long INIT_ROUND = 10000000000L;

    /**
     *  随机码： 三位随机数 + （月+日+45） + 三位随机数
     *  若随机码不够用，可更改长度：LENGTH 和 初始值：INIT_ROUND，增加随机码
     * @param oldCode
     * @return
     */
    public static String getCode(String oldCode){
        Calendar cal = Calendar.getInstance();
        int dayAddMonth = (cal.get(Calendar.DATE)) + cal.get(Calendar.MONTH) + 46;
        String code;
        Long roundNum;
        if (StrUtil.isNotBlank(oldCode)){
            //随机数增加的最大值
            long changeNum = 5L;
            // 获取随机数
            long num = Long.parseLong(oldCode.substring(0,ROUND_LENGTH) + oldCode.substring(LENGTH - ROUND_LENGTH));
            roundNum = num + Math.round(Math.random() * changeNum) + 1;
        }else {
            // 随机数 = 5*1000000000011以内的随机数 + 2*1000000000011
            roundNum = Math.round(Math.random() * 5 * INIT_ROUND) + 2 * INIT_ROUND;
        }
        // 随机数大于等于最大值，初始化
        long maxRoundNum= 10L * INIT_ROUND;
        if (roundNum >= maxRoundNum){
            roundNum =  roundNum - maxRoundNum + INIT_ROUND;
        }
        code = roundNum.toString();
        return code.substring(0,ROUND_LENGTH)+ dayAddMonth + code.substring(ROUND_LENGTH);
    }
}

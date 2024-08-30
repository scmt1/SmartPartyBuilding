/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.Data;

import java.util.List;

/**
 * 用户导入后业务返回参数
 * @author: cl
 * @date: 2021-04-20 14:20:45
 */
@Data
public class UserExcelParam {

    /** 第几行数据 */
    private Integer row;

    /** 返回的信息 */
    private String msg;

    /** 总条数 */
    private Integer total;

    /** 成功导入的条数 */
    private Integer successNum;

    /** 错误数据的条数 */
    private Integer errorNum;

    /** excel中数据格式不正确的信息*/
    private List<String> errorRowInfos;

    /** 是否导入成功  是true 否false */
    private Boolean success;

    /** json 参数，数据传递用 */
    private String param;

    /** 是否立即将数据返回给前端 是true 否false */
    private Boolean immediately;
}

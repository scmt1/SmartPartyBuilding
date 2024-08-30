/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author YXF
 * @date 2021/9/24
 */
@Data
public class ExcelInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成功信息")
    private String successMsg;

    @ApiModelProperty("失败信息")
    private String errorMsg;

    @ApiModelProperty("响应数据")
    private String data;
}

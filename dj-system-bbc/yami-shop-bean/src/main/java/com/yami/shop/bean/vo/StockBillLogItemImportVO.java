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

import com.yami.shop.bean.model.StockBillLogItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author lth
 * @Date 2021/9/13 16:35
 */
@Data
public class StockBillLogItemImportVO {

    @ApiModelProperty("成功解析的数据项")
    private List<StockBillLogItem> stockBillLogItemList;

    @ApiModelProperty("导入结果提示")
    private String tips;
}

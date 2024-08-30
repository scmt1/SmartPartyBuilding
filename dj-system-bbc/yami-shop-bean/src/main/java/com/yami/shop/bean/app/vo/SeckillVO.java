/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.yami.shop.bean.app.dto.SeckillSkuDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 秒杀信息
 *
 * @author LGH
 * @date 2019-08-28 09:36:59
 */
@Data
public class SeckillVO{

    @TableId
    @ApiModelProperty(value = "秒杀活动id", required = true)
    private Long seckillId;

    @ApiModelProperty(value = "商品id", required = true)
    private Long prodId;

    @NotNull
    @ApiModelProperty(value = "活动名称", required = true)
    private String seckillName;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;

    @ApiModelProperty(value = "活动标签", required = true)
    private String seckillTag;

    @ApiModelProperty(value = "限购数量", required = true)
    private Integer maxNum;

    @ApiModelProperty(value = "取消订单时间", required = true)
    private Integer maxCancelTime;

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "秒杀活动剩余库存", required = true)
    private Integer seckillTotalStocks;

    @ApiModelProperty(value = "秒杀活动原始库存", required = true)
    private Integer seckillOriginStocks;

    @ApiModelProperty(value = "秒杀活动最低价", required = true)
    private Double seckillPrice;

    @ApiModelProperty(value = "秒杀sku列表")
    private List<SeckillSkuDto> seckillSkuList;

    @ApiModelProperty(value = "状态：0 失效 1正常", required = true)
    private Integer status;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("语言")
    private Integer lang;
}

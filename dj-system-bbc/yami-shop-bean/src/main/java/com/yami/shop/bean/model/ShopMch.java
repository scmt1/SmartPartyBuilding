package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_shop_mch")
public class ShopMch implements Serializable {
    @TableId
    @ApiModelProperty("id")
    private Long mchId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("商户号")
    private String mchNo;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("应用key")
    private String apiKey;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}

/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_refund_addr")
public class RefundAddr implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("商家退货地址id")
    private Long refundAddrId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("收货人名字")
    private String receiverName;

    @ApiModelProperty("收货人手机号码")
    private String receiverMobile;

    @ApiModelProperty("收货人座机")
    private String receiverTelephone;

    @ApiModelProperty("邮政编码")
    private String postCode;

    @ApiModelProperty("省id")
    private Long provinceId;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("城市id")
    private Long cityId;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("区id")
    private Long areaId;

    @ApiModelProperty("区")
    private String area;

    @ApiModelProperty("具体地址")
    private String addr;

    @ApiModelProperty("默认地址(0:不是 1:是)")
    private Integer defaultAddr;

    @ApiModelProperty("状态(1:可用 0:不可用 -1:已删除)")
    private Integer status;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("创建时间")
    private Date createTime;

}

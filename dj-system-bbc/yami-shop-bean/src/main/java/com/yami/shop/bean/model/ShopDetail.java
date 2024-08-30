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

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tz_shop_detail")
public class ShopDetail implements Serializable{
    private static final long serialVersionUID = 3300529542917772262L;

    @TableId
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店长用户id")
    private String userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "店长用户名称")
    private String userName;

    @ApiModelProperty(value = "店铺简介")
    private String intro;

    @ApiModelProperty(value = "店长")
    private String shopOwner;

    @ApiModelProperty(value = "店铺绑定的手机")
    private String mobile;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "店铺联系电话")
    private String tel;

    @ApiModelProperty(value = "店铺接收短信通知手机号")
    private String receiveMobile;

    @ApiModelProperty(value = "店铺所在纬度")
    private String shopLat;

    @ApiModelProperty(value = "店铺所在经度")
    private String shopLng;

    @ApiModelProperty(value = "店铺详细地址")
    private String shopAddress;

    @ApiModelProperty(value = "店铺所在省份")
    private String province;

    @ApiModelProperty(value = "店铺所在省份Id")
    private Long provinceId;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺所在城市Id")
    private Long cityId;

    @ApiModelProperty(value = "店铺所在区域")
    private String area;

    @ApiModelProperty(value = "店铺所在区域Id")
    private Long areaId;

    @ApiModelProperty(value = "店铺logo")
    private String shopLogo;

    @ApiModelProperty(value = "店铺状态(-1:未开通 0: 停业中 1:营业中 2:平台下线 3:平台下线待审核)")
    private Integer shopStatus;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "分销设置(0关闭 1开启)")
    private Integer isDistribution;

    @ApiModelProperty(value = "营业执照")
    private String businessLicense;

    @ApiModelProperty(value = "身份证正面")
    private String identityCardFront;

    @ApiModelProperty(value = "身份证反面")
    private String identityCardLater;

    @ApiModelProperty(value = "0普通店铺 1优选好店")
    private Integer type;

    @ApiModelProperty(value = "签约起始时间")
    private Date contractStartTime;

    @ApiModelProperty(value = "签约终止时间")
    private Date contractEndTime;

    @ApiModelProperty("移动端背景图")
    private String mobileBackgroundPic;

    @ApiModelProperty("pc背景图")
    private String pcBackgroundPic;

    @ApiModelProperty("商家名称")
    private String merchantName;

    @ApiModelProperty(value = "邮箱",required=true)
    private String email;

    @ApiModelProperty("商家账号")
    @TableField(exist = false)
    private String merchantAccount;

    @ApiModelProperty("账号状态， 1:启用 0:禁用 -1:删除")
    @TableField(exist = false)
    private Integer accountStatus;

    @TableField(exist = false)
    private Long employeeId;

    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("部门id")
    private String deptId;

}

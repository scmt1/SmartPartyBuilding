/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 商家端用户
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Data
@TableName("tz_shop_employee")
public class ShopEmployee implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "店铺职员id")
    private Long employeeId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "职位：0 店铺超级管理员 1员工")
    private Integer type;

    @ApiModelProperty(value = "手机号(唯一)，可作为登陆账号")
    private String mobile;

    @NotBlank(message="用户名不能为空")
    @Size(min = 2,max = 20,message = "用户名长度要在2-20之间")
    @ApiModelProperty(value = "用户名（唯一)，可作为登陆账号")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    @NotBlank(message="邮箱不能为空")
    @Email(message="邮箱格式不正确")
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "状态  0：禁用   1：正常")
    private Integer status;

    @ApiModelProperty(value = "创建者ID")
    private Long createEmployeeId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺状态（-1：已删除 0：停业中 1：营业中 2：违规下线 3：违规审核 4：开店申请中 5：开店申请待审核")
    private Integer shopStatus;

    @TableField(exist=false)
    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIdList;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("所属机构编号")
    private String orgId;
    @ApiModelProperty("组织id")
    private Long deptId;

}

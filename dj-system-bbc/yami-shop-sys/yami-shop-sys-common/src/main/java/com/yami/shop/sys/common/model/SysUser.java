/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yami.shop.common.util.PrincipalUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 * @author yami
 */
@Data
@TableName("tz_sys_user")
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     *
     */
    @TableId
    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("用户名")
    @NotBlank(message="用户名不能为空")
    @Size(min = 2,max = 20,message = "用户名长度要在2-20之间")
    private String username;

    @ApiModelProperty("用户昵称")
    @NotBlank(message="用户昵称")
    @Size(min = 2,max = 20,message = "用户昵称长度要在2-20之间")
    private String nickName;

    @ApiModelProperty("密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ApiModelProperty("邮箱")
    @NotBlank(message="邮箱不能为空")
    @Email(message="邮箱格式不正确")
    private String email;

    @ApiModelProperty("手机号")
    @Pattern(regexp= PrincipalUtil.MOBILE_REGEXP,message = "请输入正确的手机号")
    private String mobile;

    @ApiModelProperty("状态 0：禁用 1：正常")
    private Integer status;

    @ApiModelProperty("角色ID列表")
    @TableField(exist=false)
    private List<Long> roleIdList;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("是否本人")
    @TableField(exist = false)
    private Integer isSelf;

}

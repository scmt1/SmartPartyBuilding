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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yami.shop.bean.param.UserTagParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_user")
public class User implements Serializable {
    private static final long serialVersionUID = 2090714647038636896L;
    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
    private String userId;


    /**
     * 用户名
     */
    private String userName;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 真实姓名
     */

    private String realName;

    /**
     * 用户邮箱
     */

    private String userMail;

    /**
     * 登录密码
     */

    private String loginPassword;

    /**
     * 支付密码
     */

    private String payPassword;

    /**
     * 手机号码
     */

    private String userMobile;

    /**
     * 修改时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 注册时间
     */

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date userRegtime;

    /**
     * 注册IP
     */

    private String userRegip;

    /**
     * 备注
     */

    private String userMemo;

    /**
     * 禁用备注
     */
    private String disableRemark;

    /**
     * 0男 1女
     */
    private String sex;

    /**
     * 例如：2009-11-27
     */

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String birthDate;

    /**
     * 头像图片路径
     */
    private String pic;

    /**
     * 状态 1 正常 0 无效
     */
    private Integer status;

    /**
     * 积分
     */
    @TableField(exist = false)
    private Integer score;

    /**
     * 会员成长值
     */
    @TableField(exist = false)
    private Long growth;

    /**
     * 会员等级
     */
    private Integer level;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;

    /**
     * vip结束时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date vipEndTime;

    /**
     * 会员等级名称
     */
    @TableField(exist = false)
    private String levelName;

    /**
     * openId list
     */
    @TableField(exist = false)
    private List<String> bizUserIdList;

    /**
     * 标签
     */
    @TableField(exist = false)
    private List<UserTagParam> userTagParam;


}

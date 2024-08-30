/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Yami
 */
@Data
public class PartyMemberDto {


    private String name;
    private String idCard;
    private String sex;
    private String nation;
    private String jiguan;
    private String birthday;
    private String education;
    private String personType;
    private String deptName;
    private String partyTime;
    private String transferTime;
    private String workPosition;
    private String address;
    private String phone;
    private String telephone;
    private String isFamer;
    private String isLost;
    private String loatTIme;
    private String isFlow;

}

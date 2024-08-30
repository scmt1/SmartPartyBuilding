package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author mike
 * @since 2022-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzPayFeeDetail对象", description="")
public class TzPayFeeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "缴纳月份")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payMonth;

    @ApiModelProperty(value = "应缴金额")
    private Double shouldPay;

    @ApiModelProperty(value = "实缴金额")
    private Double actuallyPay;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "部门名字")
    private String deptName;

    @ApiModelProperty(value = "缴纳状态")
    private String status;

    @ApiModelProperty(value = "是否代缴")
    private String isInsteadPay;

    @ApiModelProperty(value = "缴纳时间")
    private Date payTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "payFee表的外键")
    private Integer payFeeId;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "党员id")
    private Integer partyMemberId;
    @TableField(exist = false)
    @ApiModelProperty(value = "党员属性")
    private String personType;

    @ApiModelProperty(value = "身份证后六位")
    private String idCardLast;

    @ApiModelProperty(value = "按年统计")
    @TableField(exist = false)
    private Date year;

}

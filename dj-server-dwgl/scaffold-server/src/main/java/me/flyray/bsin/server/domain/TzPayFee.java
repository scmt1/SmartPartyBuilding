package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("tz_pay_fee")
public class TzPayFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门名字")
    private String deptName;

    @ApiModelProperty(value = "缴纳月份")
    private String payMonth;

    @ApiModelProperty(value = "应缴纳金额")
    private BigDecimal shouldPay;

    @ApiModelProperty(value = "实缴纳金额")
    private BigDecimal actuallyPay;

    @ApiModelProperty(value = "发布状态0否1是")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "是否删除")
    @TableField(exist = false)
    private String year;

    @ApiModelProperty(value = "应缴人数")
    @TableField(exist = false)
    private Integer shouldPayNum;

    @ApiModelProperty(value = "实缴人数")
    @TableField(exist = false)
    private Integer actuallyPayNum;
}

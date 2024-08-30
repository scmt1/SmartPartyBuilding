package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel(value="TzPayFee对象", description="党费缴纳表")
public class TzPayFee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "部门名字")
    private String deptName;

    @ApiModelProperty(value = "缴纳月份")
    private Date payMonth;

    @ApiModelProperty(value = "应缴纳金额")
    private Double shouldPay;

    @ApiModelProperty(value = "实缴纳金额")
    private Double actuallyPay;

    @ApiModelProperty(value = "发布状态0否1是")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "是否删除")
    @TableField(exist = false)
    private String year;


}

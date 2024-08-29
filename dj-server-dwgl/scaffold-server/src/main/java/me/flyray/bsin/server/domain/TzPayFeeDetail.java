package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mike
 * @since 2022-11-08
 */
@Data
@TableName("tz_pay_fee_detail")
public class TzPayFeeDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "名字")
    private String name;

    @ApiModelProperty(value = "缴纳月份")
    @JsonFormat(pattern = "yyyy-MM")
    private String payMonth;

    @ApiModelProperty(value = "应缴金额")
    private BigDecimal shouldPay;

    @ApiModelProperty(value = "实缴金额")
    private BigDecimal actuallyPay;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "部门名字")
    private String deptName;

    @ApiModelProperty(value = "缴纳状态")
    private Integer status;

    @ApiModelProperty(value = "是否代缴")
    private Integer isInsteadPay;

    @ApiModelProperty(value = "缴纳时间")
    private Date payTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "payFee表的外键")
    private Long payFeeId;

    @ApiModelProperty(value = "是否删除")
    @TableLogic(value = "0", delval = "1")
    private Integer delFlag;

    @ApiModelProperty(value = "党员id")
    private Long partyMemberId;

    @ApiModelProperty(value = "身份证后六位")
    private String idCardLast;

    @ApiModelProperty(value = "代缴党员id")
    private Long insteadPayPartyMemberId;

    @ApiModelProperty(value = "")
    private String insteadPayPartyMemberName;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "缴纳类型")
    private String paymentType;

    @ApiModelProperty(value = "缴纳基数")
    private String paymentBase;

    @ApiModelProperty(value = "缴纳比例")
    private String paymentRatio;

    @ApiModelProperty(value = "原因")
    private String paymentReason;

    @ApiModelProperty(value = "其他说明")
    private String reasonOther;

    @ApiModelProperty(value = "印证材料")
    private String confirmMaterial;

    //-------------------------------

    @TableField(exist = false)
    @ApiModelProperty(value = "党员属性")
    private String personType;

    @ApiModelProperty(value = "按年统计")
    @TableField(exist = false)
    private Date year;


    @TableField(exist = false)
    @ApiModelProperty(value = "支付成功数量")
    private Integer payNum;

    @TableField(exist = false)
    @ApiModelProperty(value = "部门ids")
    private List<Long> deptIds;


    @ApiModelProperty(value = "手机号")
    @TableField(exist = false)
    private String phone;

}

package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("cq_ppmd_pay_log")
public class CqPayFeeHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer orgId;

    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "党员名称")
    private String userName;

    @ApiModelProperty(value = "缴纳金额")
    private String payAlready;
    @ApiModelProperty(value = "缴纳年月")
    private String payFor;
    @ApiModelProperty(value = "支付年月")
    private String payDate;

    @ApiModelProperty(value = "支付党员名称")
    private String payUserName;
}

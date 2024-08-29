package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("tz_pay_fee_detail_log")
public class TzPayFeeDetailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long payFeeDetailId;

    private String orderNo;

    private Integer orderStatus;

    private Long amount;

    private String payResult;

    private Date payResultTime;

    private Date createTime;

    private Date successTime;

    private Integer delFlag;

    private Long partyMemberId;

    private String payMonth;

    private Long deptId;

    private BigDecimal shouldPay;

    //---------------------------

    @TableField(exist = false)
    private String partMemberName;

}

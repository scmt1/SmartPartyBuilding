package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 党组织关系转移表
 * </p>
 *
 * @author ycy
 * @since 2022-11-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PartyMemberTransfer对象", description="党组织关系转移表")
public class PartyMemberTransfer implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "党员id")
    private Long partyMemberId;

    @ApiModelProperty(value = "党员名字")
    private String name;

    @ApiModelProperty(value = "是否是市外转移")
    private Integer isOutCity;

    @ApiModelProperty(value = "市外组织全称")
    private String cityName;

    @ApiModelProperty(value = "转入的市内组织全称")
    private String cityDeptName;

    @ApiModelProperty(value = "转入的市内组织id")
    private Integer deptId;

    @ApiModelProperty(value = "1待处理 2已接收")
    private String receiveStatus;

    @ApiModelProperty(value = "转出的组织名称")
    private String transferOutDeptName;

    @ApiModelProperty(value = "转出的组织名称id")
    private Integer transferOutDeptId;

    @ApiModelProperty(value = "转接时间")
    private Date transferTime;

    @ApiModelProperty(value = "处理时间")
    private Date doTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否删除（0未删除，1已删除）")
    private Integer delFlag;

    @ApiModelProperty(value = "操作人")
    private String updateBy;

    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;
}

package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.utils.DecryptTransaction;
import me.flyray.bsin.server.utils.EncryptTransaction;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("party_member_transfer")
public class JcxfPartyMemberTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    //转出党员Id
    private Long partyId;

    private Long outBranchId;

    //转入支部Id(市内转移时使用)
    private Long inBranchId;

    //转入支部名称(市外转移使用)
    private String inBranchName;

    //转移类型(1:转移至市内,2:转移至市外)
    private String transferType;

    //当前状态(1:已处理,2:挂起)
    private String transferStatus;

    //转接时间
    private Date transferTime;

    //执行机构
    private Long executeDept;

    //挂起原因
    private String hangUpReason;

    //处理状态
    private String acceptStatus;

    //打回原因
    private String rejectReason;

    private Date createDate;

    private Long createBy;

    private Long updateBy;
    private Date updateDate;

    private Integer delFlag;

    //党员转出区域Id
    private Integer areaId;

    //---------------------------------


    @TableField(exist = false)
    private JcxfPartyMember partyMember;

    @TableField(exist = false)
    private JcxfSysDept oldTzSysDept;

    @TableField(exist = false)
    private JcxfSysDept newTzSysDept;

    @TableField(exist = false)
    private String areaName;
}

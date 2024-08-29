package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_party_honor")
public class TzPartyHonor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "党员id")
    private Long partyMemberId;

    @ApiModelProperty(value = "荣誉名称")
    private String honorName;

    @ApiModelProperty(value = "荣誉类型 1、组织荣誉 2、个人荣誉")
    private String honorType;

    @ApiModelProperty(value = "审核状态（1、已提交，2、审核通过，3、已驳回）")
    private String status;

    @ApiModelProperty(value = "图片地址")
    private String imagePath;

    @ApiModelProperty(value = "荣誉等级 1、国家级 2、省级 3、市级")
    private String honorClass;

    @ApiModelProperty(value = "获奖年份")
    private String getYear;

    @ApiModelProperty(value = "驳回理由")
    private String reason;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    @ApiModelProperty(value = "上传人id")
    private Long uploadId;

    @ApiModelProperty(value = "上传者类型1、管理员 2、党员")
    private Integer uploadType;

    @ApiModelProperty
    private String awardName;

    @TableField(exist = false)
    private JcxfSysDept tzSysDept;

    @TableField(exist = false)
    private JcxfPartyMember partyMember;

    @ApiModelProperty
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty
    @TableField(exist = false)
    private Integer count;

}

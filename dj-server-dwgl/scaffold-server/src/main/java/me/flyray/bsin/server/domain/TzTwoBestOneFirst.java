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
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="两优一先对象", description="两优一先")
@TableName("tz_two_best_one_first")
public class TzTwoBestOneFirst implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "审核状态")
    private String status;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    @ApiModelProperty(value = "上传部门id")
    private Long createDeptId;

    @ApiModelProperty(value = "党员id")
    private Long partyMemberId;

    @ApiModelProperty(value = "表格类型，党务工作者、党员、党组织")
    private String tableType;

    @ApiModelProperty(value = "个人简历/基本情况")
    private String baseCondition;

    @ApiModelProperty(value = "曾受表彰情况")
    private String commendCondition;

    @ApiModelProperty(value = "主要事迹")
    private String mainDeed;

    @ApiModelProperty(value = "个人简历")
    private String resume;

    @ApiModelProperty(value = "编辑时间")
    private Date updateTime;

    @ApiModelProperty(value = "党员照片")
    private String avatar;

    @ApiModelProperty(value = "党员当时信息")
    private String partyMemberInfo;

    @ApiModelProperty(value = "部门当时信息")
    private String deptInfo;

    @ApiModelProperty(value = "表彰表扬类型")
    private String commendType;

    @ApiModelProperty(value = "申请批次")
    private Integer timeId;

    @ApiModelProperty(value = "荣誉图片")
    private String commendImg;

    // ---------------------------------------

    @TableField(exist = false)
    private TzTwoBestOneFirstTime timeInfo;

    // @TableField(exist = false)
    // private TzSysDept tzSysDept;

//    @TableField(exist = false)
//    private List<PartyMember> partyMemberList;
}

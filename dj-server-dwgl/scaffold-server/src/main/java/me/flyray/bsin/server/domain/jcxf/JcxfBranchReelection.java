package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("branch_reelection")
public class JcxfBranchReelection implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "班子届次")
    private Integer teamSession;

    @ApiModelProperty(value = "上届换届时间")
    private Date lastSessionFinishTime;

    @ApiModelProperty(value = "该届起始时间")
    private Date thisSessionStartTime;

    @ApiModelProperty(value = "该届届满时间")
    private String thisSessionFinishTime;

    @ApiModelProperty(value = "选举方式")
    private Integer voteType;

    @ApiModelProperty(value = "创建者")
    private Integer createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    private Integer updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    @ApiModelProperty(value = "是否首届(0否1是)")
    private Integer isFirst;

    //--------------------------------------

    @ApiModelProperty(value = "是否完成换届")
    @TableField(exist = false)
    private String isChange;

    @ApiModelProperty(value = "换届提醒")
    @TableField(exist = false)
    private String tipContent;

    @ApiModelProperty(value = "部门名称")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "书记")
    private String secretary;

    @ApiModelProperty(value = "副书记")
    private String deputySecretary;

    @ApiModelProperty(value = "组织委员")
    private String committeeMember;

    @ApiModelProperty(value = "纪检委员")
    private String diCommitteeMember;

    @ApiModelProperty(value = "宣传委员")
    private String publicityCommitteeMember;

    @ApiModelProperty(value = "其他委员")
    private String otherMembers;
}

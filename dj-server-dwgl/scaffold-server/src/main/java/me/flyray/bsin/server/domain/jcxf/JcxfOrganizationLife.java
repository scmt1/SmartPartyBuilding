package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 组织生活表（三会一课等相关信息）
 * </p>
 */
@Data
@TableName(value = "party_meeting")
public class JcxfOrganizationLife implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String meetingTopic;

    private Integer meetingType;

    private Integer meetingCategory;

    private Date startTime;

    private Date endTime;

    private Integer stimatedDuration;

    private String meetingAddress;

    private String meetingInitiator;

    private Long deptId;

    private String meetingContent;

    private Integer meetingStatus;

    private Date createDate;

    private Long createBy;

    private Long updateBy;

    private Date updateDate;

    private Boolean delFlag;

    private String host;

    private String recorder;

    private String lecturer;

    private String situationRecord;

    private Integer teamId;

    private Integer joinAllCount;

    private Long batchId;

    private Integer isSend;

    private Date lastSendTime;

    private String planMeetingPartyIds;

    private String intro;

    private String classType;

    private String imgUrl;


    //----------------------------------------------

    @TableField(exist = false)
    private String hostName;

    @TableField(exist = false)
    private String hostAvatar;

    @TableField(exist = false)
    private String recorderName;

    @TableField(exist = false)
    private String recorderAvatar;

    @TableField(exist = false)
    private String teamName;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String createdName;

    @TableField(exist = false)
    private String editName;

    @ApiModelProperty(value = "组织类型")
    @TableField(exist = false)
    private String organizationType;

    @ApiModelProperty(value = "是否是退休支部（0否1是）")
    @TableField(exist = false)
    private String veteran;

    @ApiModelProperty(value = "该部门应到人员名单")
    @TableField(exist = false)
    private List<JcxfPartyMember> joinPartyMemberList;

    @ApiModelProperty(value = "用户名")
    @TableField(exist = false)
    private String username;

    @ApiModelProperty(value = "支部大会数量")
    @TableField(exist = false)
    private Integer zbdhCount;

    @ApiModelProperty(value = "支委会议数量")
    @TableField(exist = false)
    private Integer zwhyCount;

    @ApiModelProperty(value = "党小组会数量")
    @TableField(exist = false)
    private Integer dxzhCount;

    @ApiModelProperty(value = "党课数量")
    @TableField(exist = false)
    private Integer dkCount;
}

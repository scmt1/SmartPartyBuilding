package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("party_meeting")
public class JcxfPartyMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "会议主题")
    private String meetingTopic;

    @ApiModelProperty(value = "会议类型(1：党小组会，2：支部委员会，3：支部党员大会，4：党课)")
    private Integer meetingType;

    @ApiModelProperty(value = "会议开始时间")
    private Date startTime;

    @ApiModelProperty(value = "会议结束时间")
    private Date endTime;

    @ApiModelProperty(value = "会议地址")
    private String meetingAddress;

    @ApiModelProperty(value = "发起人")
    private String meetingInitiator;


    @ApiModelProperty(value = "发起人所在支部")
    private Long deptId;

    @ApiModelProperty(value = "会议内容")
    private String meetingContent;

    @ApiModelProperty(value = "会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))")
    private String meetingStatus;

    @ApiModelProperty(value = "会议状态(会议状态(1：未开始、2：进行中、3：已结束，4:已取消))")
    private Date createDate;

    @ApiModelProperty(value = "删除标记")
    private byte delFlag;

    @ApiModelProperty(value = "会议详情")
    private String situationRecord;

}

package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("party_meeting_user")
public class JcxfPartyMeetingUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long meetingId;

    private Long userId;

    private Integer checkinStatus;

    private Date checkinTime;

    private String checkinReason;

    private String askForLeaveReason;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Boolean delFlag;

    private Integer userStatus;

    private Integer userPosition;

}

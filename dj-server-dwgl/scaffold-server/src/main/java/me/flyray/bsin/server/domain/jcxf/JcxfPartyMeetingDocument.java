package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("party_meeting_document")
public class JcxfPartyMeetingDocument implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long meetingId;

    private String attachmentName;

    private String attachmentSuffix;

    private Integer attachmentType;

    private String attachmentRelativePath;

    private String attachmentAbsolutePath;

    private Date uploadTime;

    private String duration;

    private Date createDate;

    private Integer createBy;

    private Integer updateBy;

    private Date updateDate;

    private Boolean delFlag;

    private Double fileSize;

    private String type;

}

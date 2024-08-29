package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;

import java.util.Date;
import java.util.List;

@Data
@TableName("tz_study_view")
public class TzStudyView {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String type;

    private int studyId;

    private String partyMemberIds;

    private String deptIds;

    private String positionIds;

    private Date createTime;

    private Date updateTime;

    private Date messageTime;

    private Integer columnId;

    @TableField(exist = false)
    private List<JcxfPartyMember> partyMemberList;

    @TableField(exist = false)
    private List<JcxfSysDept> tzSysDeptList;

}

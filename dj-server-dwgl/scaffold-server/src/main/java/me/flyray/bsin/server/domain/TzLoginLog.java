package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("tz_login_log")
public class TzLoginLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String phone;

    private String idCard;

    private Date createTime;

    private String type;

    private String result;

    private Long deptId;

    private Long partyMemberId;

    private String realName;

    // private JcxfPartyMember partyMember;

}

package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("party_team_member")
public class JcxfPartyTeamMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //党员ID
    private Long partyId;
    //党小组Id
    private Long teamId;
    //是否党小组长 1 为死 2 为否
    private Integer isLeader;


}

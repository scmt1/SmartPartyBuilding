package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import me.flyray.bsin.server.domain.vo.JcxfPartyMemberVo;

import java.util.Date;
import java.util.List;

@Data
@TableName("tz_user_tag")
public class TzUserTag {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String tagName;

    private Integer userNum;

    private Date createTime;

    private String tenantId;


    @TableField(exist = false)
    private List<JcxfPartyMemberVo> tableData;
}

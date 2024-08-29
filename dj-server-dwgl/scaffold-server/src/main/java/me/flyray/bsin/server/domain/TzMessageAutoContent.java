package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("tz_message_auto_content")
public class TzMessageAutoContent {


    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long deptId;

    private Integer autoId;

    private String temContent;

    private Date createDate;

    private Date updateDate;
}

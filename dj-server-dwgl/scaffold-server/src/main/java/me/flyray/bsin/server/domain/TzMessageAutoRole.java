package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@TableName("tz_message_auto_role")
public class TzMessageAutoRole {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long deptId;

    private Integer autoId;

    @ApiModelProperty("状态 1、启用 2、关闭")
    private String status;

    private Date createDate;


    //-----------------------------

    @TableField(exist = false)
    private String autoIdList;

    @TableField(exist = false)
    private Integer delFlag;

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private List<TzMessageAuto> autoList;

    @TableField(exist = false)
    private TzMessageAuto auto;
}

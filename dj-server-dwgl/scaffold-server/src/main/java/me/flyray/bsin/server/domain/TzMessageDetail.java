package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tz_message_detail")
public class TzMessageDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "消息id")
    private String msgId;
    @ApiModelProperty(value = "任务名称")
    private String missionName;

    @ApiModelProperty(value = "发送内容")
    private String sendContent;

    @ApiModelProperty(value = "状态上报时间")
    private Date postTime;
    @ApiModelProperty(value = "计费条数")
    private Integer feeCount;

    @ApiModelProperty(value = "发送状态（1、成功，2、失败、3，待返回）")
    private String sendStatus;

    @ApiModelProperty(value = "模板id")
    private String templateId;

    @ApiModelProperty(value = "备注")
    private String bak;

    @ApiModelProperty(value = "消息id")
    private Long messageId;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "发送类型 1、自定义模板 2、默认模板 3、自定义发送")
    private String sendType;

    @ApiModelProperty(value = "返回code")
    private String resultCode;

    @ApiModelProperty(value = "返回msg")
    private String resultMsg;

    //--------------------------------------
    @TableField(exist = false)
    private String deptName;
}

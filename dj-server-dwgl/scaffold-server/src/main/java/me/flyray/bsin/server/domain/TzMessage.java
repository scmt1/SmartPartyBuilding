package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@TableName("tz_message")
public class TzMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "短信内容")
    private String content;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "发送时间")
    private Date sendTime;
    @ApiModelProperty(value = "发送方式")
    private Integer sendMod;
    @ApiModelProperty(value = "(1自定义发送，2、模板发送)")
    private Integer sendType;
    @ApiModelProperty(value = "发送时间方式（1、立即发送，2、定时发送）")
    private Integer sendTimeType;

    @ApiModelProperty(value = "发送对象")
    private String sendObject;

    @ApiModelProperty(value = "任务名称")
    private String missionName;

    @ApiModelProperty(value = "有效号码数")
    private Integer successCount;

    @ApiModelProperty(value = "无效号码数")
    private Integer errorCount;
    @ApiModelProperty(value = "消息id")
    private String msgId;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @TableField(exist = false)
    @ApiModelProperty(value = "发送开始时间")
    private String sendStartTime;
    @TableField(exist = false)
    @ApiModelProperty(value = "发送结束时间")
    private String sendEndTime;
    @ApiModelProperty(value = "1、发送成功，2、发送失败，3、待返回,4、已取消")
    private String status;

    @TableField(exist = false)
    private List<MessageDto> records;
}

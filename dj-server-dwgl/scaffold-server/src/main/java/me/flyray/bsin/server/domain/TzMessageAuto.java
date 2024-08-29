package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.facade.service.TzMessageAutoContentService;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tz_message_auto")
public class TzMessageAuto implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务名称")
    private String missionName;


    @ApiModelProperty(value = "模板名称")
    private String temName;

    @ApiModelProperty(value = "模板id")
    private String temId;

    @ApiModelProperty(value = "模板内容")
    private String temContent;

    @ApiModelProperty(value = "是否开启（0否1是）")
    private Integer isOpen;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "是否有变量（0否1是）")
    private Integer isVariable;

    @ApiModelProperty(value = "模板对应的业务类型")
    private String type;

    @ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "任务类型 1、自动任务 2、手动任务")
    private String missionType;

    @ApiModelProperty(value = "租户id")
    private String tenantId;


    @ApiModelProperty(value = "模板变量")
    private String variableContent;

    //-------------------------------
    @ApiModelProperty(value = "响应码")
    @TableField(exist = false)
    private String code;

    @ApiModelProperty(value = "权限")
    @TableField(exist = false)
    private TzMessageAutoRole role;

    @ApiModelProperty(value = "自定义内容")
    @TableField(exist = false)
    private TzMessageAutoContent content;


}

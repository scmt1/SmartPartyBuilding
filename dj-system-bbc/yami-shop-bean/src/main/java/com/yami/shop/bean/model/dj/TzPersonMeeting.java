package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author mike
 * @since 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TzPersonMeeting对象", description = "")
public class TzPersonMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "参会支部")
    private String deptName;

    @ApiModelProperty(value = "主持人")
    private String host;

    @ApiModelProperty(value = "记录人")
    private String record;

    @ApiModelProperty(value = "参会人员")
    private String joins;

    @ApiModelProperty(value = "无故缺席")
    private String absent;

    @ApiModelProperty(value = "病假")
    private String sick;

    @ApiModelProperty(value = "公假")
    private String common;

    @ApiModelProperty(value = "事假")
    private String thing;

    @ApiModelProperty(value = "迟到")
    private String later;

    @ApiModelProperty(value = "流动党员参会")
    private String flow;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "会议id")
    private Integer meetingId;

    @ApiModelProperty(value = "支部id")
    private Integer deptId;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;


}

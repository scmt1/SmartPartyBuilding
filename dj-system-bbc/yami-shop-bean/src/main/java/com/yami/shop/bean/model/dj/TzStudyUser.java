package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yami.shop.bean.model.AttachFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 党员表
 * </p>
 *
 * @author ycy
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzStudyUser对象", description="用户文件学习记录表")
public class TzStudyUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "学习id")
    private Long studyId;
    @ApiModelProperty(value = "学分")
    private Integer studyScore;
    @ApiModelProperty(value = "学时")
    private Integer studyHours;
    @ApiModelProperty(value = "学习类型（1文件精神，2党史学习，3在线视频）")
    private String type;
    @ApiModelProperty(value = "是否完成（0否1是）")
    private String isFinish;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;



}

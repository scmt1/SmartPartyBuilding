package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yami.shop.bean.model.AttachFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动阵地信息表
 * </p>
 *
 * @author mike
 * @since 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzTopicScore对象", description="专项答题分数记录表")
public class TzTopicScore implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer delFlag;
    @ApiModelProperty(value = "专项答题记录表id")
    private Integer topicDetailId;
    @ApiModelProperty(value = "单项分数")
    private Integer itemRecord;
    @ApiModelProperty(value = "是否正确")
    private Integer trueOrFalse;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "实际答案")
    private String achieveAnswer;
    @ApiModelProperty(value = "标准答案")
    private String standard;

    @ApiModelProperty(value = "专项答题用户记录id")
    private String topicUserId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    private String questionType;



}

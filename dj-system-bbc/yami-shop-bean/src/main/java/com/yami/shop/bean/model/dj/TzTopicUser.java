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
@ApiModel(value="TzTopicUser", description="专项答题人员分数信息表")
public class TzTopicUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "专项答题表id")
    private Integer topicId;

    private Integer delFlag;
    @ApiModelProperty(value = "总分数")
    private Integer totalScore;
    @ApiModelProperty(value = "实际分数")
    private Integer achieveScore;
    @ApiModelProperty(value = "所得学分")
    private Integer studyScore;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "用户id")
    private String userId;


}

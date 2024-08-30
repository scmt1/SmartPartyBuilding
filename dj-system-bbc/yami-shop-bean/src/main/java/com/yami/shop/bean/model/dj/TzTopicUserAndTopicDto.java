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
 * 活动阵地信息表
 * </p>
 *
 * @author mike
 * @since 2022-12-09
 */
@Data
public class TzTopicUserAndTopicDto {



    private Integer id;
    @ApiModelProperty(value = "专项答题表id")
    private Integer topicId;

    private Integer delFlag;
    @ApiModelProperty(value = "总分数")
    private Integer totalScore;
    @ApiModelProperty(value = "实际分数")
    private Integer achieveScore;


    @ApiModelProperty(value = "题目主题")
    private String topicName;
    @ApiModelProperty(value = "主题类型")
    private String topicType;

}

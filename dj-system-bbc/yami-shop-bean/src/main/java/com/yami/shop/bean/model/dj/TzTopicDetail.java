package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 专题发布详情表
 * </p>
 *
 * @author hxh
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzTopicDetail对象", description="专题发布详情表")
public class TzTopicDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目内容")
    private String question;

    @ApiModelProperty(value = "答案")
    private String answer;

    @ApiModelProperty(value = "解析")
    private String analysis;

    @ApiModelProperty(value = "题目类型(1选择题 2 判断题 3填空题 4 主观题)")
    private String questionType;

    @ApiModelProperty(value = "对应的专项主题id")
    private Integer topicId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "选项A")
    private String itemA;

    @ApiModelProperty(value = "选项B")
    private String itemB;

    @ApiModelProperty(value = "选项C")
    private String itemC;

    @ApiModelProperty(value = "选项D")
    private String itemD;

    private Integer delFlag;
    private Integer itemRecord;

}

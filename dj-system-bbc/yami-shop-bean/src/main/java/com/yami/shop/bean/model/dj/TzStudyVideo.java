package com.yami.shop.bean.model.dj;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 视频管理表
 * </p>
 *
 * @author hxh
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzStudyVideo对象", description="视频管理表")
public class TzStudyVideo implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "栏目id")
    private Integer columnId;

    @ApiModelProperty(value = "栏目名称")
    private String columnName;

    private String title;

    @ApiModelProperty(value = "审核状态(0未发布 1已发布)")
    private String status;

    @ApiModelProperty(value = "点击数")
    private Integer playCount;

    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    @ApiModelProperty(value = "收藏数")
    private Integer collectCount;

    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "发布时间")
    private Date postTime;
    @ApiModelProperty(value = "审核时间")
    private Date processTime;

    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "学时")
    private Integer hours;

    @ApiModelProperty(value = "学分")
    private Integer scores;

    @ApiModelProperty(value = "摘要")
    private String summary;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "视频简介")
    private String introduce;

    private Integer delFlag;

    private String deptId;

}

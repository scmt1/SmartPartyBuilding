package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.domain.TzVideoColumn;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 视频管理表
 * </p>
 *
 * @author hxh
 * @since 2023-02-23
 */
@Data
@TableName("tz_study_video")
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

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "来源")
    private String source;

    @ApiModelProperty(value = "视频简介")
    private String introduce;

    private Integer delFlag;

    private Long deptId;

    private Integer top;

    private Integer topSort;

    private Integer showHome;

    private Integer showHomeSort;

    private String keynoteSpeaker;

    private Date startTime;

    private Date endTime;

    private String viewType;

    private String imageUrl;

    private String fileUrl;

    private String fileName;

    private String videoUrl;

    //------------------------------------------------

    @TableField(exist = false)
    private List<AttachFile> attachFileList;

    @TableField(exist = false)
    private TzVideoColumn tzVideoColumn;

    @ApiModelProperty(value = "是否学习过")
    @TableField(exist = false)
    private Boolean isStudy;

    @ApiModelProperty(value = "学习过的人数")
    @TableField(exist = false)
    private Integer studyNum;

    @ApiModelProperty(value = "课程范围设置信息")
    @TableField(exist = false)
    private TzStudyView tzStudyView;

    @ApiModelProperty(value = "短信发送时间")
    @TableField(exist = false)
    private Date messageTime;

}

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
import java.util.Map;

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
@ApiModel(value="TzStudyFile", description="专项答题人员分数信息表")
public class TzStudyFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "学时")
    private Integer hours;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "发布时间")
    private Date postTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "文件名")
    private String name;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "发布状态")
    private String status;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    @ApiModelProperty(value = "是否修改")
    private String delFlag;
    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "封面图片")
    private String imagePath;

    @ApiModelProperty(value = "附件列表")
    @TableField(exist = false)
    private  List<AttachFile> attachFileList;
    @ApiModelProperty(value = "任务是否完成（该字段是tz_study_user表里的,这里是连表查询要用）")
    @TableField(exist = false)
    private  String isFinish;
    @ApiModelProperty(value = "学习进度")
    @TableField(exist = false)
    private  float progress;


}

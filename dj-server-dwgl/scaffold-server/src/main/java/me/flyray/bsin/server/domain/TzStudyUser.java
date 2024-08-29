package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfPartyMember;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 视频管理表
 * </p>
 *
 * @author hxh
 * @since 2023-02-23
 */
@Data
@TableName("tz_study_user")
public class TzStudyUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "手机端用户登录id，即党员id")
    private Long userId;

    @ApiModelProperty(value = "学习文件id")
    private Integer studyId;


    @ApiModelProperty(value = "学分")
    private Integer studyScore;

    @ApiModelProperty(value = "学时")
    private Integer studyHours;

    @ApiModelProperty(value = "学习类型（1文件精神，2党史学习，3在线视频，4、专项答题）")
    private String type;

    @ApiModelProperty(value = "是否完成学习（0否1是）")
    private Integer isFinish;

    @ApiModelProperty(value = "党员id和学习id的唯一对应，通过“,”来拼接")
    private String userStudy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "最后一次访问时间")
    private Date lastTime;
    @ApiModelProperty(value = "学习进度")
    private Float progress;

    @TableField(exist = false)
    private JcxfPartyMember partyMember;

    @TableField(exist = false)
    private TzStudyVideo tzStudyVideo;


}

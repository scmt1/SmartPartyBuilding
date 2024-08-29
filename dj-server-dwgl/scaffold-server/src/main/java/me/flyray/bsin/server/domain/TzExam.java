package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tz_exam")
public class TzExam implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "关联题库id")
    private Integer questionId;

    @ApiModelProperty(value = "考试封面")
    private String examImg;

    @ApiModelProperty(value = "考试时长")
    private Integer examHours;

    @ApiModelProperty(value = "考试名称")
    private String examName;

    @ApiModelProperty(value = "考试总分")
    private Integer examScore;

    @ApiModelProperty(value = "及格分数")
    private Integer examPass;

    @ApiModelProperty(value = "及格积分")
    private Integer passPoint;

    @ApiModelProperty(value = "满分积分")
    private Integer passSocre;

    @ApiModelProperty(value = "单选题id集合")
    private String radioIds;

    @ApiModelProperty(value = "多选题id集合")
    private String multipleIds;
    @ApiModelProperty(value = "判断题id集合）")
    private String judgeIds;
    @ApiModelProperty(value = "单选单题分数（默认5分）")
    private Integer radioScore;
    @ApiModelProperty(value = "多选单题分数（默认5分）")
    private Integer judgeScore;
    @ApiModelProperty(value = "本级部门id")
    private Long deptId;
    @ApiModelProperty(value = "可参加考试的部门id")
    private Integer deptIds;
    @ApiModelProperty(value = "")
    private Integer delFlag;
    @ApiModelProperty(value = "（1未发布、2已发布、3已撤回）")
    private String status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;



}

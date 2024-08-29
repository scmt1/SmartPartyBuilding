package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tz_question_bank")
public class TzQuestionBank implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer delFlag;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "题库名称")
    private String name;

    @ApiModelProperty(value = "题库描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "试题总数")
    private Integer countAll;

    @TableField(exist = false)
    @ApiModelProperty(value = "单选数量")
    private Integer oneSelect;
    @TableField(exist = false)
    @ApiModelProperty(value = "多选数量")
    private Integer selects;
    @TableField(exist = false)
    @ApiModelProperty(value = "判断题数量")
    private Integer isTrueCount;


}

package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("tz_test")
public class TzTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer delFlag;

    @ApiModelProperty(value = "部门id")
    private String deptId;
    @ApiModelProperty(value = "所属题库 id")
    private String questionId;

    @ApiModelProperty(value = "试题类型（1、单选，2、多选，3、判断）")
    private String testType;

    @ApiModelProperty(value = "正确答案")
    private String answer;
    @ApiModelProperty(value = "正确答案的选项id")
    private String answerId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "试题题目")
    private String title;
    @ApiModelProperty(value = "来源")
    private String source;
    @ApiModelProperty(value = "关键字")
    private String keyWord;
    @ApiModelProperty(value = "考点")
    private String point;


}

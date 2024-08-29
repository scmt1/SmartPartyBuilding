package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzVote对象", description="")
public class TzVote implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String delFlag;

    @ApiModelProperty(value = "投票名称")
    private String name;

    @ApiModelProperty(value = "发起组织")
    private String startDept;

    @ApiModelProperty(value = "发起组织id")
    private Long startDeptId;

    @ApiModelProperty(value = "发起人")
    private String createBy;

    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "是否记名（0否1是）")
    private String isScore;

    @ApiModelProperty(value = "投票次数")
    private Integer numberTimes;

    @ApiModelProperty(value = "投票结果")
    private String voteResult;

    @ApiModelProperty(value = "投票说明")
    private String voteDescription;

    @ApiModelProperty(value = "参与投票部门id集合")
    private String voteDeptIds;

    @ApiModelProperty(value = "投票类型(1知识问答、2、人物投票，3、问卷调查，4、视频投票)")
    private String voteType;

    @ApiModelProperty(value = "发起时间")
    private Date createTime;

    @ApiModelProperty(value = "封面图")
    private String imagePath;

    @ApiModelProperty(value = "投票详情id")
    @TableField(exist = false)
    private String voteDetailId;

    @ApiModelProperty(value = "浏览次数")
    private Integer viewCount;

    @ApiModelProperty(value = "状态")
    private Integer voteStatus;

    @ApiModelProperty(value = "参选数")
    @TableField(exist = false)
    private Integer detailNum;

    @ApiModelProperty(value = "票数")
    @TableField(exist = false)
    private Integer userNum;
}

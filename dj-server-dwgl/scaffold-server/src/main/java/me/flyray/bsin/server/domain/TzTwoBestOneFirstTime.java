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
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="两优一先对象", description="两优一先")
@TableName("tz_two_best_one_first_time")
public class TzTwoBestOneFirstTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Date upStartTime;

    private Date upEndTime;

    private String status;

    private Integer delFlag;

    private Long deptId;

    private Date createTime;

    private Date updateTime;

    private Integer maxParentId;

    private String title;

    private Date auditStartTime;

    private Date auditEndTime;

    //----------------------------

    @TableField(exist = false)
    private Map<String, Object> contentInfo;

    @TableField(exist = false)
    private Boolean addTime;

    @ApiModelProperty(value = "待提交数量")
    @TableField(exist = false)
    private Integer audit;

    @ApiModelProperty(value = "审核中数量")
    @TableField(exist = false)
    private Integer auditing;

    @ApiModelProperty(value = "审核驳回数量")
    @TableField(exist = false)
    private Integer turnDown;

    @ApiModelProperty(value = "审核通过数量")
    @TableField(exist = false)
    private Integer pass;

    @TableField(exist = false)
    private List<Long> deptIds;
}

package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="两优一先审核记录", description="两优一先审核记录")
@TableName("tz_two_best_one_first_audit")
public class TzTwoBestOneFirstAudit {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "两优一先id")
    private Integer tbofId;

    @ApiModelProperty(value = "审核部门id")
    private Long auditDeptId;

    @ApiModelProperty(value = "审核意见")
    private String auditMessage;

    @ApiModelProperty(value = "审核时间")
    private Date createTime;

    @ApiModelProperty(value = "审核状态 1、通过  2、驳回")
    private String auditStatus;

    @TableField(exist = false)
    private JcxfSysDept tzSysDept;

    /**
     * 表彰类型
     */
    @TableField(exist = false)
    private String commendType;
    /**
     * 表彰图片
     */
    @TableField(exist = false)
    private String commendImg;



}

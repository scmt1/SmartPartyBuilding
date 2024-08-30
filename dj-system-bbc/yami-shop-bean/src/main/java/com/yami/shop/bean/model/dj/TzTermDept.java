package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 部门换届表
 * </p>
 *
 * @author mike
 * @since 2022-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TzTermDept对象", description = "部门换届表")
public class TzTermDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer delFlag;

    @ApiModelProperty(value = "班子届次")
    private Integer leaderTime;

    @ApiModelProperty(value = "是否首届(0否1是)")
    private Integer isFirst;

    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上届换届时间")
    private Date lastChangeTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "本届换届时间")
    private Date thisChangeTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "本届届满时间")
    private Date thisFinishTime;

    @ApiModelProperty(value = "选举方式（1,投票选举）")
    private String electType;

    @ApiModelProperty(value = "记录添加时间")
    private Date recordAddTime;

    @ApiModelProperty(value = "最后一次编辑时间")
    private Date lastEditTime;

    @ApiModelProperty(value = "记录人")
    private String recorder;

    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    @ApiModelProperty(value = "备注")
    private String bak;
    @ApiModelProperty(value = "部门名称")
    private String deptName;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否完成换届")
    @TableField(exist = false)
    private String isChange;

}

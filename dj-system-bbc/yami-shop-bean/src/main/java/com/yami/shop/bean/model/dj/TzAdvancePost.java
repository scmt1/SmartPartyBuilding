package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author mike
 * @since 2022-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzAdvancePost对象", description="")
public class TzAdvancePost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "填报单位 ")
    private String fillUnit;

    @ApiModelProperty(value = "填报党组织id ")
    private String deptId;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "党组织名称")
    private String deptName;

    @ApiModelProperty(value = "所在省市区")
    private String provinceCity;

    @ApiModelProperty(value = "党组织负责人")
    private String manager;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "基本情况")
    private String baseCondition;

    @ApiModelProperty(value = "曾受表彰情况")
    private String commendCondition;

    @ApiModelProperty(value = "主要事迹")
    private String mainThings;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "填报时间")
    private String fillTime;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "是否删除（0未删除，1已删除）")
    private Integer delFlag;


}

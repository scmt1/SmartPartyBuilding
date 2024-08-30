package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 村(社区)集体经济表
 * </p>
 *
 * @author mike
 * @since 2022-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "TzIncomeDept对象", description = "村(社区)集体经济表")
public class TzIncomeDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deptId;

    private String deptName;

    @ApiModelProperty(value = "年份")
    private String year;

    @ApiModelProperty(value = "收入")
    private Double income;

    @ApiModelProperty(value = "成本")
    private Double cost;

    @ApiModelProperty(value = "利润")
    private Double profit;

    @ApiModelProperty(value = "收益分配(万元)")
    private Double incomeDistribution;

    @ApiModelProperty(value = "收益留存")
    private Double incomeSave;

    @ApiModelProperty(value = "集体固定资产价值")
    private Double collectiveAssets;

    @ApiModelProperty(value = "创建时间")
    @TableField("createTime")
    private Date createTime;

    @ApiModelProperty(value = "收入来源")
    private String incomeSource;

    private Integer delFlag;

    // 根据范围筛选查询
    @TableField(exist = false)
    private Double incomeMax;
    @TableField(exist = false)
    private Double incomeMin;
    @TableField(exist = false)
    private Double costMin;
    @TableField(exist = false)
    private Double costMax;
    @TableField(exist = false)
    private Double profitMax;
    @TableField(exist = false)
    private Double profitMin;
    @TableField(exist = false)
    private Double collectiveAssetsMax;
    @TableField(exist = false)
    private Double collectiveAssetsMin;

}

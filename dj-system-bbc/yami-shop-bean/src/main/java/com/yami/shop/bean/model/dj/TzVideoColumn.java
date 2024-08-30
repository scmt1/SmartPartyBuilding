package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 活动阵地信息表
 * </p>
 *
 * @author mike
 * @since 2022-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzVideoColumn", description="视频栏目管理表")
public class TzVideoColumn implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "排序")
    private Integer orders;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "栏目名称")
    private String name;
    @ApiModelProperty(value = "栏目编码")
    private String code;
    @ApiModelProperty(value = "创建人")
    private String createBy;
    @ApiModelProperty(value = "修改人")
    private String updateBy;
    @ApiModelProperty(value = "是否删除 0否1是")
    private String delFlag;
    @ApiModelProperty(value = "上级栏目")
    private String parentName;
    @ApiModelProperty(value = "是否删除 0否1是")
    private Integer parentId;
    @ApiModelProperty(value = "1为视频栏目，2位师资库分类")
    private String type;


}

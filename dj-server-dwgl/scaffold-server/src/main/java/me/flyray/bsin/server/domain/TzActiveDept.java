package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 活动阵地信息表
 * </p>
 *
 * @author mike
 * @since 2022-12-09
 */
@Data
@TableName("tz_active_dept")
public class TzActiveDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long deptId;

    private Integer delFlag;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "阵地介绍")
    private String introduce;
    @ApiModelProperty(value = "修建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date buildTime;

    @ApiModelProperty(value = "面积")
    private Double square;

    @ApiModelProperty(value = "是否党建示范阵地（0否1是）")
    private Integer demonstrate;

    @ApiModelProperty(value = "图片列表信息")
    @TableField(exist = false)
    private List<AttachFile> attachFileList;


}

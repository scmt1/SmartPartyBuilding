package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yami.shop.bean.model.AttachFile;
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
@ApiModel(value="TzExcellentPost对象", description="")
public class TzExcellentPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "表彰区域类型")
    private String areaType;

    @ApiModelProperty(value = "填报时间")
    private String fillTime;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "民族")
    private String national;

    @ApiModelProperty(value = "出生年月")
    private String birthday;

    @ApiModelProperty(value = "籍贯")
    private String hometown;

    @ApiModelProperty(value = "工作单位及职务")
    private String workunitPosition;

    @ApiModelProperty(value = "参加工作时间")
    private String jobTime;

    @ApiModelProperty(value = "入党时间")
    private String partyTime;

    @ApiModelProperty(value = "文化程度")
    private String education;

    @ApiModelProperty(value = "职称")
    private String technical;

    @ApiModelProperty(value = "单位地址")
    private String unitAddr;

    @ApiModelProperty(value = "单位电话")
    private String unitPhone;

    @ApiModelProperty(value = "身份证号")
    private String idCard;

    @ApiModelProperty(value = "本人电话")
    private String privatePhone;

    @ApiModelProperty(value = "简历")
    private String resume;

    @ApiModelProperty(value = "曾受表彰表扬情况")
    private String commendCondition;

    @ApiModelProperty(value = "主要事迹")
    private String mainThings;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    private Integer delFlag;

    @ApiModelProperty(value = "填报单位")
    private String deptName;

    private Integer deptId;

    @ApiModelProperty(value = "状态（0审核中 1已通过 2已驳回）")
    private String status;

    @ApiModelProperty(value = "申请类型(0 优秀党员申报 1优秀党务工作者申报)")
    private String postType;


    @ApiModelProperty(value = "附件信息")
    @TableField(exist = false)
    private AttachFile attachFile;

}

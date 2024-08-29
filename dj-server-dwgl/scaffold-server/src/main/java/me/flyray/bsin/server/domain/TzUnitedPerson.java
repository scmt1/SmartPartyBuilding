package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author hxh
 * @since 2022-09-20
 */
@Data
@TableName("tz_united_person")
public class TzUnitedPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "所属统战部门id")
    private Integer unitedId;

    @ApiModelProperty(value = "统战对象")
    private String unitedObject;

    @ApiModelProperty(value = "所属统战组织名称")
    private String unitedName;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "民族")
    private String national;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;


    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "学位")
    private String degree;

    @ApiModelProperty(value = "毕业院校及专业")
    private String university;

    @ApiModelProperty(value = "职务")
    private String position;

    @ApiModelProperty(value = "专业技术职称")
    private String technology;

    @ApiModelProperty(value = "职级")
    private String level;

    @ApiModelProperty(value = "现任职时间")
    private Date officeTime;

    @ApiModelProperty(value = "政治面貌")
    private String politicalOutlook;

    @ApiModelProperty(value = "政治安排情况")
    private String politicalArrangements;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "加入何民主党派")
    private String joinParty;

    @ApiModelProperty(value = "民主党派所任职")
    private String positionParty;

    @ApiModelProperty(value = "认定时间")
    private Date identifyTime;

    @ApiModelProperty(value = "国企知联会职务")
    private String supportAssociation;

    @ApiModelProperty(value = "删除标识")
    private Integer delFlag;

}

package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @author hxh
 * @since 2023-02-24
 */
@Data
@TableName("TzTeacherInfo")
public class TzTeacherInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String delFlag;

    private Date createTime;

    private String createBy;

    private String updateBy;

    private Date updateTime;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "师资栏目分类id")
    private Integer columnId;

    @ApiModelProperty(value = "师资栏目分类名称")
    private String columnName;

    @ApiModelProperty(value = "出生年月")
    private String birthday;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "个人特长、	研究方向及	个人特长、研究方向以及突出业绩:")
    private String specialty;

    @ApiModelProperty(value = "个人简介")
    private String introduce;

    @ApiModelProperty(value = "0男1女2未知")
    private Integer sex;

    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;

    @ApiModelProperty(value = "学历学位")
    private String education;

    @ApiModelProperty(value = "工作单位及职称职务")
    private String workPosition;

    @ApiModelProperty(value = "主要讲授方向及课程")
    private String majorCourse;

    @ApiModelProperty(value = "头像地址")
    private String imgPath;


}

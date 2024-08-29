package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author hxh
 * @since 2022-09-20
 */
@Data
@TableName("tz_united_dept")
public class TzUnitedDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级编号")
    private Long parentId;

    @ApiModelProperty(value = "所有父级编号")
    private String parentIds;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "所属工会名称")
    private String parentName;

    @ApiModelProperty(value = "工会名称")
    private String name;

    @ApiModelProperty(value = "是否成立统战机构（0否1是）")
    private Integer isUnited;

    @ApiModelProperty(value = "分管领导姓名")
    private String leaderName;

    @ApiModelProperty(value = "分管领导职务")
    private String position;

    @ApiModelProperty(value = "分管领导联系电话")
    private String phone;

    @ApiModelProperty(value = "统战工作职能部门名称")
    private String functionalName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "统战工作职能部门负责人")
    private String master;

    @ApiModelProperty(value = "统战工作职能部门联系人电话")
    private String masterPhone;

    @ApiModelProperty(value = "主办人")
    private String sponsor;

    @ApiModelProperty(value = "主办人电话")
    private String sponsorPhone;

    @ApiModelProperty(value = "删除标识")
    private Integer delFlag;

    //-------------------------------------
    @ApiModelProperty(value = "是否叶子节点")
    @TableField(exist = false)
    private boolean isLeaf;

    @TableField(exist = false)
    private JcxfSysDept jcxfSysDept;
}

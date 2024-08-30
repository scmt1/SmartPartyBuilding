package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
 *
 * </p>
 *
 * @author ycy
 * @since 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="DevelopParty对象", description="")
public class DevelopParty implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "党员id")
    private Integer partyMemberId;

    @ApiModelProperty(value = "毕业院校和专业")
    private String collegeMajor;

    @ApiModelProperty(value = "家庭住址")
    private String homeAddr;

    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "发展纪实阶段")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String developState;

    @ApiModelProperty(value = "提交入党申请书时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date submitDate;

    @ApiModelProperty(value = "列为入党积极分子时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date activeDate;

    @ApiModelProperty(value = "确定为发展对象时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date developDate;

    @ApiModelProperty(value = "讨论为预备党员时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date preDate;

    @ApiModelProperty(value = "成为正式党员时间")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Date officialDate;

    @ApiModelProperty(value = "所在组织变更情况")
    private String bak;

    @ApiModelProperty(value = "提醒")
    private String notice;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "是否是返乡农民工（1是0否）")
    private String isReturnWorker;

    @ApiModelProperty(value = "是否延期（1是0否）")
    private String isLate;

    @ApiModelProperty(value = "操作人")
    private String updateBy;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "民族")
    private String national;

    @ApiModelProperty(value = "生日")
    private Date birthday;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "组织id")
    private Long deptId;

    @ApiModelProperty(value = "归属组织名称")
    private String deptName;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "工作岗位")
    private String workPosition;

}

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
import java.util.List;

/**
 * <p>
 * 组织生活表（三会一课等相关信息）
 * </p>
 *
 * @author mike
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzOrganizationLife对象", description="组织生活表（三会一课等相关信息）")
public class TzOrganizationLife implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer deptId;

    @ApiModelProperty(value = "发起部门")
    private String deptName;

    @ApiModelProperty(value = "会议名称")
    private String meetingName;

    @ApiModelProperty(value = "会议类型（1三会一课(固定党日)，2组织生活会（民主评议党员））")
    private String meetingType;

    @ApiModelProperty(value = "会议开始时间")
    private String startTime;

    @ApiModelProperty(value = "会议结束时间 ")
    private String endTime;

    @ApiModelProperty(value = "会议状态（1进行中、2已结束、3已取消）")
    private String meetingStatus;

    @ApiModelProperty(value = "发布时间")
    private Date postTime;

    @ApiModelProperty(value = "最后一次编辑时间")
    private Date lastEditTime;

    @ApiModelProperty(value = "创建人id")
    private Integer createdId;

    @ApiModelProperty(value = "创建人姓名")
    private String createdName;

    @ApiModelProperty(value = "编辑人id")
    private Integer editId;

    @ApiModelProperty(value = "编辑人姓名")
    private String editName;

    @ApiModelProperty(value = "党小组名称")
    private String partyName;

    @ApiModelProperty(value = "主持人")
    private String host;

    @ApiModelProperty(value = "记录人")
    private String record;

    @ApiModelProperty(value = "参加人员")
    private String joins;

    @ApiModelProperty(value = "缺席人员")
    private String absence;

    @ApiModelProperty(value = "应到人数")
    private String shouldCount;

    @ApiModelProperty(value = "实到人数")
    private String actuallyCount;

    @ApiModelProperty(value = "会议记录")
    private String meetingRecord;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "会议记录")
    private String addr;

    @ApiModelProperty(value = "会议记录")
    private String meetingContent;

    @ApiModelProperty(value = "流动参会人员")
    private String flow;

    @ApiModelProperty(value = "组织类型")
    @TableField(exist = false)
    private String organizationType;

    @ApiModelProperty(value = "是否是退休支部（0否1是）")
    @TableField(exist = false)
    private String veteran;

    @TableField(exist = false)
    private List<AttachFile> attachFile;
    @ApiModelProperty(value = "该部门应到人员名单")
    @TableField(exist = false)
    private List<String> nameList;
    @ApiModelProperty(value = "用户名")
    @TableField(exist = false)
    private String username;


}

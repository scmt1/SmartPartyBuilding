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
 * 党员表
 * </p>
 *
 * @author ycy
 * @since 2022-10-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="PartyMember对象", description="党员表")
public class PartyMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "归属部门")
    private Long deptId;

    @ApiModelProperty(value = "归属部门")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "党龄")
    @TableField(exist = false)
    private Integer partyAge;

    @ApiModelProperty(value = "农民工党建 外部组织lid")
    private String deptOutId;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "身份证")
    private String idcard;

    @ApiModelProperty(value = "是否为农民工（0否1是）")
    private Integer isFamer;

    @ApiModelProperty(value = "性别(0:未知,1:男,2:女)")
    private Integer sex;

    @ApiModelProperty(value = "民族")
    private String national;

    @ApiModelProperty(value = "出生日期")
    private Date birthday;

    @ApiModelProperty(value = "学历")
    private String education;

    @ApiModelProperty(value = "加入党组织日期")
    private Date partyTime;

    @ApiModelProperty(value = "转为正式党员日期")
    private Date transferTime;

    @ApiModelProperty(value = "所在党支部职务")
    private Integer position;

    @ApiModelProperty(value = "人员类别(正式党员、预备党员)")
    private Integer personType;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "座机号码")
    private String telephone;

    @ApiModelProperty(value = "家庭住址")
    private String address;

    @ApiModelProperty(value = "党籍状态(正常=1、保留党籍=10、恢复党籍=11、停止党籍=12、开除党籍=13)")
    private Integer partyState;

    private Date partyStateDate;

    private Date reserveTime;

    private Date recoverTime;

    private Date stopTime;

    private Date dismissTime;

    @ApiModelProperty(value = "是否失联党员(0:否,1:是)")
    private String isLost;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    @ApiModelProperty(value = "失去联系日期")
    private Date lostTime;

    @ApiModelProperty(value = "是否流动党员(0:否,1:是)")
    private String isFlow;

    @ApiModelProperty(value = "外出流向")
    private String flowPlace;

    @ApiModelProperty(value = "工作岗位")
    private String workPosition;

    @ApiModelProperty(value = "党员照片")
    private String avatar;

    @ApiModelProperty(value = "app头像")
    private String profilePhoto;

    @ApiModelProperty(value = "创建者")
    private Long createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    private Long updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    @ApiModelProperty(value = "党员标签")
    private String partyMemberLabel;

    @ApiModelProperty(value = "是否忽略身份证校验(0:否,1:是)")
    private Integer isIgnore;

    @ApiModelProperty(value = "手机登陆账户时间")
    private Date appLoginDate;

    private String openid;

    private Date appModifyLoginDate;

    @ApiModelProperty(value = "是否第一书记(0:否,1:是)")
    private String isFirstSecretary;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "区域ID")
    private String areaId;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    @ApiModelProperty(value = "工作单位")
    private String workUnit;

    @ApiModelProperty(value = "是否为流入党员")
    private String isFloatIn;

    @ApiModelProperty(value = "流入地")
    private String floatInPlace;

    @ApiModelProperty(value = "是否困难党员")
    private String isSuffer;

    @ApiModelProperty(value = "是否贫困户")
    private String isPoverty;

    private String isOutsideCity;

    @ApiModelProperty(value = "应缴费金额")
    private Double payMoney;

    @ApiModelProperty(value = "同步主键(泸县的主键)")
    private Long proxyId;

    @ApiModelProperty(value = "姓氏笔画排序")
    private Long familySort;

    @ApiModelProperty(value = "附件信息")
    @TableField(exist = false)
    private List<AttachFile> attachFiles;


    @ApiModelProperty(value = "需要删除的附件ids")
    @TableField(exist = false)
    private String[] ids;

    @ApiModelProperty(value = "激活数量")
    @TableField(exist = false)
    private Integer active;

    @ApiModelProperty(value = "未激活数量")
    @TableField(exist = false)
    private Integer unActive;

    @ApiModelProperty(value = "是否激活 0未激活 1已激活")
    @TableField(exist = false)
    private Integer isActive;

    @ApiModelProperty(value = "流动状态")
    private String floatingStatus;
    @ApiModelProperty(value = "流动类型")
    private String floatingOutType;
    @ApiModelProperty(value = "流出日期")
    private Date flowDate;
    @ApiModelProperty(value = "流入日期")
    private Date floatDate;
    @ApiModelProperty(value = "从事职业")
    private String flowType;
    @ApiModelProperty(value = "是否还处于发展中党员(0否1是，是的话正常查询就不显示该记录)")
    private String isDevelop;

    @TableField(exist = false)
    private String key;
    @TableField(exist = false)
    private String label;

}

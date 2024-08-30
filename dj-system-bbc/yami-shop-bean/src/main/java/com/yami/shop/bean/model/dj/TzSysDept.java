package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yami.shop.bean.model.AttachFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("tz_sys_dept")
public class TzSysDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "酒城特有组织机构编码")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级编号")
    private Long parentId;

    @ApiModelProperty(value = "所有父级编号")
    private String parentIds;

    @ApiModelProperty(value = "区域编码")
    private String code;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "机构类型")
    private Integer type;

    @ApiModelProperty(value = "行政类型")
    private Integer deptType;

    private Integer finallySort;

    @ApiModelProperty(value = "机构等级")
    private String grade;

    @ApiModelProperty(value = "联系地址")
    private String address;

    @ApiModelProperty(value = "邮政编码")
    private String zipCode;

    @ApiModelProperty(value = "负责人")
    private String master;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "传真")
    private String fax;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateDate;

    @ApiModelProperty(value = "删除标记")
    private Integer delFlag;

    @ApiModelProperty(value = "党组织所在单位情况")
    private Integer partyOrgSituation;

    @ApiModelProperty(value = "党组织联系人")
    private String partyOrgContact;

    @ApiModelProperty(value = "党组织书记")
    private String partyOrgManager;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "单位类别")
    private Integer partyOrgType;

    @ApiModelProperty(value = "单位建立组织情况")
    private Integer unitOrgSituation;

    @ApiModelProperty(value = "党组织所在单位代码")
    private String unitCode;

    @ApiModelProperty(value = "组建方式")
    private Integer partyOrgCreateType;

    @ApiModelProperty(value = "批准建立日期")
    private Date approvalCreateTime;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "纬度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "组织标签")
    private String deptLabel;

    private String deptPhoto;

    @ApiModelProperty(value = "支部介绍")
    private String deptIntroduction;

    @ApiModelProperty(value = "组织类型")
    private Integer organizationType;

    @ApiModelProperty(value = "示范校")
    private Integer demonstrativeSchool;

    @ApiModelProperty(value = "示范党组织")
    private Integer demonstrativePartyOrg;

    @ApiModelProperty(value = "机构所属区域")
    private String areaId;

    @ApiModelProperty(value = "党组织代码")
    private String orgCode;

    @ApiModelProperty(value = "上级党组织编码")
    private String parentOrgCode;

    private Integer sort;

    @ApiModelProperty(value = "是否撤消支部 1 未撤消 0 为未撤消")
    private Integer undoFlag;

    @ApiModelProperty(value = "机构撤销时间")
    private Date undoDate;

    private String oldId;

    private Long proxyId;

    @ApiModelProperty(value = "机构名拼音首字母")
    private String pyName;

    @ApiModelProperty(value = "商户号")
    private String merchant;

    @ApiModelProperty(value = "商户审批状态")
    private String merstatus;

    @ApiModelProperty(value = "是否退休支部")
    private String veteran;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "部门地区")
    private String deptRegion;
    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "创建日期")
    @TableField(exist = false)
    private Long partyNum;

    @ApiModelProperty(value = "上级部门")
    @TableField(exist = false)
    private String parentDept;


    @TableField(exist = false)
    private Date thisChangeTime;


    @TableField(exist = false)
    private Date thisFinishTime;

    @ApiModelProperty(value = "支部简介图片列表")
    @TableField(exist = false)
    private List<AttachFile> attachFileList;
}

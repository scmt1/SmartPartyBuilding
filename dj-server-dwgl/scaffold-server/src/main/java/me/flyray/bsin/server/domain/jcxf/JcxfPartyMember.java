package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.utils.DecryptTransaction;
import me.flyray.bsin.server.utils.EncryptTransaction;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("party_member")
public class JcxfPartyMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "登录名")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "归属部门")
    private Long deptId;

    @ApiModelProperty(value = "农民工党建 外部组织lid")
    private String deptOutId;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "身份证")
    @EncryptTransaction
    @DecryptTransaction
    private String idcard;

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
    @EncryptTransaction
    @DecryptTransaction
    private String phone;

    @ApiModelProperty(value = "没有加密的手机号码")
    @TableField(exist = false)
    private String mobile;

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
    @TableLogic(value = "0", delval = "1")
    private Boolean delFlag;

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
    private Integer areaId;

    @ApiModelProperty(value = "区域名称")
    private String areaName;

    private String regionIds;

    @ApiModelProperty(value = "租户id")
    private String tenantId;


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

    @ApiModelProperty(value = "同步主键(泸县的主键)")
    private Long proxyId;

    @ApiModelProperty(value = "姓氏笔画排序")
    private Long familySort;

    @ApiModelProperty(value = "是否是党务工作者（0否1是）")
    private String isPartyWorker;

    @ApiModelProperty(value = "党务工作者标签，以为,分隔")
    private String partyWorkerTips;

    @ApiModelProperty(value = "按钮权限")
    private String buttonRole;

    @ApiModelProperty(value = "籍贯")
    private String nativePlace;

    //-----------------------------------------------

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

    @TableField(exist = false)
    private String key;

    @TableField(exist = false)
    private String label;

    @TableField(exist = false)
    private String today;

    @ApiModelProperty(value = "归属部门")
    @TableField(exist = false)
    private String deptName;

    @ApiModelProperty(value = "党龄")
    @TableField(exist = false)
    private Integer politicsAge;

    @ApiModelProperty(value = "岁数")
    @TableField(exist = false)
    private Integer Age;

    @ApiModelProperty(value = "党龄")
    @TableField(exist = false)
    private Integer partyAge;

    @ApiModelProperty(value = "支付年月")
    @TableField(exist = false)
    private String payMonth;

    @TableField(exist = false)
    @ApiModelProperty(value = "应缴金额")
    private BigDecimal shouldPay;
}

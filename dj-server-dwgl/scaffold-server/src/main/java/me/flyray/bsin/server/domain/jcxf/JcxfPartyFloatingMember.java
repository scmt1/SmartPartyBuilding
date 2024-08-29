package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("party_floating_member")
public class JcxfPartyFloatingMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer floatingType;

    private Long partyId;

    private String realName;

    private String sex;

    private String national;

    private String idcard;

    private String phone;

    private String outType;

    private String floatingStatus;

    private Long deptId;

    private String floatingPlace;

    private Date floatingDate;

    private String floatingBack;

    private String working;

    private Long createBy;

    private Date createDate;

    private Long updateBy;

    private Date updateDate;

    private Boolean delFlag;

    private BigDecimal floatingLongitude;

    private BigDecimal floatingLatitude;

    private String floatingProvince;

    private String floatingCity;

    private BigDecimal cityLongitude;

    private BigDecimal cityLatitude;

    private Integer familySort;

    private String flowTargetDept;

    private String flowReason;

    private String regionIds;

    //------------------------------

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private Long areaId;

}

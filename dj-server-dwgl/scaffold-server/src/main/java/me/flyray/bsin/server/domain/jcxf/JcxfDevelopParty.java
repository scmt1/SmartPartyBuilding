package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.utils.DecryptTransaction;
import me.flyray.bsin.server.utils.EncryptTransaction;

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
@TableName("party_member_develop")
public class JcxfDevelopParty implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long deptId;

    private String realName;

    @EncryptTransaction
    @DecryptTransaction
    private String idcard;

    private Integer sex;

    private Integer national;

    private Date birthday;

    private String education;

    private Date partyTime;

    private Date transferTime;

    private Integer personType;

    @EncryptTransaction
    @DecryptTransaction
    private String phone;

    private String telephone;

    private String address;

    private String workPosition;

    private String avatar;

    private String profilePhoto;

    private Integer createBy;

    private Date createDate;

    private Integer updateBy;

    private Date updateDate;

    private Integer delFlag;

    private Integer isIgnore;

    private String workUnit;

    private Date submitDate;

    private Date activeDate;

    private Date developDate;

    private String  changeOrganization;

    private Integer isReturnWorkers;

    private String collagesMajors;

    private Long partyMemberId;

    private Integer postponeFlag;

    private String notice;

    private String isTrain;

    private Date trainStartTime;

    private Date trainEndTime;

    private String isQualified;

    //--------------------------------------

    @TableField(exist = false)
    private String deptName;

}

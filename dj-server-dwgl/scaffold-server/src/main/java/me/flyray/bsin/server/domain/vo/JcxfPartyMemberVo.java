package me.flyray.bsin.server.domain.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.AttachFile;
import me.flyray.bsin.server.utils.DecryptTransaction;
import me.flyray.bsin.server.utils.EncryptTransaction;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("party_member")
public class JcxfPartyMemberVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "所在党支部职务")
    private Integer position;

    @ApiModelProperty(value = "身份证号")
    private String idcard;

    @ApiModelProperty(value = "工作岗位")
    private String workPosition;
}

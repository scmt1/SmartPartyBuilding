package me.flyray.bsin.server.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class JcxfDevelopPartyMemberVo implements Serializable {

    private Long id;

    @ApiModelProperty(value = "姓名")
    private String realName;

    @ApiModelProperty(value = "手机号码")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idcard;
}

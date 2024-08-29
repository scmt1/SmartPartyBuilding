package me.flyray.bsin.server.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class PayFeeDetailsVo implements Serializable {

    @ApiModelProperty(value = "应缴纳总党费")
    private Double allPartyMoney;

    @ApiModelProperty(value = "实缴总党费")
    private Double alreadyPartyMoney;

    @ApiModelProperty(value = "应缴总人数")
    private Integer allPartyNum;

    @ApiModelProperty(value = "实缴总人数")
    private Integer alreadyPartyNum;

    @ApiModelProperty(value = "未缴总党费")
    private Double notPartyMoney;

    @ApiModelProperty(value = "未缴总人数")
    private Integer notPartyNum;
}

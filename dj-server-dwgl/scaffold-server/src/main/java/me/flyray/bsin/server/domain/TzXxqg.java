package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@TableName("tz_xxqg")
public class TzXxqg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "部门id")
    private Long deptId;

    private Integer delFlag;

    @ApiModelProperty(value = "组织名称")
    private String deptName;
    @ApiModelProperty(value = "学员总数")
    private Integer memberCount;
    @ApiModelProperty(value = "激活总数")
    private Integer activationCount;
    @ApiModelProperty(value = "活跃学员")
    private Integer activeCount;
    @ApiModelProperty(value = "参与度")
    private String joinPercent;
    @ApiModelProperty(value = "活跃学员参与度")
    private String activePercent;
    @ApiModelProperty(value = "总获得积分")
    private String totalScore;
    @ApiModelProperty(value = "人均积分")
    private String averageScore;
    @ApiModelProperty(value = "是否党员填写率")
    private BigDecimal isPartyPercent;

    @ApiModelProperty(value = "导入时间")
    private Date importTime;
    @ApiModelProperty(value = "排序")
    private Integer sort;


}

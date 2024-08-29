package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfBranchReelection;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("cq_organization")
public class CqSysOrg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级编号")
    private Integer parentId;

    private String orgLevel;

    @ApiModelProperty(value = "机构名称")
    private String name;

    @ApiModelProperty(value = "是否叶子节点")
    @TableField(exist = false)
    private boolean isLeaf;
}

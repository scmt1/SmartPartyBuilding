package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.flyray.bsin.server.domain.jcxf.JcxfSysDept;

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
@TableName("tz_trade_union")
public class TzTradeUnion implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父级编号")
    private Long parentId;

    @ApiModelProperty(value = "所有父级编号")
    private String parentIds;

    @ApiModelProperty(value = "部门id")
    private Long deptId;

    @ApiModelProperty(value = "所属工会名称")
    private String parentName;

    @ApiModelProperty(value = "工会名称")
    private String name;

    @ApiModelProperty(value = "工会类型")
    private Integer type;

    @ApiModelProperty(value = "工会负责人")
    private String master;

    @ApiModelProperty(value = "担任职务")
    private String position;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "工会总人数")
    private Integer personCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除标识")
    private Integer delFlag;

    //-------------------------------------
    @ApiModelProperty(value = "是否叶子节点")
    @TableField(exist = false)
    private boolean isLeaf;


    @ApiModelProperty(value = "部门信息")
    @TableField(exist = false)
    private JcxfSysDept jcxfSysDept;

}

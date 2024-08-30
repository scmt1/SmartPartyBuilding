package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 党小组管理表
 * </p>
 *
 * @author mike
 * @since 2022-12-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzGroupDept对象", description="党小组管理表")
public class TzGroupDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer delFlag;

    private String deptId;

    private String groupName;

    private String headmanId;

    @ApiModelProperty(value = "党小组长")
    private String headman;

    @ApiModelProperty(value = "党小组建立时间")
    private Date buildTime;

    @ApiModelProperty(value = "小组人数")
    private String personCount;

    @ApiModelProperty(value = "最后一次编辑时间")
    private Date finalEditTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "记录人")
    private String recorder;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "党小组成员的id集合，以','分隔")
    private String partyMemberIds;


}

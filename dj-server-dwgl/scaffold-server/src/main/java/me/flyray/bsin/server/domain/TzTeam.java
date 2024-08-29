package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 机构表
 * </p>
 *
 * @author hxh
 * @since 2022-09-20
 */
@Data
@TableName("tz_team")
public class TzTeam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "团组织书记")
    private Long deptId;

    @ApiModelProperty(value = "团组织书记")
    private String master;

    @ApiModelProperty(value = "团组织名称")
    private String name;

    @ApiModelProperty(value = "联系电话")
    private String phone;

    @ApiModelProperty(value = "团组织联系人")
    private String contacts;

    @ApiModelProperty(value = "联系人电话")
    private String  contactsPhone;

    @ApiModelProperty(value = "团组织地址")
    private String addr;

    @ApiModelProperty(value = "团组织人数")
    private String personCount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}

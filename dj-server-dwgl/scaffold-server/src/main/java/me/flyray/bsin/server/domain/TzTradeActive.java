package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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
@TableName("tz_trade_active")
public class TzTradeActive implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "所属工会id")
    private Integer tradeId;

    @ApiModelProperty(value = "所属工会名字")
    private String tradeName;

    @ApiModelProperty(value = "活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动描述")
    private String description;

    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "删除标识")
    private Integer delFlag;

    @ApiModelProperty(value = "文件地址")
    private String fileUrl;

    //------------------------------------

    @ApiModelProperty(value = "附件列表")
    @TableField(exist = false)
    private List<AttachFile> attachFileList;

    @ApiModelProperty(value = "图片列表")
    @TableField(exist = false)
    private List<AttachFile> imageFileList;
}

package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 具体投票项
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzVoteDetail对象", description="具体投票项")
public class TzVoteDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "投票id")
    private Integer voteId;

    @ApiModelProperty(value = "图片地址")
    private String imgPath;

    @ApiModelProperty(value = "名称")
    private String nameItem;

    @ApiModelProperty(value = "票数")
    private Integer numberVote;

    @ApiModelProperty(value = "投票人")
    private String voteBy;
    @ApiModelProperty(value = "投票介绍")
    private String voteIntro;
    @ApiModelProperty(value = "排名")
    private Integer orders;


}

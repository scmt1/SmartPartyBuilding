package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 人员会议中间表
 * </p>
 *
 * @author mike
 * @since 2022-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzPartyMeeting对象", description="人员会议中间表")
public class TzPartyMeeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "人员id")
    private Integer personId;

    @ApiModelProperty(value = "人员类型")
    private String personType;

    @ApiModelProperty(value = "会议id")
    private Integer meetingId;


}

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
 *
 * </p>
 *
 * @author hxh
 * @since 2023-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzMeetingRegister对象", description="")
public class TzMeetingRegister implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String userId;
    private String meetingId;
    @ApiModelProperty(value = "0签到1请假")
    private String type;


}

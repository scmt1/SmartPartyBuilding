package com.yami.shop.bean.model.dj;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 专项答题表
 * </p>
 *
 * @author hxh
 * @since 2023-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="TzTopic对象", description="专项答题表")
public class TzTopic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "题目主题")
    private String topicName;

    @ApiModelProperty(value = "学分")
    private Integer records;

    @ApiModelProperty(value = "发布时间")
    private Date postTime;

    @ApiModelProperty(value = "是否删除")
    private Integer delFlag;

    @ApiModelProperty(value = "主题类型")
    private String topicType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建人id")
    private String createId;
    @ApiModelProperty(value = "创建人")
    private String createBy;

    @ApiModelProperty(value = "修改人id")
    private String updateId;

    @ApiModelProperty(value = "修改人")
    private String updateBy;

    private Integer deptId;

    private String deptName;

    private Integer status;


}

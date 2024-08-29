package me.flyray.bsin.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
@ApiModel(value="TzVoteUser对象", description="")
public class TzVoteUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    private Integer voteId;
    private Integer voteDetailId;
    private Date createTime;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String avatar;
}

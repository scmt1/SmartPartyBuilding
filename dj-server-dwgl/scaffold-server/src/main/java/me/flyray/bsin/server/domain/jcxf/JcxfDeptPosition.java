package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("dept_position")
public class JcxfDeptPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String area;

    private Long deptId;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private Boolean delFlag;

    //阵地简介
    private String positionIntroduction;

    private Integer demonstrate;

}

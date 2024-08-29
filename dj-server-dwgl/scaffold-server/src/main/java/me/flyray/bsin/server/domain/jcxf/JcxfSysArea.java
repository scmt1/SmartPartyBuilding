package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("sys_area")
public class JcxfSysArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField(value = "pId")
    private Long pId;

    private String mergerIds;

    private String shortName;

    private String level;

    private String cityCode;

    private String zipCode;

    private String  mergerName;

    private String lng;

    private String lat;

    private String pinyin;

    private String firstChar;

    private String shortHand;

    private Boolean delFlag;

    @TableField(exist = false)
    List<JcxfSysArea> children;

}

package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_dictionary")
public class JcxfSysDictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String code;
    //编码类型
    private String codeName;
    //code编码
    private String detail;
    //编码详细信息
    private String detailName;
    //排序
    private Integer sort;

    private Integer delFlag;


}

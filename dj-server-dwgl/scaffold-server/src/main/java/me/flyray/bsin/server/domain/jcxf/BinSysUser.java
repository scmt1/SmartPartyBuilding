package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_user")
public class BinSysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id")
    private String userId;

    private String userName;

}

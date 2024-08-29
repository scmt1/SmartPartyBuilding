package me.flyray.bsin.server.domain.bsin;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("sys_tenant")
public class BsinSysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "tenant_id")
    private String tenantId;

    private Integer status;

    private String mchNo;

    private String mchAppId;

    private String apiKey;

}

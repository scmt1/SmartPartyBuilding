package me.flyray.bsin.server.domain.jcxf;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "party_money_dues")
public class JcxfPartyMoneyDues implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //年份
    private String payYear;
    //季度
    private String payQuarter;

    //党员账号id
    private Long partyId;

    //部门id
    private Long deptId;
    //订单号
    private String payOrder;
    //凭证
    private String payPic;
    //审核部门id
    private Long payParent;

    //父级缴费单ID
    private Long parentId;
    //岗位id
    private String postId;
    //岗位名称
    private String postName;
    //工资
    private String salary;

}

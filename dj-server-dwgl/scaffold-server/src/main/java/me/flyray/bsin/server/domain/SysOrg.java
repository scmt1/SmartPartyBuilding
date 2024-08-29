package me.flyray.bsin.server.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysOrg implements Serializable {
    /**
     * 机构id
     */
    private String orgId;

    /**
     * 机构编码
     */
    private String orgCode;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 逻辑删除 0，未删除 1，已删除
     */
    private Integer delFlag;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 父级id
     */
    private String parentId;

    /**
     * 机构级别
     */
    private String level;

    /**
     * 机构类型 1、公司 2、部门 3、小组 4、其他
     */
    private Integer type;

    /**
     * 负责人
     */
    private String leader;

    /**
     * 办公电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 创建时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(shape =JsonFormat.Shape.STRING,pattern ="yyyy-MM-dd HH:mm:ss",timezone ="GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 描述
     */
    private String remark;
}

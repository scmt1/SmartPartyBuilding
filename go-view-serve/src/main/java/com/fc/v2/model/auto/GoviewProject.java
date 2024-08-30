package com.fc.v2.model.auto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class GoviewProject implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "主键")
	private String id;

	@ApiModelProperty(value = "项目名称")
	private String projectName;

	@ApiModelProperty(value = "项目状态[-1未发布,1发布]")
	private Integer state;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "创建人id")
	private String createUserId;

	@ApiModelProperty(value = "删除状态[1删除,-1未删除]")
	private Integer isDelete;

	@ApiModelProperty(value = "首页图片")
	private String indexImage;

	@ApiModelProperty(value = "项目介绍")
	private String remarks;

	@ApiModelProperty(value = "租户id")
	private String tenantId;

	@ApiModelProperty(value = "机构id")
	private String orgId;

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id =  id;
	}
	@JsonProperty("projectName")
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName =  projectName;
	}
	@JsonProperty("state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state =  state;
	}
	@JsonProperty("createTime")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime =  createTime;
	}
	@JsonProperty("createUserId")
	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId =  createUserId;
	}
	@JsonProperty("isDelete")
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete =  isDelete;
	}
	@JsonProperty("indexImage")
	public String getIndexImage() {
		return indexImage;
	}

	public void setIndexImage(String indexImage) {
		this.indexImage =  indexImage;
	}
	@JsonProperty("remarks")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks =  remarks;
	}

	@JsonProperty("tenantId")
	public String getTenantId() { return tenantId;}

	public void setTenantId(String tenantId) {
		this.tenantId =  tenantId;
	}

	@JsonProperty("orgId")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId =  orgId;
	}


	public GoviewProject(String id,String projectName,Integer state,Date createTime,String createUserId,Integer isDelete,String indexImage,String remarks,String tenantId,String orgId) {

		this.id = id;

		this.projectName = projectName;

		this.state = state;

		this.createTime = createTime;

		this.createUserId = createUserId;

		this.isDelete = isDelete;

		this.indexImage = indexImage;

		this.remarks = remarks;
		this.tenantId = tenantId;
		this.orgId = orgId;

	}

	public GoviewProject() {
	    super();
	}

	public String dateToStringConvert(Date date) {
		if(date!=null) {
			return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
		}
		return "";
	}


}

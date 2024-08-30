package com.fc.v2.model.auto;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.date.DateUtil;
import java.util.Date;

public class GoviewProjectData implements Serializable {
    private static final long serialVersionUID = 1L;

	
	@ApiModelProperty(value = "主键")
	private String id;
	
	@ApiModelProperty(value = "项目id")
	private String projectId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	@ApiModelProperty(value = "创建时间")
	private Date createTime;
	
	@ApiModelProperty(value = "创建人id")
	private String createUserId;
	
	@ApiModelProperty(value = "存储数据")
	private byte[] content;
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id =  id;
	}
	@JsonProperty("projectId")
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId =  projectId;
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
	@JsonProperty("content")
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content =  content;
	}

										
	public GoviewProjectData(String id,String projectId,Date createTime,String createUserId,byte[] content) {
				
		this.id = id;
				
		this.projectId = projectId;
				
		this.createTime = createTime;
				
		this.createUserId = createUserId;
				
		this.content = content;
				
	}

	public GoviewProjectData() {
	    super();
	}

	public String dateToStringConvert(Date date) {
		if(date!=null) {
			return DateUtil.format(date, "yyyy-MM-dd HH:mm:ss");
		}
		return "";
	}
	
	/**
     * bate数组转成str
     * @return
     * @author fuce
     * @Date 2021年2月26日 上午1:55:24
     */
	public String getDataToStr() {
		byte[] bs= getContent();
		String str="二进制转换错误";
		/*try {
			str = new String(bs, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
*/
		str = new String(bs);
		return str;
	}
	

}
package com.fc.v2.model.custom;

import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

public class MagicHttp {
	/**
	 * 请求url
	 */
	@ApiModelProperty(value = "请求url")
	private String url;
	/**
	 * 请求类型 get post
	 */
	@ApiModelProperty(value = "请求类型 get or post")
	private String requestType;

	@ApiModelProperty(value = "head参数")
	private Map<String, String> head;
	
	@ApiModelProperty(value = "body 内容")
	private String body;
	
	@ApiModelProperty(value = "超时时间为0 不超时")
	private Integer timeout;
	
	@ApiModelProperty(value = "form表单")
	private Map<String, Object> form;

	@ApiModelProperty(value = "cookie")
	private String cookie;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public Map<String, String> getHead() {
		return head;
	}

	public void setHead(Map<String, String> head) {
		this.head = head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Map<String, Object> getForm() {
		return form;
	}

	public void setForm(Map<String, Object> form) {
		this.form = form;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.ouputjson.getrandommemories;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.util.List;

/**
 *
 * @author sangvv2
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
	"statusCode",
	"content",
	"expireTime",
	"operationId"
})
public class GetRandomMemories {

	@JsonProperty("statusCode")
	private Integer statusCode;
	@JsonProperty("content")
	private List<ContentItem> content;
	@JsonProperty("expireTime")
	private Long expireTime;
	@JsonProperty("operationId")
	private Integer operationId;

	@JsonProperty("statusCode")
	public Integer getStatusCode() {
		return statusCode;
	}

	@JsonProperty("statusCode")
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@JsonProperty("content")
	public List<ContentItem> getContent() {
		return content;
	}

	@JsonProperty("content")
	public void setContent(List<ContentItem> content) {
		this.content = content;
	}

	@JsonProperty("expireTime")
	public Long getExpireTime() {
		return expireTime;
	}

	@JsonProperty("expireTime")
	public void setExpireTime(Long expireTime) {
		this.expireTime = expireTime;
	}

	@JsonProperty("operationId")
	public Integer getOperationId() {
		return operationId;
	}

	@JsonProperty("operationId")
	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.ouputjson.setgroupsfollowmemory;

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
	"groupHasMemory",
	"failed"
})
public class Content {

	@JsonProperty("statusCode")
	private Integer statusCode;
	@JsonProperty("groupHasMemory")
	private List<Integer> groupHasMemory;
	@JsonProperty("failed")
	private List<Integer> failed;

	@JsonProperty("statusCode")
	public Integer getStatusCode() {
		return statusCode;
	}

	@JsonProperty("statusCode")
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@JsonProperty("groupHasMemory")
	public List<Integer> getGroupHasMemory() {
		return groupHasMemory;
	}

	@JsonProperty("groupHasMemory")
	public void setGroupHasMemory(List<Integer> groupHasMemory) {
		this.groupHasMemory = groupHasMemory;
	}

	@JsonProperty("failed")
	public List<Integer> getFailed() {
		return failed;
	}

	@JsonProperty("failed")
	public void setFailed(List<Integer> failed) {
		this.failed = failed;
	}

}

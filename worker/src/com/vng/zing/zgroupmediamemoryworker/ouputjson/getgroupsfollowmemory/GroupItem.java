/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.ouputjson.getgroupsfollowmemory;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author sangvv2
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
	"groupId",
	"hasMemory"
})
public class GroupItem {

	@JsonProperty("groupId")
	private Integer groupId;
	@JsonProperty("hasMemory")
	private Boolean hasMemory;

	public GroupItem(Integer groupId, Boolean hasMemory) {
		this.groupId = groupId;
		this.hasMemory = hasMemory;
	}

	@JsonProperty("groupId")
	public Integer getGroupId() {
		return groupId;
	}

	@JsonProperty("groupId")
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@JsonProperty("hasMemory")
	public Boolean getHasMemory() {
		return hasMemory;
	}

	@JsonProperty("hasMemory")
	public void setHasMemory(Boolean hasMemory) {
		this.hasMemory = hasMemory;
	}
}

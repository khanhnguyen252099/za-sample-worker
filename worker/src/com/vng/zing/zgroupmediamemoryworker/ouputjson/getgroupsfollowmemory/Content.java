/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.ouputjson.getgroupsfollowmemory;

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
	"total",
	"isLoadMore",
	"lastId",
	"groups"
})
public class Content {

	@JsonProperty("total")
	private Integer total;
	@JsonProperty("isLoadMore")
	private Boolean isLoadMore;
	@JsonProperty("lastId")
	private Integer lastId;
	@JsonProperty("groups")
	private List<GroupItem> groups;

	@JsonProperty("total")
	public Integer getTotal() {
		return total;
	}

	@JsonProperty("total")
	public void setTotal(Integer total) {
		this.total = total;
	}

	@JsonProperty("isLoadMore")
	public Boolean getIsLoadMore() {
		return isLoadMore;
	}

	@JsonProperty("isLoadMore")
	public void setIsLoadMore(Boolean isLoadMore) {
		this.isLoadMore = isLoadMore;
	}

	@JsonProperty("lastId")
	public Integer getLastId() {
		return lastId;
	}

	@JsonProperty("lastId")
	public void setLastId(Integer lastId) {
		this.lastId = lastId;
	}

	@JsonProperty("groups")
	public List<GroupItem> getGroups() {
		return groups;
	}

	@JsonProperty("groups")
	public void setGroups(List<GroupItem> groups) {
		this.groups = groups;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.ouputjson.getrandommemories;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author sangvv2
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
	"globalMsgId",
	"displayName",
	"ownerId",
	"clientMsgId",
	"width",
	"createdTime",
	"srcUrl",
	"subType",
	"hdUrl",
	"thumbUrl",
	"fileId",
	"desc",
	"height",
	"location",
	"customTitle"
})
public class ContentItem {

	@JsonProperty("globalMsgId")
	private Long globalMsgId;
	@JsonProperty("displayName")
	private String displayName;
	@JsonProperty("ownerId")
	private Integer ownerId;
	@JsonProperty("clientMsgId")
	private Long clientMsgId;
	@JsonProperty("width")
	private Integer width;
	@JsonProperty("createdTime")
	private Long createdTime;
	@JsonProperty("srcUrl")
	private String srcUrl;
	@JsonProperty("subType")
	private String subType;
	@JsonProperty("hdUrl")
	private String hdUrl;
	@JsonProperty("thumbUrl")
	private String thumbUrl;
	@JsonProperty("fileId")
	private Long fileId;
	@JsonProperty("desc")
	private String desc;
	@JsonProperty("height")
	private Integer height;
	@JsonProperty("location")
	private String location;
	@JsonProperty("customTitle")
	private CustomTitle customTitle;

	@JsonProperty("globalMsgId")
	public Long getGlobalMsgId() {
		return globalMsgId;
	}

	@JsonProperty("globalMsgId")
	public void setGlobalMsgId(Long globalMsgId) {
		this.globalMsgId = globalMsgId;
	}

	@JsonProperty("displayName")
	public String getDisplayName() {
		return displayName;
	}

	@JsonProperty("displayName")
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@JsonProperty("ownerId")
	public Integer getOwnerId() {
		return ownerId;
	}

	@JsonProperty("ownerId")
	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	@JsonProperty("clientMsgId")
	public Long getClientMsgId() {
		return clientMsgId;
	}

	@JsonProperty("clientMsgId")
	public void setClientMsgId(Long clientMsgId) {
		this.clientMsgId = clientMsgId;
	}

	@JsonProperty("width")
	public Integer getWidth() {
		return width;
	}

	@JsonProperty("width")
	public void setWidth(Integer width) {
		this.width = width;
	}

	@JsonProperty("createdTime")
	public Long getCreatedTime() {
		return createdTime;
	}

	@JsonProperty("createdTime")
	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	@JsonProperty("srcUrl")
	public String getSrcUrl() {
		return srcUrl;
	}

	@JsonProperty("srcUrl")
	public void setSrcUrl(String srcUrl) {
		this.srcUrl = srcUrl;
	}

	@JsonProperty("subType")
	public String getSubType() {
		return subType;
	}

	@JsonProperty("subType")
	public void setSubType(String subType) {
		this.subType = subType;
	}

	@JsonProperty("hdUrl")
	public String getHdUrl() {
		return hdUrl;
	}

	@JsonProperty("hdUrl")
	public void setHdUrl(String hdUrl) {
		this.hdUrl = hdUrl;
	}

	@JsonProperty("thumbUrl")
	public String getThumbUrl() {
		return thumbUrl;
	}

	@JsonProperty("thumbUrl")
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}

	@JsonProperty("fileId")
	public Long getFileId() {
		return fileId;
	}

	@JsonProperty("fileId")
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@JsonProperty("desc")
	public String getDesc() {
		return desc;
	}

	@JsonProperty("desc")
	public void setDesc(String desc) {
		this.desc = desc;
	}

	@JsonProperty("height")
	public Integer getHeight() {
		return height;
	}

	@JsonProperty("height")
	public void setHeight(Integer height) {
		this.height = height;
	}

	@JsonProperty("location")
	public String getLocation() {
		return location;
	}

	@JsonProperty("location")
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty("customTitle")
	public CustomTitle getCustomTitle() {
		return customTitle;
	}

	@JsonProperty("customTitle")
	public void setCustomTitle(CustomTitle customTitle) {
		this.customTitle = customTitle;
	}

}

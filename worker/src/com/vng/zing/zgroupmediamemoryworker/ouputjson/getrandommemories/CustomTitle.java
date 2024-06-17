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
	"en",
	"vi"
})
public class CustomTitle {

	@JsonProperty("en")
	private String en;

	@JsonProperty("vi")
	private String vi;

	@JsonProperty("en")
	public String getEn() {
		return en;
	}

	@JsonProperty("en")
	public void setEn(String en) {
		this.en = en;
	}

	@JsonProperty("vi")
	public String getVi() {
		return vi;
	}

	@JsonProperty("vi")
	public void setVi(String vi) {
		this.vi = vi;
	}

	public CustomTitle(String en, String vi) {
		this.en = en;
		this.vi = vi;
	}

	public CustomTitle() {
	}

}

package com.sowisetech.investor.request;

import org.springframework.stereotype.Component;

@Component
public class InvInterestRequest {

	private long prodId;
	private String scale;
	private long interestId;

	public long getInterestId() {
		return interestId;
	}

	public void setInterestId(long interestId) {
		this.interestId = interestId;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

}

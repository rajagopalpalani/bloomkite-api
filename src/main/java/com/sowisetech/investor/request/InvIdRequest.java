package com.sowisetech.investor.request;

import org.springframework.stereotype.Component;

@Component
public class InvIdRequest {
	private int screenId;
	private String invId;
	private long id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;

	}

}

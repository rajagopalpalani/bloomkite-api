package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class ViewIdRequest {

	private int screenId;
	private String ownerId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}

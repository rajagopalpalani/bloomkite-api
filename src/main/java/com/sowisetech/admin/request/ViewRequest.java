package com.sowisetech.admin.request;

import java.sql.Timestamp;

public class ViewRequest {

	public String viewerId;
	public String ownerId;

	public String getViewerId() {
		return viewerId;
	}

	public void setViewerId(String viewerId) {
		this.viewerId = viewerId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

}

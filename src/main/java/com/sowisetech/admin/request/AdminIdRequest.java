package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class AdminIdRequest {

	private String adminId;
	private int screenId;

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

}

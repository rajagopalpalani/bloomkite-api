package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class ModifyAdminRequest {

	private String adminId;
	private String name;
	private int screenId;

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

}

package com.sowisetech.admin.request;

import org.springframework.stereotype.Component;

@Component
public class AdmIdRequest {

	private int screenId;
	private int id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}

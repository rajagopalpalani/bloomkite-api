package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class StringIdRequest {

	int screenId;
	String id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}

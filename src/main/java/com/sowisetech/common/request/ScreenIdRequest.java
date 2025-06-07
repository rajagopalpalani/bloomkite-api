package com.sowisetech.common.request;

import org.springframework.stereotype.Component;

@Component
public class ScreenIdRequest {

	private int screenId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

}

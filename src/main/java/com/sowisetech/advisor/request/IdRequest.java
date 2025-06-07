package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class IdRequest {
	private int screenId;
	private long id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;

	}
}

package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumIdRequest {

	private int screenId;
	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

}

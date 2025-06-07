package com.sowisetech.admin.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ScreenFieldRightsRequest {

	private int screenId;
	private int user_role_id;

	private List<ScreenRightsRequest> screenRightsRequest;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public int getUser_role_id() {
		return user_role_id;
	}

	public void setUser_role_id(int user_role_id) {
		this.user_role_id = user_role_id;
	}

	public List<ScreenRightsRequest> getScreenRightsRequest() {
		return screenRightsRequest;
	}

	public void setScreenRightsRequest(List<ScreenRightsRequest> screenRightsRequest) {
		this.screenRightsRequest = screenRightsRequest;
	}

}

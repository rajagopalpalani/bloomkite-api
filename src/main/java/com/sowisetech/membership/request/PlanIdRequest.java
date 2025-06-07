package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class PlanIdRequest {

	int screenId;
	String plan_id;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

}

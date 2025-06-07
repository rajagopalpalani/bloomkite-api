package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class UpdateSubRequest {

	int screenId;
	String advId;
	String sub_id;
	String plan_id;
	int remaining_count;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public int getRemaining_count() {
		return remaining_count;
	}

	public void setRemaining_count(int remaining_count) {
		this.remaining_count = remaining_count;
	}

}

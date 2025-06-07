package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class MembershipPlanRequest {

	int screenId;
	String period;
	int interval;
	ItemRequest item;
	String plan_id;
	String planName;
	String content;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public ItemRequest getItem() {
		return item;
	}

	public void setItem(ItemRequest item) {
		this.item = item;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

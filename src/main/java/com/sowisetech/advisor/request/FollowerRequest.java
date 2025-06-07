package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class FollowerRequest {

	private int screenId;
	private String advId;
	private String userId;
	private long statusId;
	private String blockedBy;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getStatusId() {
		return statusId;
	}

	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	public String getBlockedBy() {
		return blockedBy;
	}

	public void setBlockedBy(String blockedBy) {
		this.blockedBy = blockedBy;
	}

}

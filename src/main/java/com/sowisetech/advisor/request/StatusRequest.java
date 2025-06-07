package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class StatusRequest {

	private int screenId;
	private String advId;
	private int status;
	private long followersId;
	private String reason;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getFollowersId() {
		return followersId;
	}

	public void setFollowersId(long followersId) {
		this.followersId = followersId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}

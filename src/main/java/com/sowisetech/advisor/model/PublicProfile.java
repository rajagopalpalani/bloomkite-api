package com.sowisetech.advisor.model;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

public class PublicProfile {

	private int publicProfileId;
	private String profile;
	private int workFlowStatus;
	private String advId;

	public String getProfile() {
		return profile;
	}

	public int getPublicProfileId() {
		return publicProfileId;
	}

	public void setPublicProfileId(int publicProfileId) {
		this.publicProfileId = publicProfileId;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public int getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(int workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}
}

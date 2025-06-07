package com.sowisetech.advisor.model;

import org.springframework.stereotype.Component;

public class WorkFlowStatus {

	private long workFlowId;
	private String status;

	public long getWorkFlowId() {
		return workFlowId;
	}

	public void setWorkFlowId(long workFlowId) {
		this.workFlowId = workFlowId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}

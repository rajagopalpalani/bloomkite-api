package com.sowisetech.admin.request;

public class WorkFlowStatusRequest {
	public int workflowId;
	public String status;
	public int getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(int workflowId) {
		this.workflowId = workflowId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}

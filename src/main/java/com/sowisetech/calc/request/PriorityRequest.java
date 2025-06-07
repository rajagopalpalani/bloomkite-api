package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PriorityRequest {

	private int screenId;
	private String referenceId;
	private List<PriorityReq> priorityReq;
	
	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public List<PriorityReq> getPriorityReq() {
		return priorityReq;
	}

	public void setPriorityReq(List<PriorityReq> priorityReq) {
		this.priorityReq = priorityReq;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

}

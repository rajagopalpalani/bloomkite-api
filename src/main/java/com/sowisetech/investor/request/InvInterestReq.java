package com.sowisetech.investor.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class InvInterestReq {

	private int screenId;
	private List<InvInterestRequest> invInterestReq;
	private String invId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public List<InvInterestRequest> getInvInterestReq() {
		return invInterestReq;
	}

	public void setInvInterestReq(List<InvInterestRequest> invInterestReq) {
		this.invInterestReq = invInterestReq;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

}

package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class NetworthRequest {

	private int screenId;
	private String referenceId;
	private List<NetworthReq> networthReq;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public List<NetworthReq> getNetworthReq() {
		return networthReq;
	}

	public void setNetworthReq(List<NetworthReq> networthReq) {
		this.networthReq = networthReq;
	}

}

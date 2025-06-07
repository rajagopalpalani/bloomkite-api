package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CashFlowRequest {

	private int screenId;
	private String referenceId;
	private String date;
	List<CashFlowItemReq> cashFlowItemReq;

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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<CashFlowItemReq> getCashFlowItemReq() {
		return cashFlowItemReq;
	}

	public void setCashFlowItemReq(List<CashFlowItemReq> cashFlowItemReq) {
		this.cashFlowItemReq = cashFlowItemReq;
	}

}

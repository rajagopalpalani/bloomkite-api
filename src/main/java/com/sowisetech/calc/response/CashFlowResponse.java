package com.sowisetech.calc.response;

import java.util.List;

import com.sowisetech.calc.model.CashFlow;
import com.sowisetech.calc.model.CashFlowSummary;

public class CashFlowResponse {

	List<CashFlow> cashFlowList;
	CashFlowSummary cashFlowSummary;

	public List<CashFlow> getCashFlowList() {
		return cashFlowList;
	}

	public void setCashFlowList(List<CashFlow> cashFlowList) {
		this.cashFlowList = cashFlowList;
	}

	public CashFlowSummary getCashFlowSummary() {
		return cashFlowSummary;
	}

	public void setCashFlowSummary(CashFlowSummary cashFlowSummary) {
		this.cashFlowSummary = cashFlowSummary;
	}

}

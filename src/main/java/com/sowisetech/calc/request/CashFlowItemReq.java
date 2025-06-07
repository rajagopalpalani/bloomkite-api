package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class CashFlowItemReq {

	int cashFlowItemId;
	String budgetAmt;
	String actualAmt;

	public int getCashFlowItemId() {
		return cashFlowItemId;
	}

	public void setCashFlowItemId(int cashFlowItemId) {
		this.cashFlowItemId = cashFlowItemId;
	}

	public String getBudgetAmt() {
		return budgetAmt;
	}

	public void setBudgetAmt(String budgetAmt) {
		this.budgetAmt = budgetAmt;
	}

	public String getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(String actualAmt) {
		this.actualAmt = actualAmt;
	}

}

package com.sowisetech.calc.model;

import java.util.List;

public class FinancialPlanning {

//	List<Goal> goal;
	List<CashFlow> cashFlow;
	CashFlowSummary cashFlowSummary;
	List<Networth> networth;
	NetworthSummary networthSummary;
	List<Priority> priority;
	InsuranceItem insurance;
//	List<RiskProfile> riskProfile;
//	RiskSummary riskSummary;


	public List<CashFlow> getCashFlow() {
		return cashFlow;
	}

	public void setCashFlow(List<CashFlow> cashFlow) {
		this.cashFlow = cashFlow;
	}

	public CashFlowSummary getCashFlowSummary() {
		return cashFlowSummary;
	}

	public void setCashFlowSummary(CashFlowSummary cashFlowSummary) {
		this.cashFlowSummary = cashFlowSummary;
	}

	public List<Networth> getNetworth() {
		return networth;
	}

	public void setNetworth(List<Networth> networth) {
		this.networth = networth;
	}

	public NetworthSummary getNetworthSummary() {
		return networthSummary;
	}

	public void setNetworthSummary(NetworthSummary networthSummary) {
		this.networthSummary = networthSummary;
	}

	public List<Priority> getPriority() {
		return priority;
	}

	public void setPriority(List<Priority> priority) {
		this.priority = priority;
	}

	public InsuranceItem getInsurance() {
		return insurance;
	}

	public void setInsurance(InsuranceItem insurance) {
		this.insurance = insurance;
	}

}

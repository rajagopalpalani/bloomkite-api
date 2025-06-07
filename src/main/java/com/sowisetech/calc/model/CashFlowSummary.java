package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class CashFlowSummary {

	long cashFlowSummaryId;
	String referenceId;
	double monthlyExpense;
	double yearlyExpense;
	// double nonRecurExpense;
	double monthlyIncome;
	double yearlyIncome;
	// double nonRecurIncome;
	double monthlyNetCashFlow;
	double yearlyNetCashFlow;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	// double nonRecurCashflow;
	public long getCashFlowSummaryId() {
		return cashFlowSummaryId;
	}

	public void setCashFlowSummaryId(long cashFlowSummaryId) {
		this.cashFlowSummaryId = cashFlowSummaryId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public double getMonthlyExpense() {
		return monthlyExpense;
	}

	public void setMonthlyExpense(double monthlyExpense) {
		this.monthlyExpense = monthlyExpense;
	}

	public double getYearlyExpense() {
		return yearlyExpense;
	}

	public void setYearlyExpense(double yearlyExpense) {
		this.yearlyExpense = yearlyExpense;
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public double getYearlyIncome() {
		return yearlyIncome;
	}

	public void setYearlyIncome(double yearlyIncome) {
		this.yearlyIncome = yearlyIncome;
	}

	public double getMonthlyNetCashFlow() {
		return monthlyNetCashFlow;
	}

	public void setMonthlyNetCashFlow(double monthlyNetCashFlow) {
		this.monthlyNetCashFlow = monthlyNetCashFlow;
	}

	public double getYearlyNetCashFlow() {
		return yearlyNetCashFlow;
	}

	public void setYearlyNetCashFlow(double yearlyNetCashFlow) {
		this.yearlyNetCashFlow = yearlyNetCashFlow;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

}

package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class CashFlow {

	long cashFlowId;
	String referenceId;
	int cashFlowItemId;
	String cashFlowItem;
	int cashFlowItemTypeId;
	String cashFlowItemType;
	double budgetAmt;
	double actualAmt;
	String date;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	public long getCashFlowId() {
		return cashFlowId;
	}

	public void setCashFlowId(long cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public int getCashFlowItemId() {
		return cashFlowItemId;
	}

	public void setCashFlowItemId(int cashFlowItemId) {
		this.cashFlowItemId = cashFlowItemId;
	}

	public String getCashFlowItem() {
		return cashFlowItem;
	}

	public void setCashFlowItem(String cashFlowItem) {
		this.cashFlowItem = cashFlowItem;
	}

	public int getCashFlowItemTypeId() {
		return cashFlowItemTypeId;
	}

	public void setCashFlowItemTypeId(int cashFlowItemTypeId) {
		this.cashFlowItemTypeId = cashFlowItemTypeId;
	}

	public String getCashFlowItemType() {
		return cashFlowItemType;
	}

	public void setCashFlowItemType(String cashFlowItemType) {
		this.cashFlowItemType = cashFlowItemType;
	}

	public double getBudgetAmt() {
		return budgetAmt;
	}

	public void setBudgetAmt(double budgetAmt) {
		this.budgetAmt = budgetAmt;
	}

	public double getActualAmt() {
		return actualAmt;
	}

	public void setActualAmt(double actualAmt) {
		this.actualAmt = actualAmt;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

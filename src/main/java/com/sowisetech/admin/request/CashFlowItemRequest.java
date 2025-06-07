package com.sowisetech.admin.request;

public class CashFlowItemRequest {
	
	public int cashFlowItemId;
	public String cashFlowItem;
	public int cashFlowItemTypeId;
	
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

}

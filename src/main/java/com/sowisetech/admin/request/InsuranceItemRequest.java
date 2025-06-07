package com.sowisetech.admin.request;

public class InsuranceItemRequest {
	
	public long insuranceItemId;
	public String insuranceItem;
	public String value;
	
	public long getInsuranceItemId() {
		return insuranceItemId;
	}
	public void setInsuranceItemId(long insuranceItemId) {
		this.insuranceItemId = insuranceItemId;
	}
	public String getInsuranceItem() {
		return insuranceItem;
	}
	public void setInsuranceItem(String insuranceItem) {
		this.insuranceItem = insuranceItem;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}

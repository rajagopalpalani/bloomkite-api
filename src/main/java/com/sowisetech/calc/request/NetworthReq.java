package com.sowisetech.calc.request;

public class NetworthReq {

	int accountEntryId;
	String value;
	String futureValue;

	public int getAccountEntryId() {
		return accountEntryId;
	}

	public void setAccountEntryId(int accountEntryId) {
		this.accountEntryId = accountEntryId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(String futureValue) {
		this.futureValue = futureValue;
	}

}

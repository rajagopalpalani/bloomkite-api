package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class Networth {

	long networthId;
	int accountEntryId;
	String accountEntry;
	int accountTypeId;
	String accountType;
	double value;
	double futureValue;
	String referenceId;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	public long getNetworthId() {
		return networthId;
	}

	public void setNetworthId(long networthId) {
		this.networthId = networthId;
	}

	public int getAccountEntryId() {
		return accountEntryId;
	}

	public void setAccountEntryId(int accountEntryId) {
		this.accountEntryId = accountEntryId;
	}

	public String getAccountEntry() {
		return accountEntry;
	}

	public void setAccountEntry(String accountEntry) {
		this.accountEntry = accountEntry;
	}

	public int getAccountTypeId() {
		return accountTypeId;
	}

	public void setAccountTypeId(int accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(double futureValue) {
		this.futureValue = futureValue;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
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

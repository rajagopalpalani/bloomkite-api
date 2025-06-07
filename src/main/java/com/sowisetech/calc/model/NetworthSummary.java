package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class NetworthSummary {

	long networthSummaryId;
	double current_assetValue;
	double current_liability;
	double networth;
	double future_assetValue;
	double future_liability;
	double future_networth;
	String referenceId;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	public long getNetworthSummaryId() {
		return networthSummaryId;
	}

	public void setNetworthSummaryId(long networthSummaryId) {
		this.networthSummaryId = networthSummaryId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public double getCurrent_assetValue() {
		return current_assetValue;
	}

	public void setCurrent_assetValue(double current_assetValue) {
		this.current_assetValue = current_assetValue;
	}

	public double getCurrent_liability() {
		return current_liability;
	}

	public void setCurrent_liability(double current_liability) {
		this.current_liability = current_liability;
	}

	public double getNetworth() {
		return networth;
	}

	public void setNetworth(double networth) {
		this.networth = networth;
	}

	public double getFuture_assetValue() {
		return future_assetValue;
	}

	public void setFuture_assetValue(double future_assetValue) {
		this.future_assetValue = future_assetValue;
	}

	public double getFuture_liability() {
		return future_liability;
	}

	public void setFuture_liability(double future_liability) {
		this.future_liability = future_liability;
	}

	public double getFuture_networth() {
		return future_networth;
	}

	public void setFuture_networth(double future_networth) {
		this.future_networth = future_networth;
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

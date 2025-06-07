package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class FutureValue {

	long futureValueId;
	String referenceId;
	String invType;
	double invAmount;
	int duration;
	String durationType;
	double annualGrowth;
	double totalPayment;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;
	
	public long getFutureValueId() {
		return futureValueId;
	}

	public void setFutureValueId(long futureValueId) {
		this.futureValueId = futureValueId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

	public double getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(double invAmount) {
		this.invAmount = invAmount;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public double getAnnualGrowth() {
		return annualGrowth;
	}

	public void setAnnualGrowth(double annualGrowth) {
		this.annualGrowth = annualGrowth;
	}

	public double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(double totalPayment) {
		this.totalPayment = totalPayment;
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

}

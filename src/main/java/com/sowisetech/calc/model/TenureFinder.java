package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class TenureFinder {

	long tenureFinderId;
	String referenceId;
	String invType;
	double presentValue;
	double futureValue;
	double rateOfInterest;
	double tenure;
	private Timestamp created;
	private Timestamp updated;
	private String created_by;
	private String updated_by;

	public long getTenureFinderId() {
		return tenureFinderId;
	}
	public void setTenureFinderId(long tenureFinderId) {
		this.tenureFinderId = tenureFinderId;
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
	public double getPresentValue() {
		return presentValue;
	}
	public void setPresentValue(double presentValue) {
		this.presentValue = presentValue;
	}
	public double getFutureValue() {
		return futureValue;
	}
	public void setFutureValue(double futureValue) {
		this.futureValue = futureValue;
	}
	public double getRateOfInterest() {
		return rateOfInterest;
	}
	public void setRateOfInterest(double rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}
	public double getTenure() {
		return tenure;
	}
	public void setTenure(double tenure) {
		this.tenure = tenure;
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

package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class EmiInterestChange {

	long emiIntChangeId;
	double loanAmount;
	double interestRate;
	int tenure;
	String tenureType;
	String loanDate;
	double increasedEmi;
	double changedRate;
	String changedDate;
	String referenceId;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;

	public String getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}

	public long getEmiIntChangeId() {
		return emiIntChangeId;
	}

	public void setEmiIntChangeId(long emiIntChangeId) {
		this.emiIntChangeId = emiIntChangeId;
	}

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getIncreasedEmi() {
		return increasedEmi;
	}

	public void setIncreasedEmi(double increasedEmi) {
		this.increasedEmi = increasedEmi;
	}

	public double getChangedRate() {
		return changedRate;
	}

	public void setChangedRate(double changedRate) {
		this.changedRate = changedRate;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
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

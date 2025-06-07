package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class EmiCalculator {

	long emiCalculatorId;
	double loanAmount;
	int tenure;
	String tenureType;
	double interestRate;
	String date;
	String referenceId;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;


	public long getEmiCalculatorId() {
		return emiCalculatorId;
	}

	public void setEmiCalculatorId(long emiCalculatorId) {
		this.emiCalculatorId = emiCalculatorId;
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

	public int getTenure() {
		return tenure;
	}

	public void setTenure(int tenure) {
		this.tenure = tenure;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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

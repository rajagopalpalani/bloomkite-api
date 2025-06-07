package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class EmiCalculatorRequest {

	private int screenId;
	private String loanAmount;
	private String tenure;
	private String tenureType;
	private String interestRate;
	private String referenceId;
	private String date;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

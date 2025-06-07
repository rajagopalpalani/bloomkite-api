package com.sowisetech.calc.response;

import java.util.List;

public class EmiCalculatorResponse {

	double loanAmount;
	double tenure;
	double emi;
	double interestPayable;
	double total;
	List<AmortisationResponse> amortisationResponse;
	double interestRate;
	private String loanDate;

	public double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public double getTenure() {
		return tenure;
	}

	public void setTenure(double tenure) {
		this.tenure = tenure;
	}

	public double getEmi() {
		return emi;
	}

	public void setEmi(double emi) {
		this.emi = emi;
	}

	public double getInterestPayable() {
		return interestPayable;
	}

	public void setInterestPayable(double interestPayable) {
		this.interestPayable = interestPayable;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<AmortisationResponse> getAmortisationResponse() {
		return amortisationResponse;
	}

	public void setAmortisationResponse(List<AmortisationResponse> amortisationResponse) {
		this.amortisationResponse = amortisationResponse;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

}

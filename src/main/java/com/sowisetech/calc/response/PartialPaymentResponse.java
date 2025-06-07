package com.sowisetech.calc.response;

import java.util.List;

public class PartialPaymentResponse {

	double loanAmount;
	double tenure;
	double emi;
	double interestPayable;
	double total;
	double revisedTenure;
	List<AmortisationResponse> amortisation;

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

	public List<AmortisationResponse> getAmortisation() {
		return amortisation;
	}

	public void setAmortisation(List<AmortisationResponse> amortisation) {
		this.amortisation = amortisation;
	}

	public double getRevisedTenure() {
		return revisedTenure;
	}

	public void setRevisedTenure(double revisedTenure) {
		this.revisedTenure = revisedTenure;
	}

}

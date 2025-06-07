package com.sowisetech.calc.response;

public class AmortisationResponse {

	int months;
	double opening;
	double interest;
	double totalPrincipal;
	double closing;
	double loanPaid;
	String date;

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public double getOpening() {
		return opening;
	}

	public void setOpening(double opening) {
		this.opening = opening;
	}

	public double getInterest() {
		return interest;
	}

	public void setInterest(double interest) {
		this.interest = interest;
	}

	public double getTotalPrincipal() {
		return totalPrincipal;
	}

	public void setTotalPrincipal(double totalPrincipal) {
		this.totalPrincipal = totalPrincipal;
	}

	public double getClosing() {
		return closing;
	}

	public void setClosing(double closing) {
		this.closing = closing;
	}

	public double getLoanPaid() {
		return loanPaid;
	}

	public void setLoanPaid(double loanPaid) {
		this.loanPaid = loanPaid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

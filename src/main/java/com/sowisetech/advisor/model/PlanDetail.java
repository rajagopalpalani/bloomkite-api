package com.sowisetech.advisor.model;

public class PlanDetail {

	private String referenceId;
	private boolean goal;
	private boolean finance;
	private boolean loan;
	private boolean riskProfile;
	private boolean investment;
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public boolean isGoal() {
		return goal;
	}
	public void setGoal(boolean goal) {
		this.goal = goal;
	}
	public boolean isFinance() {
		return finance;
	}
	public void setFinance(boolean finance) {
		this.finance = finance;
	}
	public boolean isLoan() {
		return loan;
	}
	public void setLoan(boolean loan) {
		this.loan = loan;
	}
	public boolean isRiskProfile() {
		return riskProfile;
	}
	public void setRiskProfile(boolean riskProfile) {
		this.riskProfile = riskProfile;
	}
	public boolean isInvestment() {
		return investment;
	}
	public void setInvestment(boolean investment) {
		this.investment = investment;
	}
	
}

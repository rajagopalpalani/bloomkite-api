package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class GoalRequest {

	private int screenId;
	private String goalName;
	private String tenure;
	private String tenureType;
	private String goalAmount;
	private String inflationRate;
	private String currentAmount;
	private String growthRate;
	private String annualInvestmentRate;
	private String referenceId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

	public String getGoalAmount() {
		return goalAmount;
	}

	public void setGoalAmount(String goalAmount) {
		this.goalAmount = goalAmount;
	}

	public String getInflationRate() {
		return inflationRate;
	}

	public void setInflationRate(String inflationRate) {
		this.inflationRate = inflationRate;
	}

	public String getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(String currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getGrowthRate() {
		return growthRate;
	}

	public void setGrowthRate(String growthRate) {
		this.growthRate = growthRate;
	}

	public String getAnnualInvestmentRate() {
		return annualInvestmentRate;
	}

	public void setAnnualInvestmentRate(String annualInvestmentRate) {
		this.annualInvestmentRate = annualInvestmentRate;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

}

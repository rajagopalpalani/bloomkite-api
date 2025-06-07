package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class FutureValueAnnuityDueRequest {

	String invAmount;
	String duration;
	String annualGrowth;
	String yearlyIncrease;

	public String getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(String invAmount) {
		this.invAmount = invAmount;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getAnnualGrowth() {
		return annualGrowth;
	}

	public void setAnnualGrowth(String annualGrowth) {
		this.annualGrowth = annualGrowth;
	}

	public String getYearlyIncrease() {
		return yearlyIncrease;
	}

	public void setYearlyIncrease(String yearlyIncrease) {
		this.yearlyIncrease = yearlyIncrease;
	}

}

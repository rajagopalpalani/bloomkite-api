package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class PresentValueAnnuityDueRequest {

	String periodicAmount;
	String annualGrowthRate;
	String duration;

	public String getPeriodicAmount() {
		return periodicAmount;
	}

	public void setPeriodicAmount(String periodicAmount) {
		this.periodicAmount = periodicAmount;
	}

	public String getAnnualGrowthRate() {
		return annualGrowthRate;
	}

	public void setAnnualGrowthRate(String annualGrowthRate) {
		this.annualGrowthRate = annualGrowthRate;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}

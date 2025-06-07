package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class FutureValueRequest {

	private int screenId;
	private String invType;
	private String invAmount;
	private String duration;
	private String durationType;
	private String annualGrowth;
	private String referenceId;
	// private String yearlyIncrease;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

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

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String getAnnualGrowth() {
		return annualGrowth;
	}

	public void setAnnualGrowth(String annualGrowth) {
		this.annualGrowth = annualGrowth;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	// public String getYearlyIncrease() {
	// return yearlyIncrease;
	// }
	//
	// public void setYearlyIncrease(String yearlyIncrease) {
	// this.yearlyIncrease = yearlyIncrease;
	// }

}

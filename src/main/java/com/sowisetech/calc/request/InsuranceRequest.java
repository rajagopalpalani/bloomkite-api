package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class InsuranceRequest {

	private int screenId;
	String referenceId;
	String annualIncome;
	String stability;
	String predictability;
	String existingInsurance;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(String annualIncome) {
		this.annualIncome = annualIncome;
	}

	public String getStability() {
		return stability;
	}

	public void setStability(String stability) {
		this.stability = stability;
	}

	public String getPredictability() {
		return predictability;
	}

	public void setPredictability(String predictability) {
		this.predictability = predictability;
	}

	public String getExistingInsurance() {
		return existingInsurance;
	}

	public void setExistingInsurance(String existingInsurance) {
		this.existingInsurance = existingInsurance;
	}

}

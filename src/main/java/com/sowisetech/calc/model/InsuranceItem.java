package com.sowisetech.calc.model;

public class InsuranceItem {

	long insuranceId;
	String referenceId;
	InsuranceAmountItem annualIncome;
	InsuranceStringItem stability;
	InsuranceStringItem predictability;
	InsuranceAmountItem requiredInsurance;
	InsuranceAmountItem existingInsurance;
	InsuranceAmountItem additionalInsurance;

	public long getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(long insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public InsuranceAmountItem getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(InsuranceAmountItem annualIncome) {
		this.annualIncome = annualIncome;
	}

	public InsuranceStringItem getStability() {
		return stability;
	}

	public void setStability(InsuranceStringItem stability) {
		this.stability = stability;
	}

	public InsuranceStringItem getPredictability() {
		return predictability;
	}

	public void setPredictability(InsuranceStringItem predictability) {
		this.predictability = predictability;
	}

	public InsuranceAmountItem getRequiredInsurance() {
		return requiredInsurance;
	}

	public void setRequiredInsurance(InsuranceAmountItem requiredInsurance) {
		this.requiredInsurance = requiredInsurance;
	}

	public InsuranceAmountItem getExistingInsurance() {
		return existingInsurance;
	}

	public void setExistingInsurance(InsuranceAmountItem existingInsurance) {
		this.existingInsurance = existingInsurance;
	}

	public InsuranceAmountItem getAdditionalInsurance() {
		return additionalInsurance;
	}

	public void setAdditionalInsurance(InsuranceAmountItem additionalInsurance) {
		this.additionalInsurance = additionalInsurance;
	}
}

package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class Insurance {

	long insuranceId;
	String referenceId;
	double annualIncome;
	String stability;
	String predictability;
	double existingInsurance;
	double requiredInsurance;
	double additionalInsurance;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

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

	public double getAnnualIncome() {
		return annualIncome;
	}

	public void setAnnualIncome(double annualIncome) {
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

	public double getExistingInsurance() {
		return existingInsurance;
	}

	public void setExistingInsurance(double existingInsurance) {
		this.existingInsurance = existingInsurance;
	}

	public double getRequiredInsurance() {
		return requiredInsurance;
	}

	public void setRequiredInsurance(double requiredInsurance) {
		this.requiredInsurance = requiredInsurance;
	}

	public double getAdditionalInsurance() {
		return additionalInsurance;
	}

	public void setAdditionalInsurance(double additionalInsurance) {
		this.additionalInsurance = additionalInsurance;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

}

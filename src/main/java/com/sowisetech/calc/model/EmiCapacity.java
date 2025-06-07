package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class EmiCapacity {

	long emiCapacityId;
	int currentAge;
	int retirementAge;
	String stability;
	String backUp;
	double netFamilyIncome;
	double existingEmi;
	double houseHoldExpense;
	double additionalIncome;
	double interestRate;
	String referenceId;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;


	public long getEmiCapacityId() {
		return emiCapacityId;
	}

	public void setEmiCapacityId(long emiCapacityId) {
		this.emiCapacityId = emiCapacityId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public int getCurrentAge() {
		return currentAge;
	}

	public void setCurrentAge(int currentAge) {
		this.currentAge = currentAge;
	}

	public int getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(int retirementAge) {
		this.retirementAge = retirementAge;
	}

	public String getStability() {
		return stability;
	}

	public void setStability(String stability) {
		this.stability = stability;
	}

	public String getBackUp() {
		return backUp;
	}

	public void setBackUp(String backUp) {
		this.backUp = backUp;
	}

	public double getNetFamilyIncome() {
		return netFamilyIncome;
	}

	public void setNetFamilyIncome(double netFamilyIncome) {
		this.netFamilyIncome = netFamilyIncome;
	}

	public double getExistingEmi() {
		return existingEmi;
	}

	public void setExistingEmi(double existingEmi) {
		this.existingEmi = existingEmi;
	}

	public double getHouseHoldExpense() {
		return houseHoldExpense;
	}

	public void setHouseHoldExpense(double houseHoldExpense) {
		this.houseHoldExpense = houseHoldExpense;
	}

	public double getAdditionalIncome() {
		return additionalIncome;
	}

	public void setAdditionalIncome(double additionalIncome) {
		this.additionalIncome = additionalIncome;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
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

}

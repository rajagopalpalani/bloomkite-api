package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class EmiCapacityRequest {

	private int screenId;
	private String currentAge;
	private String retirementAge;
	private String stability;
	private String backUp;
	private String netFamilyIncome;
	private String existingEmi;
	private String houseHoldExpense;
	private String additionalIncome;
	private String interestRate;
	private String referenceId;
	// String priciple;
	// String interest;

	public String getCurrentAge() {
		return currentAge;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public void setCurrentAge(String currentAge) {
		this.currentAge = currentAge;
	}

	public String getRetirementAge() {
		return retirementAge;
	}

	public void setRetirementAge(String retirementAge) {
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

	public String getNetFamilyIncome() {
		return netFamilyIncome;
	}

	public void setNetFamilyIncome(String netFamilyIncome) {
		this.netFamilyIncome = netFamilyIncome;
	}

	public String getExistingEmi() {
		return existingEmi;
	}

	public void setExistingEmi(String existingEmi) {
		this.existingEmi = existingEmi;
	}

	public String getHouseHoldExpense() {
		return houseHoldExpense;
	}

	public void setHouseHoldExpense(String houseHoldExpense) {
		this.houseHoldExpense = houseHoldExpense;
	}

	public String getAdditionalIncome() {
		return additionalIncome;
	}

	public void setAdditionalIncome(String additionalIncome) {
		this.additionalIncome = additionalIncome;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	// public String getPriciple() {
	// return priciple;
	// }
	//
	// public void setPriciple(String priciple) {
	// this.priciple = priciple;
	// }
	//
	// public String getInterest() {
	// return interest;
	// }
	//
	// public void setInterest(String interest) {
	// this.interest = interest;
	// }

}

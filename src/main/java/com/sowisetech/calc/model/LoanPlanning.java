package com.sowisetech.calc.model;

import java.util.List;

public class LoanPlanning {

	EmiCalculator emiCalculator;
	EmiCapacity emiCapacity;
	List<PartialPayment> partialPayment;
	List<EmiChange> emiChange;
	List<InterestChange> interestChange;
//	List<EmiInterestChange> emiInterestChange;

	public EmiCalculator getEmiCalculator() {
		return emiCalculator;
	}

	public void setEmiCalculator(EmiCalculator emiCalculator) {
		this.emiCalculator = emiCalculator;
	}

	public EmiCapacity getEmiCapacity() {
		return emiCapacity;
	}

	public void setEmiCapacity(EmiCapacity emiCapacity) {
		this.emiCapacity = emiCapacity;
	}

	public List<PartialPayment> getPartialPayment() {
		return partialPayment;
	}

	public void setPartialPayment(List<PartialPayment> partialPayment) {
		this.partialPayment = partialPayment;
	}

	public List<EmiChange> getEmiChange() {
		return emiChange;
	}

	public void setEmiChange(List<EmiChange> emiChange) {
		this.emiChange = emiChange;
	}

	public List<InterestChange> getInterestChange() {
		return interestChange;
	}

	public void setInterestChange(List<InterestChange> interestChange) {
		this.interestChange = interestChange;
	}
//
//	public List<EmiInterestChange> getEmiInterestChange() {
//		return emiInterestChange;
//	}
//
//	public void setEmiInterestChange(List<EmiInterestChange> emiInterestChange) {
//		this.emiInterestChange = emiInterestChange;
//	}

}

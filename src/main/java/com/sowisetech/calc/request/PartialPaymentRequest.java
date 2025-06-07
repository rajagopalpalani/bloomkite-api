package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class PartialPaymentRequest {

	private int screenId;
	private String referenceId;
	private String loanAmount;
	private String interestRate;
	private String tenure;
	private String tenureType;
	private String loanDate;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	List<PartialPaymentReq> partialPaymentReq;

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	public List<PartialPaymentReq> getPartialPaymentReq() {
		return partialPaymentReq;
	}

	public void setPartialPaymentReq(List<PartialPaymentReq> partialPaymentReq) {
		this.partialPaymentReq = partialPaymentReq;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

}

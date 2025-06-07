package com.sowisetech.calc.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class EmiInterestChangeRequest {
	private int screenId;
	private String referenceId;
	private String loanAmount;
	private String tenure;
	private String tenureType;
	private String interestRate;
	private String loanDate;
	private List<EmiInterestChangeReq> emiInterestChangeReq;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

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

	public String getTenure() {
		return tenure;
	}

	public void setTenure(String tenure) {
		this.tenure = tenure;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public List<EmiInterestChangeReq> getEmiInterestChangeReq() {
		return emiInterestChangeReq;
	}

	public void setEmiInterestChangeReq(List<EmiInterestChangeReq> emiInterestChangeReq) {
		this.emiInterestChangeReq = emiInterestChangeReq;
	}

	public String getTenureType() {
		return tenureType;
	}

	public void setTenureType(String tenureType) {
		this.tenureType = tenureType;
	}

}

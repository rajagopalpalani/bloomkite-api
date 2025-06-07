package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class InterestChangeReq {

	String changedRate;
	String interestChangedDate;

	public String getChangedRate() {
		return changedRate;
	}

	public void setChangedRate(String changedRate) {
		this.changedRate = changedRate;
	}

	public String getInterestChangedDate() {
		return interestChangedDate;
	}

	public void setInterestChangedDate(String interestChangedDate) {
		this.interestChangedDate = interestChangedDate;
	}

}

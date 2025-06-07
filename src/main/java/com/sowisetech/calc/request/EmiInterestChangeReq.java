package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class EmiInterestChangeReq {
	String increasedEmi;
	String changedDate;
	String changedRate;

	public String getChangedDate() {
		return changedDate;
	}

	public void setChangedDate(String changedDate) {
		this.changedDate = changedDate;
	}

	public String getIncreasedEmi() {
		return increasedEmi;
	}

	public void setIncreasedEmi(String increasedEmi) {
		this.increasedEmi = increasedEmi;
	}

	public String getChangedRate() {
		return changedRate;
	}

	public void setChangedRate(String changedRate) {
		this.changedRate = changedRate;
	}

}

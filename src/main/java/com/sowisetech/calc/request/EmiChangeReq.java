package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class EmiChangeReq {

	String increasedEmi;
	String emiChangedDate;

	public String getIncreasedEmi() {
		return increasedEmi;
	}

	public void setIncreasedEmi(String increasedEmi) {
		this.increasedEmi = increasedEmi;
	}

	public String getEmiChangedDate() {
		return emiChangedDate;
	}

	public void setEmiChangedDate(String emiChangedDate) {
		this.emiChangedDate = emiChangedDate;
	}

	
}

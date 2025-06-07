package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class FundRequest {
	private int id;
	private String fundName;
	private String fundAMC;
	private String fundSubcategory;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundAMC() {
		return fundAMC;
	}

	public void setFundAMC(String fundAMC) {
		this.fundAMC = fundAMC;
	}

	public String getFundSubcategory() {
		return fundSubcategory;
	}

	public void setFundSubcategory(String fundSubcategory) {
		this.fundSubcategory = fundSubcategory;
	}

}

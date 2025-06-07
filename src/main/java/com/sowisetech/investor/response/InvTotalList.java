package com.sowisetech.investor.response;

import java.util.List;

import com.sowisetech.investor.model.Investor;

public class InvTotalList {

	long totalRecords;
	List<Investor> investors;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<Investor> getInvestors() {
		return investors;
	}

	public void setInvestors(List<Investor> investors) {
		this.investors = investors;
	}

}

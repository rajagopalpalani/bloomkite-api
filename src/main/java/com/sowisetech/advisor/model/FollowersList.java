package com.sowisetech.advisor.model;

import java.util.List;

import com.sowisetech.investor.model.Investor;

public class FollowersList {

	List<Advisor> advisors;
	List<Investor> investors;

	public List<Advisor> getAdvisors() {
		return advisors;
	}

	public void setAdvisors(List<Advisor> advisors) {
		this.advisors = advisors;
	}

	public List<Investor> getInvestors() {
		return investors;
	}

	public void setInvestors(List<Investor> investors) {
		this.investors = investors;
	}

}

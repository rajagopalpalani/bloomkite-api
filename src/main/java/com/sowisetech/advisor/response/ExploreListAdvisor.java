package com.sowisetech.advisor.response;

import java.util.List;

import com.sowisetech.advisor.model.ExploreAdvisor;

public class ExploreListAdvisor {

	long totalRecords;
	List<ExploreAdvisor> advisors;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ExploreAdvisor> getAdvisors() {
		return advisors;
	}

	public void setAdvisors(List<ExploreAdvisor> advisors) {
		this.advisors = advisors;
	}

}

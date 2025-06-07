package com.sowisetech.advisor.response;

import java.util.List;

import com.sowisetech.advisor.model.Advisor;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.ServicePlan;

public class AdvTotalList {

	long totalRecords;
	List<Advisor> advisors;
	List<ServicePlan> servicePlanList;

	public List<Advisor> getAdvisors() {
		return advisors;
	}

	public void setAdvisors(List<Advisor> advisors) {
		this.advisors = advisors;
	}

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ServicePlan> getServicePlanList() {
		return servicePlanList;
	}

	public void setServicePlanList(List<ServicePlan> servicePlanList) {
		this.servicePlanList = servicePlanList;
	}

}

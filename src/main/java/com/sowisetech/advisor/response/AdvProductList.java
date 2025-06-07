package com.sowisetech.advisor.response;

import java.util.List;

import com.sowisetech.advisor.model.ServicePlan;

public class AdvProductList {

	long totalRecords;
	List<ServicePlan> servicePlanList;

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

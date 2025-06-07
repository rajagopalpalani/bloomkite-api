package com.sowisetech.advisor.model;

import java.util.List;

public class Service {
	private long serviceId;
	private String service;
	private long prodId;
	List<ServicePlan> servicePlans;

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public List<ServicePlan> getServicePlans() {
		return servicePlans;
	}

	public void setServicePlans(List<ServicePlan> servicePlans) {
		this.servicePlans = servicePlans;
	}

}

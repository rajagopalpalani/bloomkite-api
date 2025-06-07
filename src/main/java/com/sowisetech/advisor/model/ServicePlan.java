package com.sowisetech.advisor.model;

public class ServicePlan {

	private long servicePlanId;
	private long prodId;
	private long serviceId;
	private long brandId;
	private String servicePlan;
	private String servicePlanLink;

	public long getServicePlanId() {
		return servicePlanId;
	}

	public void setServicePlanId(long servicePlanId) {
		this.servicePlanId = servicePlanId;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getServicePlan() {
		return servicePlan;
	}

	public void setServicePlan(String servicePlan) {
		this.servicePlan = servicePlan;
	}

	public String getServicePlanLink() {
		return servicePlanLink;
	}

	public void setServicePlanLink(String servicePlanLink) {
		this.servicePlanLink = servicePlanLink;
	}

}

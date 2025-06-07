package com.sowisetech.advisor.model;

import java.util.List;

public class Product {

	private long prodId;
	private String product;
	List<Brand> brands;
	List<Service> services;
	List<ServicePlan> servicePlans;

	public List<Brand> getBrands() {
		return brands;
	}

	public void setBrands(List<Brand> brands) {
		this.brands = brands;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public List<ServicePlan> getServicePlans() {
		return servicePlans;
	}

	public void setServicePlans(List<ServicePlan> servicePlans) {
		this.servicePlans = servicePlans;
	}

}

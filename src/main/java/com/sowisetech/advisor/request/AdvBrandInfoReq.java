package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class AdvBrandInfoReq {

	long prodId;
	String serviceId;
	long brandId1;
	long brandId2;
	long brandId3;

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public long getBrandId1() {
		return brandId1;
	}

	public void setBrandId1(long brandId1) {
		this.brandId1 = brandId1;
	}

	public long getBrandId2() {
		return brandId2;
	}

	public void setBrandId2(long brandId2) {
		this.brandId2 = brandId2;
	}

	public long getBrandId3() {
		return brandId3;
	}

	public void setBrandId3(long brandId3) {
		this.brandId3 = brandId3;
	}

}

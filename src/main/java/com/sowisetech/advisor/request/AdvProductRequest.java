package com.sowisetech.advisor.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AdvProductRequest {

	private long prodId;
	private String serviceId;
	private long remId;
	private long licId;
	private String licNumber;
	private String validity;
	private String licImage;
	private long advProdId;

	// private String issuedBy;

	public long getAdvProdId() {
		return advProdId;
	}

	public void setAdvProdId(long advProdId) {
		this.advProdId = advProdId;
	}

	public String getLicNumber() {
		return licNumber;
	}

	public void setLicNumber(String licNumber) {
		this.licNumber = licNumber;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public long getRemId() {
		return remId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setRemId(long remId) {
		this.remId = remId;
	}

	public long getLicId() {
		return licId;
	}

	public void setLicId(long licId) {
		this.licId = licId;
	}

	public String getLicImage() {
		return licImage;
	}

	public void setLicImage(String licImage) {
		this.licImage = licImage;
	}
}

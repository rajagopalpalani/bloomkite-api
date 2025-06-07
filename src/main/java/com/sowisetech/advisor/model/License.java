package com.sowisetech.advisor.model;

public class License {
	
	private long licId;
	private String license;
	private String issuedBy;
	private long prodId;
	public long getLicId() {
		return licId;
	}
	public void setLicId(long licId) {
		this.licId = licId;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public long getProdId() {
		return prodId;
	}
	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

}

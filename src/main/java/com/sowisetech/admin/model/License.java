package com.sowisetech.admin.model;

public class License {
	private int licId;
	private String license;
	private String issuedBy;
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	private int prodId;
	public int getProdId() {
		return prodId;
	}
	public void setProdId(int prodId) {
		this.prodId = prodId;
	}
	public int getLicId() {
		return licId;
	}
	public void setLicId(int licId) {
		this.licId = licId;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	
	
}

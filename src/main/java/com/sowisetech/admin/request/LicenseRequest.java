package com.sowisetech.admin.request;

public class LicenseRequest {
	public int licId;
	public String license;
	public String issuedBy;
	public String getIssuedBy() {
		return issuedBy;
	}
	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}
	public int prodId;
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

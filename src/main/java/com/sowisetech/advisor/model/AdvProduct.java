package com.sowisetech.advisor.model;

import java.sql.Timestamp;

public class AdvProduct {
	private String advId;
	private long advProdId;
	private long prodId;
	private String serviceId;
	private long remId;
	private long licId;
	private String licNumber;
	private String validity;
	private String delete_flag;
	private String licImage;
	private String created_by;
	private String updated_by;
	private Timestamp created;
	private Timestamp updated;

	// private String issuedBy;
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

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public long getAdvProdId() {
		return advProdId;
	}

	public void setAdvProdId(long advProdId) {
		this.advProdId = advProdId;
	}

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

	public long getRemId() {
		return remId;
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

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public String getLicImage() {
		return licImage;
	}

	public void setLicImage(String licImage) {
		this.licImage = licImage;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

}

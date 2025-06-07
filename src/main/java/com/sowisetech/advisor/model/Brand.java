package com.sowisetech.advisor.model;

public class Brand {

	private long brandId;
	private String brand;
	private long prodId;

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getProdId() {
		return prodId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

}

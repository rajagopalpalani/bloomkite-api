package com.sowisetech.advisor.model;

public class Category {

	private long categoryId;
	private String desc;
	private long categoryTypeId;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getCategoryTypeId() {
		return categoryTypeId;
	}

	public void setCategoryTypeId(long categoryTypeId) {
		this.categoryTypeId = categoryTypeId;
	}

}

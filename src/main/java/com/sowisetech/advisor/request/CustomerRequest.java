package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class CustomerRequest {
	public String productname;
	public int category;
	public int price;
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
}

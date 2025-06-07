package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class OrderRequest {

	int screenId;
	String orderDetailId;
	String roleBasedId;
	String emailId;
	String phoneNumber;
	String name;
	String plan_id;
	String subscription_id;
	String razorpay_order_id;
	String type;
	String razorpay_payment_id;

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getRoleBasedId() {
		return roleBasedId;
	}

	public void setRoleBasedId(String roleBasedId) {
		this.roleBasedId = roleBasedId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(String subscription_id) {
		this.subscription_id = subscription_id;
	}

	public String getRazorpay_order_id() {
		return razorpay_order_id;
	}

	public void setRazorpay_order_id(String razorpay_order_id) {
		this.razorpay_order_id = razorpay_order_id;
	}

	public String getRazorpay_payment_id() {
		return razorpay_payment_id;
	}

	public void setRazorpay_payment_id(String razorpay_payment_id) {
		this.razorpay_payment_id = razorpay_payment_id;
	}

}

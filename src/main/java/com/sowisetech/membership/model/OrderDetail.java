package com.sowisetech.membership.model;

import java.sql.Timestamp;

public class OrderDetail {

	private String orderDetailId;
	private String roleBasedId;
	private String emailId;
	private String phoneNumber;
	private String name;
	private String razorpay_plan_id;
	private String status;
	private Timestamp created;
	private String created_by;
	private String subscription_id;
	private String razorpay_order_id;
	private String type;
	private Timestamp updated;
	private String updated_by;
	private String razorpay_payment_id;

	public String getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(String orderDetailId) {
		this.orderDetailId = orderDetailId;
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

	public String getRazorpay_plan_id() {
		return razorpay_plan_id;
	}

	public void setRazorpay_plan_id(String razorpay_plan_id) {
		this.razorpay_plan_id = razorpay_plan_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getRazorpay_payment_id() {
		return razorpay_payment_id;
	}

	public void setRazorpay_payment_id(String razorpay_payment_id) {
		this.razorpay_payment_id = razorpay_payment_id;
	}

}

package com.sowisetech.membership.model;

import java.sql.Timestamp;
import java.util.Date;

public class SinglePayment {
	private int singlePaymentId;
	private String roleBasedId;
	private String name;
	private String emailId;
	private String phoneNumber;
	private String order_id;
	private String entity;
	private int amount;
	private int amount_paid;
	private int amount_due;
	private String currency;
	private String receipt;
	private String offer_id;
	private String status;
	private int attempts;
	private Date created_at;
	private String secretId;
	private String type;
	private String plan_id;
	private String period;
	private Timestamp subStartedAt;
	private Timestamp subEndAt;
	private int total_count;

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getSinglePaymentId() {
		return singlePaymentId;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public void setSinglePaymentId(int singlePaymentId) {
		this.singlePaymentId = singlePaymentId;
	}

	public String getRoleBasedId() {
		return roleBasedId;
	}

	public void setRoleBasedId(String roleBasedId) {
		this.roleBasedId = roleBasedId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmount_paid() {
		return amount_paid;
	}

	public void setAmount_paid(int amount_paid) {
		this.amount_paid = amount_paid;
	}

	public int getAmount_due() {
		return amount_due;
	}

	public void setAmount_due(int amount_due) {
		this.amount_due = amount_due;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Timestamp getSubStartedAt() {
		return subStartedAt;
	}

	public void setSubStartedAt(Timestamp subStartedAt) {
		this.subStartedAt = subStartedAt;
	}

	public Timestamp getSubEndAt() {
		return subEndAt;
	}

	public void setSubEndAt(Timestamp subEndAt) {
		this.subEndAt = subEndAt;
	}

	}



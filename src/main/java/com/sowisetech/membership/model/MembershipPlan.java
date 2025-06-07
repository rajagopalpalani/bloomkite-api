package com.sowisetech.membership.model;

import java.sql.Timestamp;

public class MembershipPlan {

	private int membershipPlanId;
	private String razorpayPlanId;
	private String period;
	private int interval;
	private String itemId;
	private boolean active;
	private String name;
	private String description;
	private int amount;
	private String currency;
	private String planName;
	private String content;
	private int created_at;
	private int updated_at;
	private String delete_flag;

	public int getMembershipPlanId() {
		return membershipPlanId;
	}

	public void setMembershipPlanId(int membershipPlanId) {
		this.membershipPlanId = membershipPlanId;
	}

	public String getRazorpayPlanId() {
		return razorpayPlanId;
	}

	public void setRazorpayPlanId(String razorpayPlanId) {
		this.razorpayPlanId = razorpayPlanId;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	public int getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(int updated_at) {
		this.updated_at = updated_at;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

}

package com.sowisetech.membership.model;

import java.util.Date;

public class MemberSubscription {
	private int sub_id;
	private String advId;
	private String razorpaySubId;
	private String razorpayPlanId;
	private String customer_id;
	private String status;
	private int current_start;
	private int current_end;
	private long ended_at;
	private int quantity;
	private int charge_at;
	private int start_at;
	private long end_at;
	private int auth_attempts;
	private int total_count;
	private int paid_count;
	private boolean customer_notify;
	private Date created_at;
	// private int paused_at;
	// private String pause_initiated_by;
	// private String cancel_initiated_by;
	private int expire_by;
	private String short_url;
	private boolean has_scheduled_changes;
	private String change_scheduled_at;
	private String source;
	private int remaining_count;
	private String payment_method;
	private String secretId;
	private String type;
	private SinglePayment singlePayment;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSub_id() {
		return sub_id;
	}

	public void setSub_id(int sub_id) {
		this.sub_id = sub_id;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public String getRazorpaySubId() {
		return razorpaySubId;
	}

	public void setRazorpaySubId(String razorpaySubId) {
		this.razorpaySubId = razorpaySubId;
	}

	public String getRazorpayPlanId() {
		return razorpayPlanId;
	}

	public void setRazorpayPlanId(String razorpayPlanId) {
		this.razorpayPlanId = razorpayPlanId;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCurrent_start() {
		return current_start;
	}

	public void setCurrent_start(int current_start) {
		this.current_start = current_start;
	}

	public int getCurrent_end() {
		return current_end;
	}

	public void setCurrent_end(int current_end) {
		this.current_end = current_end;
	}

	public long getEnded_at() {
		return ended_at;
	}

	public void setEnded_at(long ended_at) {
		this.ended_at = ended_at;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCharge_at() {
		return charge_at;
	}

	public void setCharge_at(int charge_at) {
		this.charge_at = charge_at;
	}

	public int getStart_at() {
		return start_at;
	}

	public void setStart_at(int start_at) {
		this.start_at = start_at;
	}

	public long getEnd_at() {
		return end_at;
	}

	public void setEnd_at(long end_at) {
		this.end_at = end_at;
	}

	public int getAuth_attempts() {
		return auth_attempts;
	}

	public void setAuth_attempts(int auth_attempts) {
		this.auth_attempts = auth_attempts;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getPaid_count() {
		return paid_count;
	}

	public void setPaid_count(int paid_count) {
		this.paid_count = paid_count;
	}

	public boolean isCustomer_notify() {
		return customer_notify;
	}

	public void setCustomer_notify(boolean customer_notify) {
		this.customer_notify = customer_notify;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	// public int getPaused_at() {
	// return paused_at;
	// }
	//
	// public void setPaused_at(int paused_at) {
	// this.paused_at = paused_at;
	// }
	//
	// public String getPause_initiated_by() {
	// return pause_initiated_by;
	// }
	//
	// public void setPause_initiated_by(String pause_initiated_by) {
	// this.pause_initiated_by = pause_initiated_by;
	// }
	//
	// public String getCancel_initiated_by() {
	// return cancel_initiated_by;
	// }
	//
	// public void setCancel_initiated_by(String cancel_initiated_by) {
	// this.cancel_initiated_by = cancel_initiated_by;
	// }

	public int getExpire_by() {
		return expire_by;
	}

	public void setExpire_by(int expire_by) {
		this.expire_by = expire_by;
	}

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	public boolean isHas_scheduled_changes() {
		return has_scheduled_changes;
	}

	public void setHas_scheduled_changes(boolean has_scheduled_changes) {
		this.has_scheduled_changes = has_scheduled_changes;
	}

	public String getChange_scheduled_at() {
		return change_scheduled_at;
	}

	public void setChange_scheduled_at(String change_scheduled_at) {
		this.change_scheduled_at = change_scheduled_at;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public int getRemaining_count() {
		return remaining_count;
	}

	public void setRemaining_count(int remaining_count) {
		this.remaining_count = remaining_count;
	}

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public SinglePayment getSinglePayment() {
		return singlePayment;
	}

	public void setSinglePayment(SinglePayment singlePayment) {
		this.singlePayment = singlePayment;
	}		
	
}

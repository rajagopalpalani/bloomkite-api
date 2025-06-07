package com.sowisetech.membership.request;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionEntityRequest {

	String id;
	String entity;
	String plan_id;
	String customer_id;
	String status;
	int type;
	int current_start;
	int current_end;
	long ended_at;
	int quantity;
	int charge_at;
	int start_at;
	long end_at;
	int auth_attempts;
	int total_count;
	int paid_count;
	boolean customer_notify;
	Date created_at;
	int expire_by;
	String short_url;
	boolean has_scheduled_changes;
	String change_scheduled_at;
	String offer_id;
	int remaining_count;
	String source;
	String payment_method;
	String pause_initiated_by;
	String cancel_initiated_by;

	public String getPayment_method() {
		return payment_method;
	}

	public void setPayment_method(String payment_method) {
		this.payment_method = payment_method;
	}

	public String getPause_initiated_by() {
		return pause_initiated_by;
	}

	public void setPause_initiated_by(String pause_initiated_by) {
		this.pause_initiated_by = pause_initiated_by;
	}

	public String getCancel_initiated_by() {
		return cancel_initiated_by;
	}

	public void setCancel_initiated_by(String cancel_initiated_by) {
		this.cancel_initiated_by = cancel_initiated_by;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public String getOffer_id() {
		return offer_id;
	}

	public void setOffer_id(String offer_id) {
		this.offer_id = offer_id;
	}

	public int getRemaining_count() {
		return remaining_count;
	}

	public void setRemaining_count(int remaining_count) {
		this.remaining_count = remaining_count;
	}

}

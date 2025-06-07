package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionChargedRequest {
	String entity;
	String account_id;
	String event;
	String contains[];
	PayloadRequest payload;
	int created_at;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String[] getContains() {
		return contains;
	}

	public void setContains(String[] contains) {
		this.contains = contains;
	}

	public PayloadRequest getPayload() {
		return payload;
	}

	public void setPayload(PayloadRequest payload) {
		this.payload = payload;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

}

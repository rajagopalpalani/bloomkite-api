package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionModeRequest {
	
	SubscriptionEntityRequest entity;

	public SubscriptionEntityRequest getEntity() {
		return entity;
	}

	public void setEntity(SubscriptionEntityRequest entity) {
		this.entity = entity;
	}

}

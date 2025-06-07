package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class PaymentRequest {
	
	PaymentEntityRequest entity;

	public PaymentEntityRequest getEntity() {
		return entity;
	}

	public void setEntity(PaymentEntityRequest entity) {
		this.entity = entity;
	}

}

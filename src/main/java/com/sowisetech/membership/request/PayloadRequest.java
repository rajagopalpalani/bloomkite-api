package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class PayloadRequest {
	
	SubscriptionModeRequest subscription;
	PaymentRequest payment;
	
	public SubscriptionModeRequest getSubscription() {
		return subscription;
	}
	public void setSubscription(SubscriptionModeRequest subscription) {
		this.subscription = subscription;
	}
	public PaymentRequest getPayment() {
		return payment;
	}
	public void setPayment(PaymentRequest payment) {
		this.payment = payment;
	}
	

}

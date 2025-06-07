package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class VerifyPaymentRequest {

	private int screenId;
	private int singlePaymentId;
	private String razorpay_payment_id;
	private String razorpay_order_id;
	private String signature;
	private String subscription_id;
	private String type;

	public String getSubscription_id() {
		return subscription_id;
	}

	public void setSubscription_id(String subscription_id) {
		this.subscription_id = subscription_id;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public int getSinglePaymentId() {
		return singlePaymentId;
	}

	public void setSinglePaymentId(int singlePaymentId) {
		this.singlePaymentId = singlePaymentId;
	}

	public String getRazorpay_payment_id() {
		return razorpay_payment_id;
	}

	public void setRazorpay_payment_id(String razorpay_payment_id) {
		this.razorpay_payment_id = razorpay_payment_id;
	}

	public String getRazorpay_order_id() {
		return razorpay_order_id;
	}

	public void setRazorpay_order_id(String razorpay_order_id) {
		this.razorpay_order_id = razorpay_order_id;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

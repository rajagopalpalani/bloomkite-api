package com.sowisetech.membership.model;

import java.util.Date;

public class SubscriptionPayment {

	String razorpaySubId;
	String razorpayPaymentId;
	int amount;
	String currency;
	String status;
	String order_id;
	String invoice_id;
	boolean international;
	String method;
	int amount_refunded;
	int amount_transferred;
	String refund_status;
	boolean singlepay_captured;
	int captured;
	String description;
	String card_id;
	String bank;
	String wallet;
	String vpa;
	String emailId;
	String contact;
	String customer_id;
	String token_id;
	int fee;
	int tax;
	String error_code;
	String error_description;
	String error_source;
	String error_step;
	String error_reason;
	int created_at;
	Date pay_created_at;

	public String getRazorpaySubId() {
		return razorpaySubId;
	}

	public void setRazorpaySubId(String razorpaySubId) {
		this.razorpaySubId = razorpaySubId;
	}

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getInvoice_id() {
		return invoice_id;
	}

	public void setInvoice_id(String invoice_id) {
		this.invoice_id = invoice_id;
	}

	public boolean isInternational() {
		return international;
	}

	public void setInternational(boolean international) {
		this.international = international;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public int getAmount_refunded() {
		return amount_refunded;
	}

	public void setAmount_refunded(int amount_refunded) {
		this.amount_refunded = amount_refunded;
	}

	public int getAmount_transferred() {
		return amount_transferred;
	}

	public void setAmount_transferred(int amount_transferred) {
		this.amount_transferred = amount_transferred;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public boolean isSinglepay_captured() {
		return singlepay_captured;
	}

	public void setSinglepay_captured(boolean singlepay_captured) {
		this.singlepay_captured = singlepay_captured;
	}

	public int getCaptured() {
		return captured;
	}

	public void setCaptured(int captured) {
		this.captured = captured;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCard_id() {
		return card_id;
	}

	public void setCard_id(String card_id) {
		this.card_id = card_id;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}

	public String getVpa() {
		return vpa;
	}

	public void setVpa(String vpa) {
		this.vpa = vpa;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getToken_id() {
		return token_id;
	}

	public void setToken_id(String token_id) {
		this.token_id = token_id;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getTax() {
		return tax;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public String getError_description() {
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

	public String getError_source() {
		return error_source;
	}

	public void setError_source(String error_source) {
		this.error_source = error_source;
	}

	public String getError_step() {
		return error_step;
	}

	public void setError_step(String error_step) {
		this.error_step = error_step;
	}

	public String getError_reason() {
		return error_reason;
	}

	public void setError_reason(String error_reason) {
		this.error_reason = error_reason;
	}

	public Date getPay_created_at() {
		return pay_created_at;
	}

	public void setPay_created_at(Date pay_created_at) {
		this.pay_created_at = pay_created_at;
	}

}

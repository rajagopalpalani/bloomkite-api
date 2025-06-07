package com.sowisetech.membership.model;

import java.util.List;

public class InvoiceSubscription {

	int count;

	String itemId;
	String entity;
	String receipt;
	String invoice_number;
	String customer_id;
	String name;
	String email;
	String contact;
	String billing_address;
	String shipping_address;
	String customer_name;
	String customer_email;
	String customer_contact;
	String order_id;
	String sub_id;
	List<LineItems> line_items;

	String payment_id;
	String status;
	int expire_by;
	int issued_at;
	int paid_at;
	int cancelled_at;
	int expired_at;
	String sms_status;
	String email_status;
	int date;
	int payment_amount;
	int payment_amount_paid;
	int payment_amount_due;
	String payment_currency;
	String payment_currency_symbol;
	String short_url;
	String payment_type;
	int created_at;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getInvoice_number() {
		return invoice_number;
	}

	public void setInvoice_number(String invoice_number) {
		this.invoice_number = invoice_number;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getSub_id() {
		return sub_id;
	}

	public void setSub_id(String sub_id) {
		this.sub_id = sub_id;
	}

	public List<LineItems> getLine_items() {
		return line_items;
	}

	public void setLine_items(List<LineItems> line_items) {
		this.line_items = line_items;
	}

	public String getBilling_address() {
		return billing_address;
	}

	public void setBilling_address(String billing_address) {
		this.billing_address = billing_address;
	}

	public String getShipping_address() {
		return shipping_address;
	}

	public void setShipping_address(String shipping_address) {
		this.shipping_address = shipping_address;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getCustomer_email() {
		return customer_email;
	}

	public void setCustomer_email(String customer_email) {
		this.customer_email = customer_email;
	}

	public String getCustomer_contact() {
		return customer_contact;
	}

	public void setCustomer_contact(String customer_contact) {
		this.customer_contact = customer_contact;
	}

	public String getPayment_id() {
		return payment_id;
	}

	public void setPayment_id(String payment_id) {
		this.payment_id = payment_id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getExpire_by() {
		return expire_by;
	}

	public void setExpire_by(int expire_by) {
		this.expire_by = expire_by;
	}

	public int getIssued_at() {
		return issued_at;
	}

	public void setIssued_at(int issued_at) {
		this.issued_at = issued_at;
	}

	public int getPaid_at() {
		return paid_at;
	}

	public void setPaid_at(int paid_at) {
		this.paid_at = paid_at;
	}

	public int getCancelled_at() {
		return cancelled_at;
	}

	public void setCancelled_at(int cancelled_at) {
		this.cancelled_at = cancelled_at;
	}

	public int getExpired_at() {
		return expired_at;
	}

	public void setExpired_at(int expired_at) {
		this.expired_at = expired_at;
	}

	public String getSms_status() {
		return sms_status;
	}

	public void setSms_status(String sms_status) {
		this.sms_status = sms_status;
	}

	public String getEmail_status() {
		return email_status;
	}

	public void setEmail_status(String email_status) {
		this.email_status = email_status;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_amount(int payment_amount) {
		this.payment_amount = payment_amount;
	}

	public int getPayment_amount_paid() {
		return payment_amount_paid;
	}

	public void setPayment_amount_paid(int payment_amount_paid) {
		this.payment_amount_paid = payment_amount_paid;
	}

	public int getPayment_amount_due() {
		return payment_amount_due;
	}

	public void setPayment_amount_due(int payment_amount_due) {
		this.payment_amount_due = payment_amount_due;
	}

	public String getPayment_currency() {
		return payment_currency;
	}

	public void setPayment_currency(String payment_currency) {
		this.payment_currency = payment_currency;
	}

	public String getPayment_currency_symbol() {
		return payment_currency_symbol;
	}

	public void setPayment_currency_symbol(String payment_currency_symbol) {
		this.payment_currency_symbol = payment_currency_symbol;
	}

	public String getShort_url() {
		return short_url;
	}

	public void setShort_url(String short_url) {
		this.short_url = short_url;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public int getCreated_at() {
		return created_at;
	}

	public void setCreated_at(int created_at) {
		this.created_at = created_at;
	}

}

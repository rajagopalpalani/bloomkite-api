package com.sowisetech.membership.model;

public class SubPaymentCard {

	String razorpayPaymentId;
	String razorpayCardId;
	String name;
	int last4;
	String network;
	String type;
	String issuer;
	boolean international;
	boolean emi;
	int expiry_month;
	int expiry_year;

	public String getRazorpayPaymentId() {
		return razorpayPaymentId;
	}

	public void setRazorpayPaymentId(String razorpayPaymentId) {
		this.razorpayPaymentId = razorpayPaymentId;
	}

	public String getRazorpayCardId() {
		return razorpayCardId;
	}

	public void setRazorpayCardId(String razorpayCardId) {
		this.razorpayCardId = razorpayCardId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLast4() {
		return last4;
	}

	public void setLast4(int last4) {
		this.last4 = last4;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public boolean isInternational() {
		return international;
	}

	public void setInternational(boolean international) {
		this.international = international;
	}

	public boolean isEmi() {
		return emi;
	}

	public void setEmi(boolean emi) {
		this.emi = emi;
	}

	public int getExpiry_month() {
		return expiry_month;
	}

	public void setExpiry_month(int expiry_month) {
		this.expiry_month = expiry_month;
	}

	public int getExpiry_year() {
		return expiry_year;
	}

	public void setExpiry_year(int expiry_year) {
		this.expiry_year = expiry_year;
	}

}

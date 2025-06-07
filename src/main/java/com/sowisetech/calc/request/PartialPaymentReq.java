package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class PartialPaymentReq {

	String partPayDate;
	String partPayAmount;

	public String getPartPayDate() {
		return partPayDate;
	}

	public void setPartPayDate(String partPayDate) {
		this.partPayDate = partPayDate;
	}

	public String getPartPayAmount() {
		return partPayAmount;
	}

	public void setPartPayAmount(String partPayAmount) {
		this.partPayAmount = partPayAmount;
	}

}

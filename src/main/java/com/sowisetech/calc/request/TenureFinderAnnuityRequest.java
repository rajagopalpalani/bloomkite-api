package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class TenureFinderAnnuityRequest {

	String presentValue;
	String futureValue;
	String rateOfInterest;

	public String getPresentValue() {
		return presentValue;
	}

	public void setPresentValue(String presentValue) {
		this.presentValue = presentValue;
	}

	public String getFutureValue() {
		return futureValue;
	}

	public void setFutureValue(String futureValue) {
		this.futureValue = futureValue;
	}

	public String getRateOfInterest() {
		return rateOfInterest;
	}

	public void setRateOfInterest(String rateOfInterest) {
		this.rateOfInterest = rateOfInterest;
	}

}

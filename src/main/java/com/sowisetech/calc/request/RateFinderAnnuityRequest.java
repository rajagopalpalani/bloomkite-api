package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class RateFinderAnnuityRequest {

	String presentValue;
	String futureValue;
	String duration;

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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

}

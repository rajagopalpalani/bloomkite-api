package com.sowisetech.calc.request;

import org.springframework.stereotype.Component;

@Component
public class RateFinderRequest {

	private int screenId;
	private String invType;
	private String presentValue;
	private String futureValue;
	private String duration;
	private String durationType;
	private String referenceId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getInvType() {
		return invType;
	}

	public void setInvType(String invType) {
		this.invType = invType;
	}

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

	public String getDurationType() {
		return durationType;
	}

	public void setDurationType(String durationType) {
		this.durationType = durationType;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

}

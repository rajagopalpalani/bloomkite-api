package com.sowisetech.membership.request;

import org.springframework.stereotype.Component;

@Component
public class PauseAndResumeSubRequest {

	private int screenId;
	private String advId;
	private String razorpaySubId;
	private String key;

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getRazorpaySubId() {
		return razorpaySubId;
	}

	public void setRazorpaySubId(String razorpaySubId) {
		this.razorpaySubId = razorpaySubId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}

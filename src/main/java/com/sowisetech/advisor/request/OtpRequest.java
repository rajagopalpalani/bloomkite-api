package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class OtpRequest {

	private String phoneNumber;
	private String otp;
	private String type;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

package com.sowisetech.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:sms.properties")
public class SmsConstants {

	public static final String OTP = "OTP";

	@Value("${apikey}")
	private String apikey;
	@Value("${otp_message}")
	private String otp_message;
	@Value("${sender}")
	private String sender;
	@Value("${india_code}")
	private String india_code;
	@Value("${otp_validity}")
	private int otp_validity;

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public String getOtp_message() {
		return otp_message;
	}

	public void setOtp_message(String otp_message) {
		this.otp_message = otp_message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getIndia_code() {
		return india_code;
	}

	public void setIndia_code(String india_code) {
		this.india_code = india_code;
	}

	public static String getOtp() {
		return OTP;
	}

	public int getOtp_validity() {
		return otp_validity;
	}

	public void setOtp_validity(int otp_validity) {
		this.otp_validity = otp_validity;
	}

}

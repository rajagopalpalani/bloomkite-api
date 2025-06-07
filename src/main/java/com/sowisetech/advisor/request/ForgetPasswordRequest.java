package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class ForgetPasswordRequest {

//	private int screenId;
	private String emailId;

//	public int getScreenId() {
//		return screenId;
//	}
//
//	public void setScreenId(int screenId) {
//		this.screenId = screenId;
//	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}

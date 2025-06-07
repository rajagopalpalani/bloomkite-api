package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class ResetPasswordRequest {

//	private int screenId;
	private String newPassword;
//	private String emailId;

//	public int getScreenId() {
//		return screenId;
//	}
//
//	public void setScreenId(int screenId) {
//		this.screenId = screenId;
//	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
//
//	public String getEmailId() {
//		return emailId;
//	}
//
//	public void setEmailId(String emailId) {
//		this.emailId = emailId;
//	}

}

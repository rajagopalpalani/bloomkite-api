package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class MailTemplateRequest {
	private String emailId;
	private String template;

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}

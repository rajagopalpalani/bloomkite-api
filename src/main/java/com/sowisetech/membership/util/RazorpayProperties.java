package com.sowisetech.membership.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:razorpay.properties")
@Component
public class RazorpayProperties {

	@Value("${webhook_secret}")
	public String webhook_secret;

	@Value("${encryption_password}")
	public String encryption_password;

	@Value("${status_cancelled}")
	public String status_cancelled;

	@Value("${status_completed}")
	public String status_completed;

	public String getWebhook_secret() {
		return webhook_secret;
	}

	public void setWebhook_secret(String webhook_secret) {
		this.webhook_secret = webhook_secret;
	}

	public String getEncryption_password() {
		return encryption_password;
	}

	public void setEncryption_password(String encryption_password) {
		this.encryption_password = encryption_password;
	}

	public String getStatus_cancelled() {
		return status_cancelled;
	}

	public void setStatus_cancelled(String status_cancelled) {
		this.status_cancelled = status_cancelled;
	}

	public String getStatus_completed() {
		return status_completed;
	}

	public void setStatus_completed(String status_completed) {
		this.status_completed = status_completed;
	}

}

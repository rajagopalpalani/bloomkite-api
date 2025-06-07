package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class ResendMailRequest {

	private String key;
	private String token;
	private String teamEmail;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTeamEmail() {
		return teamEmail;
	}

	public void setTeamEmail(String teamEmail) {
		this.teamEmail = teamEmail;
	}

}

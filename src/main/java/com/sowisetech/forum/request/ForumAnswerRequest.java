package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumAnswerRequest {
	private int screenId;
	private String answer;
	private long partyId;
	private long queryId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getQueryId() {
		return queryId;
	}

	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}

}

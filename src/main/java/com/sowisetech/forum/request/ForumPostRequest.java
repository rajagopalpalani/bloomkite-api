package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumPostRequest {

	private int screenId;
	private String content;
	private long partyId;
	private long threadId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

}

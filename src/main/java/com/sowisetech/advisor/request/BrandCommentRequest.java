package com.sowisetech.advisor.request;

public class BrandCommentRequest {

	private int screenId;
	private String content;
	private long partyId;
	private String paramId;
	private long parentCommentId;

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

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}

	public long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}
}

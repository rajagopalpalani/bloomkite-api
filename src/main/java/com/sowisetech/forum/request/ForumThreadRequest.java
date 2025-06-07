package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumThreadRequest {

	private int screenId;
	private String subject;
	private long partyId;
	private long forumSubCategoryId;
	private long forumCategoryId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getForumSubCategoryId() {
		return forumSubCategoryId;
	}

	public void setForumSubCategoryId(long forumSubCategoryId) {
		this.forumSubCategoryId = forumSubCategoryId;
	}

	public long getForumCategoryId() {
		return forumCategoryId;
	}

	public void setForumCategoryId(long forumCategoryId) {
		this.forumCategoryId = forumCategoryId;
	}

}

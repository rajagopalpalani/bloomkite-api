package com.sowisetech.forum.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class QueryRequest {
	private int screenId;
	private String query;
	private List<Long> postedToPartyId;
	private long partyId;
	private long forumSubCategoryId;
	private long forumCategoryId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<Long> getPostedToPartyId() {
		return postedToPartyId;
	}

	public void setPostedToPartyId(List<Long> postedToPartyId) {
		this.postedToPartyId = postedToPartyId;
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

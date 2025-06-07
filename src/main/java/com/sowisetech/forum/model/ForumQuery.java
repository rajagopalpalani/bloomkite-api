package com.sowisetech.forum.model;

import java.sql.Timestamp;

public class ForumQuery {

	private long queryId;
	private String query;
	private long partyId;
	private long postedToPartyId;
	private long forumSubCategoryId;
	private long forumCategoryId;
	private Timestamp created;
	private Timestamp updated;
	private String delete_flag;
	private String created_by;
	private String updated_by;

	public long getQueryId() {
		return queryId;
	}

	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getPostedToPartyId() {
		return postedToPartyId;
	}

	public void setPostedToPartyId(long postedToPartyId) {
		this.postedToPartyId = postedToPartyId;
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

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

}

package com.sowisetech.forum.model;

public class ForumSubCategory {

	private long forumSubCategoryId;
	private String name;
	private long forumCategoryId;

	public long getForumSubCategoryId() {
		return forumSubCategoryId;
	}

	public void setForumSubCategoryId(long forumSubCategoryId) {
		this.forumSubCategoryId = forumSubCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getForumCategoryId() {
		return forumCategoryId;
	}

	public void setForumCategoryId(long forumCategoryId) {
		this.forumCategoryId = forumCategoryId;
	}

}

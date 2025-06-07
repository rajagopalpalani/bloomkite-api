package com.sowisetech.forum.model;

import java.util.List;

public class ForumCategory {

	private long forumCategoryId;
	private String name;

	private List<ForumSubCategory> forumSubCategory;

	public long getForumCategoryId() {
		return forumCategoryId;
	}

	public void setForumCategoryId(long forumCategoryId) {
		this.forumCategoryId = forumCategoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ForumSubCategory> getForumSubCategory() {
		return forumSubCategory;
	}

	public void setForumSubCategory(List<ForumSubCategory> forumSubCategory) {
		this.forumSubCategory = forumSubCategory;
	}

}

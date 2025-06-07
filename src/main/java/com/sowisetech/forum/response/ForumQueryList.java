package com.sowisetech.forum.response;

import java.util.List;

import com.sowisetech.forum.model.ForumQuery;

public class ForumQueryList {

	long totalRecords;
	List<ForumQuery> forumQueryList;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ForumQuery> getForumQueryList() {
		return forumQueryList;
	}

	public void setForumQueryList(List<ForumQuery> forumQueryList) {
		this.forumQueryList = forumQueryList;
	}

}

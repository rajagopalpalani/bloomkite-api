package com.sowisetech.forum.response;

import java.util.List;

import com.sowisetech.forum.model.ForumAnswer;

public class ForumAnswerList {

	long totalRecords;
	List<ForumAnswer> forumAnswerList;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ForumAnswer> getForumAnswerList() {
		return forumAnswerList;
	}

	public void setForumAnswerList(List<ForumAnswer> forumAnswerList) {
		this.forumAnswerList = forumAnswerList;
	}

}

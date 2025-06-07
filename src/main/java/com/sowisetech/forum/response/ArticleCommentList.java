package com.sowisetech.forum.response;

import java.util.List;

import com.sowisetech.forum.model.ArticleComment;

public class ArticleCommentList {

	long totalRecords;
	List<ArticleComment> articleCommentList;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ArticleComment> getArticleCommentList() {
		return articleCommentList;
	}

	public void setArticleCommentList(List<ArticleComment> articleCommentList) {
		this.articleCommentList = articleCommentList;
	}

}

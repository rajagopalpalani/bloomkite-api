package com.sowisetech.forum.response;

import java.util.List;

import com.sowisetech.forum.model.ArticlePost;

public class ArticlePostList {

	long totalRecords;
	List<ArticlePost> articlePostList;

	public long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public List<ArticlePost> getArticlePostList() {
		return articlePostList;
	}

	public void setArticlePostList(List<ArticlePost> articlePostList) {
		this.articlePostList = articlePostList;
	}

}

package com.sowisetech.forum.request;

public class ChangeStatusRequest {

	private int screenId;
	private long articleId;
	private int articleStatusId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public int getArticleStatusId() {
		return articleStatusId;
	}

	public void setArticleStatusId(int articleStatusId) {
		this.articleStatusId = articleStatusId;
	}

}

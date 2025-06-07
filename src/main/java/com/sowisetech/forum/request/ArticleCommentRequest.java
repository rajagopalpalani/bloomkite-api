package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ArticleCommentRequest {

	private int screenId;
	private String content;
	private long partyId;
	private long articleId;
	private long parentCommentId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getContent() {
		return content;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

}

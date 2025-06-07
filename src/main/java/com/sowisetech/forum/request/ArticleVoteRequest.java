package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ArticleVoteRequest {

	private int screenId;
	private long voteType;
	private long partyId;
	private long articleId;
	private long commentId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public long getVoteType() {
		return voteType;
	}

	public void setVoteType(long voteType) {
		this.voteType = voteType;
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

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

}

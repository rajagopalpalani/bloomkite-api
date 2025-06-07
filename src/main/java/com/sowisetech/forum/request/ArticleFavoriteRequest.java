package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ArticleFavoriteRequest {

	private int screenId;
	private long partyId;
	private long articleId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
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

}

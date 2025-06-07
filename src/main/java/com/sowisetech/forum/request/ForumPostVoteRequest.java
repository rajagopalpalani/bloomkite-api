package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ForumPostVoteRequest {

	private int screenId;
	private long voteType;
	private long partyId;
	private long postId;

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

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

}

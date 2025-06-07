package com.sowisetech.forum.model;

public class ForumPostVote {

	private long voteId;
	private long up_count;
	private long down_count;
	private long postId;

	public long getVoteId() {
		return voteId;
	}

	public void setVoteId(long voteId) {
		this.voteId = voteId;
	}

	

	public long getUp_count() {
		return up_count;
	}

	public void setUp_count(long up_count) {
		this.up_count = up_count;
	}

	public long getDown_count() {
		return down_count;
	}

	public void setDown_count(long down_count) {
		this.down_count = down_count;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

}

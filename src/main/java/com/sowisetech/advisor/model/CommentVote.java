package com.sowisetech.advisor.model;

public class CommentVote {

	private long commentVoteId;
	private long upCount;
	private long downCount;
	private long commentId;

	public long getCommentVoteId() {
		return commentVoteId;
	}

	public void setCommentVoteId(long commentVoteId) {
		this.commentVoteId = commentVoteId;
	}

	public long getUpCount() {
		return upCount;
	}

	public void setUpCount(long upCount) {
		this.upCount = upCount;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getDownCount() {
		return downCount;
	}

	public void setDownCount(long downCount) {
		this.downCount = downCount;
	}

}

package com.sowisetech.forum.request;

import org.springframework.stereotype.Component;

@Component
public class ModerateRequest {

	private int screenId;
	private long forumStatusId;
	private String adminId;
	private long articleId;
	private long commentId;
	private String reason;
	private long threadId;
	private long postId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public long getForumStatusId() {
		return forumStatusId;
	}

	public void setForumStatusId(long forumStatusId) {
		this.forumStatusId = forumStatusId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

}

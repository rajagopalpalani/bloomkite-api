package com.sowisetech.forum.model;

import java.sql.Timestamp;
import java.util.List;

public class ArticleComment {

	private long commentId;
	private String content;
	private Timestamp created;
	private long forumStatusId;
	private long partyId;
	private String name;
	private String designation;
	private String imagePath;
	private long articleId;
	private Timestamp updated;
	private String adminId;
	private String delete_flag;
	private String created_by;
	private String updated_by;
	private long parentCommentId;
	private String userName;
	private List<ArticleComment> replyComments;

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public long getCommentId() {
		return commentId;
	}

	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}

	public long getForumStatusId() {
		return forumStatusId;
	}

	public void setForumStatusId(long forumStatusId) {
		this.forumStatusId = forumStatusId;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public long getParentCommentId() {
		return parentCommentId;
	}

	public void setParentCommentId(long parentCommentId) {
		this.parentCommentId = parentCommentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ArticleComment> getReplyComments() {
		return replyComments;
	}

	public void setReplyComments(List<ArticleComment> replyComments) {
		this.replyComments = replyComments;
	}

}

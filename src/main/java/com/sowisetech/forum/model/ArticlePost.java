package com.sowisetech.forum.model;

import java.sql.Timestamp;

public class ArticlePost {

	private long articleId;
	private String title;
	private String url;
	private String content;
	private long forumStatusId;
	private long partyId;
	private String name;
	private String designation;
	private String imagePath;
	private String adminId;
	private String delete_flag;
	private String reason;
	private long articleStatusId;
	private Timestamp created;
	private Timestamp updated;
	private String created_by;
	private String updated_by;
	private String forumStatus;
	private String articleStatus;
	private long prodId;
	private String userName;
	private String roleBasedId;

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getArticleStatusId() {
		return articleStatusId;
	}

	public void setArticleStatusId(long articleStatusId) {
		this.articleStatusId = articleStatusId;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
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

	public String getForumStatus() {
		return forumStatus;
	}

	public void setForumStatus(String forumStatus) {
		this.forumStatus = forumStatus;
	}

	public String getArticleStatus() {
		return articleStatus;
	}

	public void setArticleStatus(String articleStatus) {
		this.articleStatus = articleStatus;
	}

	public long getProdId() {
		return prodId;
	}

	public void setProdId(long prodId) {
		this.prodId = prodId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleBasedId() {
		return roleBasedId;
	}

	public void setRoleBasedId(String roleBasedId) {
		this.roleBasedId = roleBasedId;
	}

	
}

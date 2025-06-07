package com.sowisetech.advisor.request;

public class BloggerIdRequest {

	private int screenId;
	private String bloggerId;
	private String userName;
	private String roleBasedId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getBloggerId() {
		return bloggerId;
	}

	public void setBloggerId(String bloggerId) {
		this.bloggerId = bloggerId;
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

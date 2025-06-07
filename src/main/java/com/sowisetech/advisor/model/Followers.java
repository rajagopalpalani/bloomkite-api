package com.sowisetech.advisor.model;

import java.sql.Timestamp;

public class Followers {

	private long followersId;
	private String userId;
	private long userType;
	private long status;
	private String advId;
	private Timestamp created;
	private String created_by;
	private Timestamp updated;
	private String updated_by;
	private String byWhom;
	private String userName;

	
	private String name;
	private String profileImage;
	private long partyId;

	public long getFollowersId() {
		return followersId;
	}

	public void setFollowersId(long followersId) {
		this.followersId = followersId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public long getUserType() {
		return userType;
	}

	public void setUserType(long userType) {
		this.userType = userType;
	}

	public long getStatus() {
		return status;
	}

	public void setStatus(long statusId) {
		this.status = statusId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
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

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getByWhom() {
		return byWhom;
	}

	public void setByWhom(String byWhom) {
		this.byWhom = byWhom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	

}

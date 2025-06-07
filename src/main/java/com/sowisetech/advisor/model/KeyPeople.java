package com.sowisetech.advisor.model;

import java.sql.Timestamp;

public class KeyPeople {

	private long keyPeopleId;
	private String fullName;
	private String designation;
	private long parentPartyId;
	private String image;
	private Timestamp created;
	private Timestamp updated;
	private String delete_flag;
	private String created_by;
	private String updated_by;

	public long getKeyPeopleId() {
		return keyPeopleId;
	}

	public void setKeyPeopleId(long keyPeopleId) {
		this.keyPeopleId = keyPeopleId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public long getParentPartyId() {
		return parentPartyId;
	}

	public void setParentPartyId(long parentPartyId) {
		this.parentPartyId = parentPartyId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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

}

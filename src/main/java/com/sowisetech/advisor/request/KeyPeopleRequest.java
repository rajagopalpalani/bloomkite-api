package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class KeyPeopleRequest {

	private int screenId;
	private long keyPeopleId;
	private String fullName;
	private String designation;
	private String image;
	private long parentPartyId;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

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

}

package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class AdvPersonalInfoRequest {
	private int screenId;
	private String userName;
	private String designation;
	private String displayName;
	private String firmType;
	private String corporateLable;
	private String website;
	private String dob;
	private String gender;
	private String address1;
	private String address2;
	private String state;
	private String city;
	private String pincode;
	private String aboutme;
	private String advId;
	private String imagePath;
	private String name;
	private String panNumber; 

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFirmType() {
		return firmType;
	}

	public void setFirmType(String firmType) {
		this.firmType = firmType;
	}

	public String getCorporateLable() {
		return corporateLable;
	}

	public void setCorporateLable(String corporateLable) {
		this.corporateLable = corporateLable;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getAboutme() {
		return aboutme;
	}

	public void setAboutme(String aboutme) {
		this.aboutme = aboutme;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

}

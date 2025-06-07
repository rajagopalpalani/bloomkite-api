package com.sowisetech.advisor.model;

import java.util.List;

public class Adv {

	private String advId;
	private String name;
	private int promoCount;

	private String designation;
	private String emailId;
	private String phoneNumber;
	private String password;
	private String userName;
	private long partyStatusId;
	private long parentPartyId;
	private long partyId;
	private String delete_flag;
	private int advType;
	private String displayName;
	private String firmType;
	private String corporateLable;
	private String website;
	private String dob;
	private String gender;
	private String panNumber;
	private String address1;
	private String address2;
	private String state;
	private String city;
	private String pincode;
	private String aboutme;
	private String imagePath;
	private int isVerified;
	// private String verifiedBy;
	// private Timestamp verified;
	private int isMobileVerified;
	private int workFlowStatus;
	private String corporateUsername;
	// private Timestamp approvedDate;
	// private String approvedBy;
	// private Timestamp revokedDate;
	// private String revokedBy;

	// private String created_by;
	// private String updated_by;
	// private Timestamp created;
	// private Timestamp updated;

	private List<Award> awards;
	private List<Certificate> certificates;
	private List<Education> educations;
	private List<Experience> experiences;
	private List<AdvProduct> advProducts;
	private List<AdvBrandInfo> advBrandInfo;
	private List<AdvBrandRank> advBrandRank;
	private List<Promotion> promotions;
	private List<KeyPeople> keyPeopleList;
	private List<PlanDetail> planDetailList;
	private List<Advisor> teamMemberList;

	public List<PlanDetail> getPlanDetailList() {
		return planDetailList;
	}

	public void setPlanDetailList(List<PlanDetail> planDetailList) {
		this.planDetailList = planDetailList;
	}

	public List<KeyPeople> getKeyPeopleList() {
		return keyPeopleList;
	}

	public void setKeyPeopleList(List<KeyPeople> keyPeopleList) {
		this.keyPeopleList = keyPeopleList;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public String getName() {
		return name;
	}

	public String getCorporateUsername() {
		return corporateUsername;
	}

	public void setCorporateUsername(String corporateUsername) {
		this.corporateUsername = corporateUsername;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getPartyStatusId() {
		return partyStatusId;
	}

	public void setPartyStatusId(long partyStatusId) {
		this.partyStatusId = partyStatusId;
	}

	public long getParentPartyId() {
		return parentPartyId;
	}

	public void setParentPartyId(long parentPartyId) {
		this.parentPartyId = parentPartyId;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public int getAdvType() {
		return advType;
	}

	public void setAdvType(int advType) {
		this.advType = advType;
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

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
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

	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public int getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(int workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public List<Award> getAwards() {
		return awards;
	}

	public void setAwards(List<Award> awards) {
		this.awards = awards;
	}

	public List<Certificate> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<Certificate> certificates) {
		this.certificates = certificates;
	}

	public List<Education> getEducations() {
		return educations;
	}

	public void setEducations(List<Education> educations) {
		this.educations = educations;
	}

	public List<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}

	public List<AdvProduct> getAdvProducts() {
		return advProducts;
	}

	public void setAdvProducts(List<AdvProduct> advProducts) {
		this.advProducts = advProducts;
	}

	public List<AdvBrandInfo> getAdvBrandInfo() {
		return advBrandInfo;
	}

	public void setAdvBrandInfo(List<AdvBrandInfo> advBrandInfo) {
		this.advBrandInfo = advBrandInfo;
	}

	public List<AdvBrandRank> getAdvBrandRank() {
		return advBrandRank;
	}

	public void setAdvBrandRank(List<AdvBrandRank> advBrandRank) {
		this.advBrandRank = advBrandRank;
	}

	public List<Promotion> getPromotions() {
		return promotions;
	}

	public void setPromotions(List<Promotion> promotions) {
		this.promotions = promotions;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public List<Advisor> getTeamMemberList() {
		return teamMemberList;
	}

	public void setTeamMemberList(List<Advisor> teamMemberList) {
		this.teamMemberList = teamMemberList;
	}

	public int getIsMobileVerified() {
		return isMobileVerified;
	}

	public void setIsMobileVerified(int isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}

	public int getPromoCount() {
		return promoCount;
	}

	public void setPromoCount(int promoCount) {
		this.promoCount = promoCount;
	}

}

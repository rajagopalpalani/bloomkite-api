package com.sowisetech.investor.model;

import java.sql.Timestamp;
import java.util.List;

import com.sowisetech.advisor.model.PlanDetail;

public class Investor {

	private String invId;
	private String fullName;
	private String displayName;
	private String dob;
	private String gender;
	private String emailId;
	private String phoneNumber;
	private String password;
	private String userName;
	private String pincode;
	private long partyStatusId;
	private Timestamp created;
	private Timestamp updated;
	private String delete_flag;
	private long partyId;
	private int isVerified;
	private String verifiedBy;
	private Timestamp verified;
	private String created_by;
	private String updated_by;
	private String imagePath;
	private int isMobileVerified;
	private List<PlanDetail> planDetailList;

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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

	private List<InvInterest> invInterest;

	public long getPartyStatusId() {
		return partyStatusId;
	}

	public void setPartyStatusId(long partyStatusId) {
		this.partyStatusId = partyStatusId;
	}

	public List<InvInterest> getInvInterest() {
		return invInterest;
	}

	public void setInvInterest(List<InvInterest> invInterest) {
		this.invInterest = invInterest;
	}

	public String getInvId() {
		return invId;
	}

	public void setInvId(String invId) {
		this.invId = invId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(String delete_flag) {
		this.delete_flag = delete_flag;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public int getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(int isVerified) {
		this.isVerified = isVerified;
	}

	public String getVerifiedBy() {
		return verifiedBy;
	}

	public void setVerifiedBy(String verifiedBy) {
		this.verifiedBy = verifiedBy;
	}

	public Timestamp getVerified() {
		return verified;
	}

	public void setVerified(Timestamp verified) {
		this.verified = verified;
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

	public int getIsMobileVerified() {
		return isMobileVerified;
	}

	public void setIsMobileVerified(int isMobileVerified) {
		this.isMobileVerified = isMobileVerified;
	}

	public List<PlanDetail> getPlanDetailList() {
		return planDetailList;
	}

	public void setPlanDetailList(List<PlanDetail> planDetailList) {
		this.planDetailList = planDetailList;
	}

}

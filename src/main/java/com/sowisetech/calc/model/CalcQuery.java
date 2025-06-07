package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class CalcQuery {

	private long calcQueryId;
	private String referenceId;
	private long partyId;
	private long postedToPartyId;
	private String receiverName;
	private String url;
	private Timestamp created;
	private Timestamp updated;
	private String delete_flag;
	private String created_by;
	private String updated_by;
	private String plans;
	private String name;
	private String displayName;
	private String planName;
	private int age;
	private String phoneNumber;
	private String emailId;

	// public String getQuery() {
	// return query;
	// }
	//
	// public void setQuery(String query) {
	// this.query = query;
	// }

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public long getCalcQueryId() {
		return calcQueryId;
	}

	public void setCalcQueryId(long calcQueryId) {
		this.calcQueryId = calcQueryId;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public long getPartyId() {
		return partyId;
	}

	public void setPartyId(long partyId) {
		this.partyId = partyId;
	}

	public long getPostedToPartyId() {
		return postedToPartyId;
	}

	public void setPostedToPartyId(long postedToPartyId) {
		this.postedToPartyId = postedToPartyId;
	}

	// public long getForumSubCategoryId() {
	// return forumSubCategoryId;
	// }
	//
	// public void setForumSubCategoryId(long forumSubCategoryId) {
	// this.forumSubCategoryId = forumSubCategoryId;
	// }
	//
	// public long getForumCategoryId() {
	// return forumCategoryId;
	// }
	//
	// public void setForumCategoryId(long forumCategoryId) {
	// this.forumCategoryId = forumCategoryId;
	// }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getPlans() {
		return plans;
	}

	public void setPlans(String plans) {
		this.plans = plans;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}

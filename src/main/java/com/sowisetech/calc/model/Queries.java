package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class Queries {

	private long queryId;
	private long calcQueryId;
	private long senderId;
	private String query;
	private String referenceId;
	private long receiverId;
	private String plans;
	private Timestamp created;
	private Timestamp updated;
	private String delete_flag;
	private String created_by;
	private String updated_by;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public long getQueryId() {
		return queryId;
	}

	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}

	public long getCalcQueryId() {
		return calcQueryId;
	}

	public void setCalcQueryId(long calcQueryId) {
		this.calcQueryId = calcQueryId;
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

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getPlans() {
		return plans;
	}

	public void setPlans(String plans) {
		this.plans = plans;
	}

}

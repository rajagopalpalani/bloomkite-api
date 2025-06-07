package com.sowisetech.calc.model;

import java.sql.Timestamp;

public class Priority {

	long priorityId;
	int priorityItemId;
	String priorityItem;
	// double value;
	// int timeLine;
	int urgencyId;
	String urgency;
	int priorityOrder;
	String referenceId;
	Timestamp created;
	Timestamp updated;
	String created_by;
	String updated_by;

	public long getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(long priorityId) {
		this.priorityId = priorityId;
	}

	public int getPriorityItemId() {
		return priorityItemId;
	}

	public void setPriorityItemId(int priorityItemId) {
		this.priorityItemId = priorityItemId;
	}

	public String getPriorityItem() {
		return priorityItem;
	}

	public void setPriorityItem(String priorityItem) {
		this.priorityItem = priorityItem;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	// public double getValue() {
	// return value;
	// }
	//
	// public void setValue(double value) {
	// this.value = value;
	// }
	//
	// public int getTimeLine() {
	// return timeLine;
	// }
	//
	// public void setTimeLine(int timeLine) {
	// this.timeLine = timeLine;
	// }

	public int getUrgencyId() {
		return urgencyId;
	}

	public void setUrgencyId(int urgencyId) {
		this.urgencyId = urgencyId;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public int getPriorityOrder() {
		return priorityOrder;
	}

	public void setPriorityOrder(int priorityOrder) {
		this.priorityOrder = priorityOrder;
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

}

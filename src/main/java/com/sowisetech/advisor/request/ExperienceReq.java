package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class ExperienceReq implements IJsonRequest {

	String company;
	String designation;
	String location;
	String fromYear;
	String toYear;
	long expId;
	boolean tillDate;

	public long getExpId() {
		return expId;
	}

	public void setExpId(long expId) {
		this.expId = expId;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFromYear() {
		return fromYear;
	}

	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}

	public String getToYear() {
		return toYear;
	}

	public void setToYear(String toYear) {
		this.toYear = toYear;
	}

	public boolean isTillDate() {
		return tillDate;
	}

	public void setTillDate(boolean tillDate) {
		this.tillDate = tillDate;
	}

}

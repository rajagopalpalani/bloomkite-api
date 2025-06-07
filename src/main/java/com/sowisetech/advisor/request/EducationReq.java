package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

@Component
public class EducationReq implements IJsonRequest {

	String institution;
	String degree;
	String field;
	String fromYear;
	String toYear;
	long eduId;
	boolean tillDate;

	public long getEduId() {
		return eduId;
	}

	public void setEduId(long eduId) {
		this.eduId = eduId;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
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

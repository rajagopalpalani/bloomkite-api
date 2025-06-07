package com.sowisetech.advisor.request;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sowisetech.advisor.request.AwardReq;

@Component
public class AdvProfessionalInfoRequest implements IJsonRequest {

	int screenId;
	String advId;
	long awardId;
	List<AwardReq> awards;
	List<CertificateReq> certificates;
	List<ExperienceReq> experiences;
	List<EducationReq> educations;
	long expId;
	long eduId;
	long certificateId;
	List<String> deleteAll;	

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public List<String> getDeleteAll() {
		return deleteAll;
	}

	public void setDeleteAll(List<String> deleteAll) {
		this.deleteAll = deleteAll;
	}

	public List<CertificateReq> getCertificates() {
		return certificates;
	}

	public void setCertificates(List<CertificateReq> certificates) {
		this.certificates = certificates;
	}

	public long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}

	public long getEduId() {
		return eduId;
	}

	public void setEduId(long eduId) {
		this.eduId = eduId;
	}

	public long getExpId() {
		return expId;
	}

	public void setExpId(long expId) {
		this.expId = expId;
	}

	public long getAwardId() {
		return awardId;
	}

	public void setAwardId(long awardId) {
		this.awardId = awardId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public List<AwardReq> getAwards() {
		return awards;
	}

	public void setAwards(List<AwardReq> awards) {
		this.awards = awards;
	}

	public List<ExperienceReq> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<ExperienceReq> experiences) {
		this.experiences = experiences;
	}

	public List<EducationReq> getEducations() {
		return educations;
	}

	public void setEducations(List<EducationReq> educations) {
		this.educations = educations;
	}

}

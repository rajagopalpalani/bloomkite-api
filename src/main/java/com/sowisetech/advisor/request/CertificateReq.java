package com.sowisetech.advisor.request;

import org.springframework.stereotype.Component;

import com.sowisetech.advisor.model.Certificate;
import com.sowisetech.advisor.request.IJsonRequest;

@Component
public class CertificateReq implements IJsonRequest {
	String title;
	String issuedBy;
	String year;
	String imagePath;
	long certificateId;

	public long getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(long certificateId) {
		this.certificateId = certificateId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
